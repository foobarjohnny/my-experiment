/**
 * 
 */
package com.telenav.cserver.dsr.streaming.proxy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.audio.recognition.ds.Stop;
import com.telenav.audio.streaming.Packet;
import com.telenav.audio.streaming.ProcessorReturn;
import com.telenav.audio.streaming.domain.ProtoLocation;
import com.telenav.audio.streaming.domain.ProtoMetaInfo;
import com.telenav.audio.streaming.domain.ProtoRecResult;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.framework.RecognizerProxy;
import com.telenav.cserver.dsr.framework.RecognizerResultListener;
import com.telenav.cserver.dsr.streaming.server.SocketClient;
import com.telenav.cserver.dsr.util.DsrConfigure;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.livestats.client.LiveStatsPacketFactory;
import com.telenav.livestats.client.LiveStatsPacketFactoryBuilder;

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

/**
 * @author joses
 *
 * 
 */
public class DSRStreamingProxy implements RecognizerProxy {
	
	private static Logger logger = Logger.getLogger(DSRStreamingProxy.class.getName());
	
	ProcessObject procObj = null;
	SocketClient dsrClient = null;
	byte[] response;
	
	ReentrantLock responseReceivedLock = new ReentrantLock();
	
	boolean dsrResponseReceived = false;
	Thread dsrReadingThread;
	List<RecognizerResultListener> listeners = new ArrayList<RecognizerResultListener>();
	
   	private static final String statsUrl = "livestats-ivr.mypna.com";
   	private static final int statsPort = 8088;
   	private static LiveStatsPacketFactory livestatsFactory;
   	private String livestatsStr = "Stable";
   	   	
