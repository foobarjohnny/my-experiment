/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telenav.cserver.dsr;

import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.grizzly.util.OutputWriter;
import com.sun.grizzly.util.buf.ByteChunk;
import com.telenav.audio.streaming.ProcessorReturn;
import com.telenav.audio.streaming.common.Util;
import com.telenav.audio.streaming.server.AbstractStreamListener;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.ProcessConfiguration;
import com.telenav.cserver.dsr.framework.ProcessConfigurationFactory;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.framework.ProcessProfile;
import com.telenav.cserver.dsr.framework.RecognizerProxy;
import com.telenav.cserver.dsr.framework.RecognizerResultListener;
import com.telenav.cserver.dsr.protocol.ProtocolHandler;
import com.telenav.cserver.dsr.protocol.ProtocolHandlerFactory;
import com.telenav.cserver.dsr.protocol.TxNodeProtocolHandler;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.dsr.handler.IResultsHandler;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.impl.interceptor.TnContextInterceptor;
import com.telenav.cserver.framework.executor.protocol.txnode.TxNodeRequestParser;
import com.telenav.j2me.datatypes.DataConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author yueyulin, qpmeng May 6, 2008
 */
public class StreamPacketArrivalListener extends AbstractStreamListener {

    private static Logger logger = Logger.getLogger(StreamPacketArrivalListener.class.getName());

    TxNodeRequestParser nodeParser = new TxNodeRequestParser();
    TnContextInterceptor tnContextInterceptor = new TnContextInterceptor();
    ExecutorContext executorContext = new ExecutorContext();
    
    boolean isFirstPacket = true ;
    ProtocolHandler protocolHandler = null;
    ProcessProfile profile = new ProcessProfile();
    
    //STREAMING CHANGES
    boolean metaReceived = false;
    boolean metaSent = false;
    boolean stream = false;
    RecContext context = null;
    ProcessObject procObj = new ProcessObject();
    ProcessConfiguration processConfig = null;
    final ReentrantLock responseLock = new ReentrantLock();
    final Condition responseProcessed = responseLock.newCondition();
    final BooleanByReference finished = new BooleanByReference(false);
    List<IResultsHandler> handlerList = null;
    RecognizerResultListener listener = new RecognizerResultListener() {
		
		@Override
		public void handleResult(ProcessObject procObj)
		{
			try
			{
				logger.fine("DSR Status Code: "+procObj.getStatusCode());
				if(procObj.getStatusCode()==ResourceConst.SERVER_ERROR){
					//DSR Recognition Failure
					String responseMsg = "Failed to recognize speech.[-1]";
					handleError(responseMsg, responseMsg);
					return;
				}
				
				for(IResultsHandler handler : handlerList){
					procObj = handler.process(procObj);
				}			
				
				byte[] respBs = protocolHandler.formatResponse(procObj);
				
				writeResp(respBs);
			}
			catch (Exception e)
			{
	            String errorMsg = "Unable to process request due to "+e ;
	        	String responseMsg = "Recognition Error" ;
	        	handleError(errorMsg,responseMsg) ;
			}
			finally
			{
				responseLock.lock();
				finished.value = true;
				responseProcessed.signalAll();
				responseLock.unlock();
			}
		}
	};
	RecognizerProxy recProxy = null;
	
    private GrizzlyResponse grizzlyResponse;
   	
    public StreamPacketArrivalListener(SelectableChannel channel) {
        super(channel);
    }

    public StreamPacketArrivalListener(GrizzlyResponse resp) {
        this.grizzlyResponse = resp;
    }

    public void metaArrival(byte[] meta, int len) {
    	//STREAMING CHANGES
    	metaReceived = true;
    	
    	if (isFirstPacket)
    	{
    		profile.netIOBegin() ;
    		profile.totalProcessBegin() ;
    		isFirstPacket = false ;
    	}
    	
        logger.fine("meta unit arrived,size=" + len);
        super.metaArrival(meta, len);
    }

    public void packetUnitArrival(byte[] unit, int len) {
    	//STREAMING CHANGES
    	if(metaReceived && context == null){
    		context = handleMeta();
    		if(context == null)
    			return;
    		if(context.audioFormat != 2 && context.audioFormat != 4)
    			stream = true;
    	}
    	
    	if(metaReceived && !metaSent && stream){
    		
        	metaSent = true;
        	procObj.setContext(context);
        	
        	setProfileInfo(procObj);
        	
			procObj.setProfile(profile);
			
        	int configId = context.recType;
        	processConfig = ProcessConfigurationFactory.getConfiguration(""+configId) ;
        	handlerList = processConfig.getHandlers();
        	recProxy = processConfig.getProxy();
            recProxy.addResultListener(listener);
            recProxy.init(procObj);
    	}

    	if (isFirstPacket)
    	{
    		profile.netIOBegin() ;
    		profile.totalProcessBegin() ;
    		isFirstPacket = false ;
    	}
    	
        //at this situation unit.length == len
        logger.finer("unit arrived,size=" + len);
        super.packetUnitArrival(unit, len);
        
      //STREAMING CHANGES
        if(metaSent){
        	byte[] audioData = unit;
	        
	        if (audioData != null && audioData.length > 0) {
	            logger.fine("audio length=" + baosBuf.size());
	        } else {
	        	
	        	String errorMsg = "Invalid audio input, audio is empty" ;
	        	String responseMsg = "No speech to be recognized" ;
	        	handleError(errorMsg,responseMsg) ;
	        	return;
	        }
	        recProxy.addAudioSeg(audioData);
	        logger.fine("Sent audio data: "+audioData.length);
        }
    }
    