   	static {
   		try {
   			logger.fine("Connecting to livestats at URL = " + statsUrl
   					+ ", port = " + statsPort);
			livestatsFactory = LiveStatsPacketFactoryBuilder.buildPacketFactory(statsUrl, statsPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	}
   	
   	
	public void init(ProcessObject obj)
	{   
		List<String> config = DsrConfigure.getDSRConfig(obj.context.recType);
		
		String host = config.get(0);
		int port = Integer.parseInt(config.get(1));
		logger.info("DSR Host: "+host+", Port: "+port);

		if(host.equals("vdsrab.mypna.com")){
			obj.setPilot(true);
			livestatsStr = "Pilot";
		}
		
		init(obj, host, port);
	}
	
	public void init(ProcessObject obj, String host, int port) {
	       
		procObj = obj;
		RecContext context = procObj.getContext();	
		
		try
		{
			dsrClient = new SocketClient(host, port);
			dsrClient.setTimeout(180000);
		}
		catch (Exception e)
		{
			dsrClient = null;
		    logger.log(Level.SEVERE, "Unable to reach DSR server: "+host+":"+port);
		    handleError(ResourceConst.SERVER_ERROR, "Unable to reach DSR server.");
		    return;
		}
		
		// Start DSR thread
		dsrReadingThread = new Thread(new DSRRead());
		dsrReadingThread.start();
		
		logger.log(Level.FINE, "Opened Socket Connection to DSR");
		
		//SET META INFO
		ProtoMetaInfo meta = assembleDSRMeta(procObj);
		
		try
		{
			if (dsrClient != null)
			{
				dsrClient.sendPacket(Packet.PacketType.META, meta.toBytes());
				logger.log(Level.FINE, "Sent Meta Packet Info to DSR");
			}
			else
				logger.log(Level.SEVERE, "Unable to send meta packet: DSR client is null");
		}
		catch (Exception e)
		{
			dsrClient = null;
			logger.log(Level.SEVERE, "Unable to send meta data to DSR");
		    handleError(ResourceConst.SERVER_ERROR, "Unable to send meta data to DSR.");
		    return;
		}
	}
	
	public void handleError(int code, String message)
	{
		responseReceivedLock.lock();
		dsrResponseReceived = true;
		responseReceivedLock.unlock();
		
	    // call back error to response listeners
	    procObj.statusCode = code;
	    procObj.errorMessage = message;
	    procObj.processedResults.clear();
	    
        
        try {
        	if(code == ProcessorReturn.STATUS_TIMEOUT){
    			livestatsFactory.createPacket()
				.add("type", "dsr")
				.add("request", "dsr-cserver")
				.add("service", livestatsStr)
				.add("requestCount", "1")
				.add("successCount", "1")
				.send();

        	}
        	else{
    			livestatsFactory.createPacket()
				.add("type", "dsr")
				.add("request", "dsr-cserver")
				.add("service", livestatsStr)
				.add("requestCount", "1")
				.add("successCount", "0")
				.send();
        	}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	    
		notifyListners(procObj);
	}
	

	public void addAudioSeg(byte[] audioData) {
        
		responseReceivedLock.lock();
		if(!dsrResponseReceived)
		{

			responseReceivedLock.unlock();
			try
			{
				if (dsrClient != null)
				{
					dsrClient.sendPacket(Packet.PacketType.NEW_AUDIO, audioData);
					logger.log(Level.FINE, "Sent audio data of "+audioData.length+" bytes to DSR");
				}
				else
					logger.log(Level.SEVERE, "Unable to send audio packet: DSR client is null");
			}
			catch (Exception e)
			{
				dsrClient = null;
				logger.log(Level.SEVERE, "Error sending audio to DSR.");
			    handleError(ResourceConst.SERVER_ERROR, "Error sending audio to DSR.");
			}
		}
		else
		{
			responseReceivedLock.unlock();
			logger.fine("DSR response arrived before receiving all audio.");
			if (dsrClient != null)
				dsrClient.disconnect();
		}
	}

	public void release()
	{	
		try
		{
			if (dsrClient != null)
			{
				//dsrClient.sendPacket(Packet.PacketType.CANCEL, new byte[0]);
				logger.log(Level.FINE, "CANCEL packet sent to DSR. Closing DSR connection");
			}
			else
				logger.log(Level.SEVERE, "Unable to send cancel packet: DSR client is null");
			
			dsrReadingThread.join();
		}
		catch (Exception e)
		{
			dsrClient = null;
			logger.log(Level.SEVERE, "Error sending cancel signal to DSR.");
		    handleError(ResourceConst.SERVER_ERROR, "Error sending cancel signal to DSR.");
		    return;
		}finally{
			
			if (dsrClient != null)
				dsrClient.disconnect();
		}
				
	}

	public void endAudio() {
		responseReceivedLock.lock();
		if(!dsrResponseReceived){
			try
			{
				if (dsrClient != null)
				{
					dsrClient.sendPacket(Packet.PacketType.END_AUDIO, new byte[0]);
					procObj.profile.recBegin();
					logger.log(Level.FINE, "Sent END AUDIO Packet to DSR");
				}
				else
					logger.log(Level.SEVERE, "Unable to send END AUDIO packet: DSR client is null");
			}
			catch (Exception e)
			{
				dsrClient = null;
				logger.log(Level.SEVERE, "Error sending end audio signal to DSR.");
			    handleError(ResourceConst.SERVER_ERROR, "Error sending end audio signal to DSR.");
			    return;
			}
		}
		responseReceivedLock.unlock();
		
	}

	private ProtoMetaInfo assembleDSRMeta(ProcessObject procObj){
		RecContext context = procObj.context;
		ProtoMetaInfo meta = new ProtoMetaInfo();
		meta.setUserId(context.user.getUserId());
		meta.setTransactionID(procObj.transactionId);
		meta.setFormat(context.audioFormat);
		meta.setRecType(context.recType);
		ProtoLocation location = new ProtoLocation();
		location.setCountry(context.location.country);
		location.setState(context.location.state);
		location.setCity(context.location.city);
		location.setLat(context.location.lat);
		location.setLon(context.location.lon);
		try{
			location.setId(Integer.parseInt(context.location.stopId));
		}catch(NumberFormatException nfe){
			logger.log(Level.WARNING, "Cannot parse stopId as Integer");
			location.setId(0);
		}
		meta.setLocation(location);
		logger.log(Level.FINE, "Handled Meta Info");
		
		//WRITE META TO DSR
		logger.log(Level.FINE, "Meta : "+
						"\n\tUserId: "+meta.getUserId()+
						"\n\tFormat: "+meta.getFormat()+
						"\n\tRecType: "+meta.getRecType()+
						"\n\tLocation: "+meta.getLocation().getLat()+" "+
										meta.getLocation().getLon()+" "+
										meta.getLocation().getCity()+" "+
										meta.getLocation().getState()+" "+
										meta.getLocation().getCountry());

		return meta;
	}
	
    //TODO: DEPRECATE LATER 
	public ProcessObject doRecognition(ProcessObject procObj, RecContext context, byte[] audioData) {return null;}
	
	private class DSRRead implements Runnable{

		public void run() {
			
			if (dsrClient == null)
				return ;
			
			dsrClient.setTimeout(180000);
			CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
	        cli.setFunctionName("DSRRead");

			logger.log(Level.FINE, "Start reading from DSR");
			try{
				response = dsrClient.getResponse();
			}
			catch(Exception e){
				dsrClient = null;
				logger.log(Level.SEVERE, "Error reading response from DSR. "+e.toString());
			    //handleError(ResourceConst.SERVER_ERROR, "Error reading response from DSR.");
			    handleError(1, "Error reading response from DSR");
			}
			
			responseReceivedLock.lock();
			dsrResponseReceived = true;
			responseReceivedLock.unlock();
			
			ProcessorReturn pReturn = null;
			try{
				pReturn = ProcessorReturn.fromBytes(response);
				procObj.profile.recEnd();
				logger.log(Level.FINE, "Response received from DSR");
			}catch(Exception e){
				cli.addData(CliConstants.LABEL_ERROR, "Exception while parsing DSR Response");
				cli.complete();
				String msg = "Exception while parsing DSR Response" ;
				logger.log(Level.SEVERE, msg+" "+e.toString()) ;
				//handleError(ResourceConst.SERVER_ERROR, msg);
				handleError(1, msg); 
				return ;
			}
			
			if(dsrClient!=null){
				dsrClient.disconnect();
				dsrClient = null;
			}
				
			if(pReturn==null)
			{
				cli.addData(CliConstants.LABEL_ERROR, "No reponse bytes return. Failed to recognize speech");
				cli.complete();
				String msg = "Null response from DSR server" ;
				logger.log(Level.SEVERE, msg) ;
				//handleError(ResourceConst.SERVER_ERROR, msg);
				//SocketClient class is timing out
				handleError(1, msg);
				return ;
			}
			else if (pReturn.getStatus() == ProcessorReturn.STATUS_FAILED)
			{
				cli.addData(CliConstants.LABEL_ERROR, "No reponse bytes return. Failed to recognize speech");
				cli.complete();
				String msg = new String(pReturn.getResponse()) ;
				logger.log(Level.SEVERE, msg) ;
				handleError(ResourceConst.SERVER_ERROR, msg);
				return ;
			}
			else if(pReturn.getStatus() == ProcessorReturn.STATUS_TIMEOUT)
			{
				cli.addData(CliConstants.LABEL_EVENT, "DSR Timed out");
				cli.complete();
				String msg = new String(pReturn.getResponse()) ;
				logger.log(Level.SEVERE, msg);
				handleError(ProcessorReturn.STATUS_TIMEOUT, msg);
				return;
			}
			
			ProtoRecResult dsrResults = null;
			RecResult[] rawResults = null;
			try{
				dsrResults = ProtoRecResult.fromBytes(pReturn.getResponse());
				rawResults = new RecResult[dsrResults.getResultItems().size()];
			}catch(Exception e){
				cli.addData(CliConstants.LABEL_ERROR, "Exception while parsing DSR Response");
				cli.complete();
				String msg = "Exception while parsing DSR Response" ;
				logger.log(Level.SEVERE, msg+" "+e.toString()) ;
				//handleError(ResourceConst.SERVER_ERROR, msg);
				handleError(1, msg);
				return ;
			}
			
            try {
				livestatsFactory.createPacket()
				.add("type", "dsr")
				.add("request", "dsr-cserver")
				.add("service", livestatsStr)
				.add("requestCount", "1")
				.add("successCount", "1")
				.send();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				logger.log(Level.SEVERE, e1.toString());
			} catch (IOException e2) {
				e2.printStackTrace();
				logger.log(Level.SEVERE, e2.toString());
			}  

			logger.log(Level.FINE, "Retrieved RecResult : "+dsrResults);
			cli.addData("DSR Results returned", rawResults.toString());
			cli.complete();
			
			for (int i=0; i<rawResults.length; i++)
			{
				ProtoRecResult.Item item = dsrResults.getResultItems().get(i);
				rawResults[i] = new RecResult();
				rawResults[i].setConfidence(item.getConfidence());
				rawResults[i].setValue(item.getLiteral());
				rawResults[i].setSlots(item.getSlots());
				//Fix for not getting back Stop object & availability of right Stop object
				Stop stop = new Stop();
				RecContext ctx = procObj.context;
				stop.setFirstLine(ctx.location.firstLine);
				stop.setCity(ctx.location.city);
				stop.setState(ctx.location.state);
				stop.setLat(ctx.location.lat);
				stop.setLon(ctx.location.lon);
				rawResults[i].setStop(stop);
			}
			
			procObj.setRawResults(rawResults);
			
			// We're done, notify listeners
			notifyListners(procObj);
		}
		
	}

	public void addResultListener(RecognizerResultListener listener) {
		listeners.add(listener) ;
	}
	
	private void notifyListners(ProcessObject procObj)
	{
		for (RecognizerResultListener listener : listeners)
			listener.handleResult(procObj);
	}
}