    public void cancelRequest()
	{
		//logger.warning("Cancelling request ...") ;
	}

    public synchronized void packetArrival(final byte[] packet, final byte[] lastPacketUnit, final int len) {

    	if(metaSent){
    		recProxy.endAudio();
    		logger.fine("Sent end audio");
            
            // ok, wait for response from the recognizer
            responseLock.lock();
            while (!finished.value)
            {
            	logger.info("Waiting for recognizer to finish.");
            	try {
					responseProcessed.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
            responseLock.unlock();
            
            logger.info("Processing complete.");
    	}else{
	    	profile.netIOEnd() ;
	    	
	        logger.fine("Last packet unit arrived--- The whole packet size is " + packet.length);
	        final CliTransaction cli = new CliTransaction(CliConstants.TYPE_URL);
	        cli.setFunctionName("service_DSR_Request");
	
	        try {
	        	
	        	context = handleMeta();
	        	if(context == null)
	        		return;
	        	            
	        	CliTransaction cliAudio = new CliTransaction(CliConstants.TYPE_MODULE);
	            cli.setFunctionName("handleAudio");
	            
	            byte[] audioData = baosBuf.toByteArray();
	            
	            if (audioData != null && audioData.length > 0) {
	                logger.fine("audio length=" + baosBuf.size());
	                cliAudio.complete();
	            } else {
	            	
	            	String errorMsg = "Invalid audio input, audio is empty" ;
	            	String responseMsg = "No speech to be recognized" ;
	            	handleError(errorMsg,responseMsg) ;
	            	cliAudio.setStatus("No speech to be recognized");
	            	cliAudio.complete();
	            	return;
	            }
	            
				procObj.setContext(context);
				procObj.setAudioData(audioData);
				
				setProfileInfo(procObj);
		
				procObj.setProfile(profile);
	
				int configId = context.recType;
	            ProcessConfiguration processConfig = ProcessConfigurationFactory.getConfiguration(""+configId) ;        
				
	            handlerList = processConfig.getHandlers();
	            profile.setDsrStart(System.currentTimeMillis());
	            recProxy = processConfig.getProxy();
	            recProxy.addResultListener(listener);
	            recProxy.init(procObj);
	            recProxy.addAudioSeg(procObj.getAudioData());
	            recProxy.endAudio();
	            
	            // ok, wait for response from the recognizer
	            responseLock.lock();
	            while (!finished.value)
	            {
	            	logger.info("Waiting for recognizer to finish.");
	            	responseProcessed.await();
	            }
	            responseLock.unlock();
	            
	            logger.info("Processing complete.");
	            
	        } catch (Exception e) {
	        	logger.log(Level.SEVERE, "Unable to process request due to exception",e);
	            String errorMsg = "Unable to process request due to "+e ;
	        	String responseMsg = "Recognition Error" ;
	        	cli.setStatus(e);
	        	handleError(errorMsg,responseMsg) ;
	        } finally {
	        	isFirstPacket = true ;
	        	profile = new ProcessProfile() ;
	        	protocolHandler=null;
	            super.reset();
	            setSessionComplete(true);
	            cli.complete();
	        }
    	}
    }

    private void writeResp(byte[] retBs) {
    	
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("writeResponse");
        try {
            if (channel != null) {
                OutputWriter.flushChannel(channel, ByteBuffer.wrap(Util.getIntBytes(retBs.length)));
                OutputWriter.flushChannel(channel, ByteBuffer.wrap(retBs));
            }
            if (grizzlyResponse != null) {
                try {
                    byte[] lengthBytes = Util.getIntBytes(retBs.length);
                    grizzlyResponse.setContentLength(retBs.length + lengthBytes.length);
                    ByteChunk bc = new ByteChunk();
                    bc.append(lengthBytes, 0, lengthBytes.length);
                    bc.append(retBs, 0, retBs.length);
                    grizzlyResponse.getResponse().doWrite(bc);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "write http resp error", e);
                }
                logger.fine("client on http protocol");
                cli.addData("http", "client on http protocol");
            }
            logger.fine("write back to client size=" + retBs.length);
            cli.addData("respLength", "" + retBs.length);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Recognition Failed", e);
            cli.setStatus(e);
        }
    }    
    
    private RecContext handleMeta(){
    	
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("handleMetaInfo");
        
    	byte[] metaData = metaBs.toByteArray() ;
    	byte[] metaInfo = null;

        // first parse the passcode and protocol version from the header
        if (metaData.length >= 31)
        {
        	// we currently don't do anything with the passcode
        	final int PASSCODE_LENGTH = 31;
        	final int PROTOCOL_BYTE = 0;//32nd byte indicates Protocol
        	final int META_INFO_START = 32; //33rd byte onwards
        	
	        byte[] passcode = new byte[PASSCODE_LENGTH];
	        System.arraycopy(metaData, 0, passcode, 0, passcode.length);
	        
	        String protocolVersion = Byte.toString(metaData[PROTOCOL_BYTE]) + Byte.toString(metaData[PROTOCOL_BYTE+1]);
	        
	        // get the appropirate Protocol Handler implementation based on the version
	        //protocolHandler = (ProtocolHandler) ProtocolHandlerFactory.getProtocolHandler(protocolVersion);
	        
	        //TxNode is the only protocol used by devices that talk to this DSR Cserver
	        //Configurable implementation is causing NPE ~ 500/HR
	        if(protocolVersion.equalsIgnoreCase("00"))
	        	protocolHandler = new TxNodeProtocolHandler();
	      
	        if (protocolHandler != null)
	        {
		        // parse the meta info with this request implementation
		        int metaInfoSize = metaData.length - META_INFO_START ;
		        metaInfo = new byte[metaInfoSize];
		        System.arraycopy(metaData, META_INFO_START, metaInfo, 0, metaInfoSize);
	        }
	        else
	        {
	        	String errorMsg = "Invalid protocol version:"+ protocolVersion ;
	        	handleError(errorMsg,errorMsg) ;
	        	cli.setStatus("Invalid protocol version");
	        	cli.complete();
	        	return null;
	        }
        }
        else
        {
        	String errorMsg = "Unable to parse the meta header: meta data len=" + metaBs.size() ;
        	String responseMsg = "Invalid meta header" ;
        	handleError(errorMsg,responseMsg) ;
        	cli.setStatus("Invalid meta header");
        	cli.complete();
        	return null;
        }
        
        RecContext context = protocolHandler.parseRequest(metaInfo);
        
        if (context == null) {

        	String errorMsg = "Unable to parse the meta info: meta data len=" + metaBs.size() ;
        	String responseMsg = "Invalid meta data" ;
        	handleError(errorMsg,responseMsg) ;
        	cli.setStatus("Invalid meta data");
        	cli.complete();
        	return context;
        }
        cli.addData("Context", context.toString());
        cli.complete();
        return context;
    }
    
    protected void reset() {
        baosBuf.reset();
        metaBs.reset();
    }
    
    public void setSessionComplete(boolean b)
    {
    	sessionComplete = b;
    }
    
    public void setSucc(boolean b)
    {
    	isSucc = b;
    }

    private void handleError(String logErrorMsg,
			 String responseErrorMsg)
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("handleError");
        
		byte[] respBs = {ProcessorReturn.STATUS_FAILED};
		if(protocolHandler!=null)
			respBs = protocolHandler.formatError(DataConstants.FAILED, responseErrorMsg, ProcessorReturn.STATUS_FAILED) ;
		writeResp(respBs);
        
		reset();
		setSessionComplete(true);
		setSucc(false);
				
		logger.warning(logErrorMsg);
		cli.addData("errorInfo", logErrorMsg);
		cli.setStatus(CliConstants.STATUS_FAIL);
		cli.complete();
	}
	
	private void setProfileInfo(ProcessObject procObj){
		
		profile.setCarrier(procObj.context.user.getCarrier());
		profile.setDeviceType(procObj.context.user.getDevice());
		profile.setPtn(procObj.context.user.getMin());
		logger.log(Level.INFO, "CARRIER: "+procObj.context.user.getCarrier()
					+"; DEVICE: "+procObj.context.user.getDevice()+"; USERID: "
					+procObj.context.user.getUserId());
		String cserver_machine_name = null;
        try{
            InetAddress addr = InetAddress.getLocalHost();
            cserver_machine_name = addr.getHostName().toString();
        }
        catch(Exception e)
        {
        	logger.log(Level.INFO, "fail to get the server name", e);          	
        }
        profile.setCserver_machine_name(cserver_machine_name);
	}
	
	class BooleanByReference
	{
		boolean value;
		
		public BooleanByReference(boolean value)
		{
			this.value = value;
		}
	}
}
