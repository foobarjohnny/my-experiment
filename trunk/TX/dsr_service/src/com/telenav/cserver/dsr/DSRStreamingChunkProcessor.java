package com.telenav.cserver.dsr;

import java.net.InetAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.xmlbeans.impl.common.IOUtil;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.DSRExecutorResponse;
import com.telenav.cserver.dsr.framework.ProcessConfiguration;
import com.telenav.cserver.dsr.framework.ProcessConfigurationFactory;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.framework.ProcessProfile;
import com.telenav.cserver.dsr.framework.RecognizerProxy;
import com.telenav.cserver.dsr.framework.RecognizerResultListener;
import com.telenav.cserver.dsr.handler.IResultsHandler;
import com.telenav.cserver.dsr.protocol.ProtocolConstants;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.framework.Constants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.service.chunkhandler.AbstractChunkProcssor;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

public class DSRStreamingChunkProcessor extends AbstractChunkProcssor
									implements RecognizerResultListener,
												ProtocolConstants
{

	private static Logger logger = Logger
			.getLogger(DSRStreamingChunkProcessor.class.getName());

	private ProcessObject procObj = new ProcessObject();
	private ProcessConfiguration processConfig;
	private RecognizerProxy proxy;
	private ProcessProfile profile = procObj.getProfile();
	private String executorType;

	@Override
	public void doProcess(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext execContext) throws Exception
	{ 
		executorType = req.getExecutorType();
		
		if (DSR_SIGNAL_TYPE.equals(executorType))
		{
			String signal = (String) req.getAttributes().get(SIGNAL);
			
			if (END_AUDIO_SIGNAL.equals(signal))
			{
				procObj.getProfile().netIOEnd();
				// tell the recognizer that it has all the audio
				proxy.endAudio();
				
				//Stop reading chunk data
				callback.doEnd();
			}
			else if (CANCEL_SIGNAL.equals(signal))
			{
				// The user canceled, so tell the recognizer to stop processing
				proxy.release();
			}
			else if (INIT_RECOGNIZER_SIGNAL.equals(signal))
			{
				
				//HARD CODE TNCONTEXT - REMOVE ONCE DONGHENG FIXES DSM RULES
				hardCodeTnContext(execContext, req.getUserProfile());
				
				procObj.profile.netIOBegin();
				procObj.profile.totalProcessBegin();
				RecContext rc = (RecContext) req.getAttributes()
										.get(ProtocolConstants.REC_CONTEXT);
				
				rc.user = req.getUserProfile();
				rc.ttsFormat = rc.user.getAudioFormat();
				rc.tnContext = execContext.getTnContext();
				
				List<GpsData> gpsData = req.getGpsData();
				
				if (gpsData != null && gpsData.size() > 0)
				{
					// just use the first fix
					GpsData data = gpsData.get(0);
					rc.location = new Stop();
					rc.location.lat = data.lat;
					rc.location.lon = data.lon;
				}
				else
				{
					logger.log(Level.WARNING, "Unablet o get GPS data from request.");
					rc.location = new Stop();
					rc.location.lat = 0;
					rc.location.lon = 0;
				}
				
				// process the meta information
				processMetaInfo(rc);
				
				// Initialize the recognizer
				proxy.init(procObj);
			}
			else
				logger.log(Level.WARNING, "Unrecognized dsr signal: "
						+ signal);
		}
		else if (DSR_AUDIO_TYPE.equals(executorType))
		{
			// write audio packet to the recognizer
			byte[] audioData = (byte[]) req.getAttributes()
											.get(ProtocolConstants.AUDIO);
			proxy.addAudioSeg(audioData);
		}
		else
			logger.log(Level.WARNING, "Did not recognize dsr packet type: "
					+ executorType);

	}
	
	private void hardCodeTnContext(ExecutorContext execContext, UserProfile userProfile){
		String loginName = userProfile.getMin();
        String carrier = userProfile.getCarrier();
        String device = userProfile.getDevice();
        String product = userProfile.getPlatform();
        String version = userProfile.getVersion();
        String userID = userProfile.getUserId();    
        String applicationName = userProfile.getProduct();
        //String mapDpi = userProfile.getMapDpi();
        String locale = userProfile.getLocale();
        //String programCode = userProfile.getProgramCode();
      
        TnContext tc = new TnContext();
        tc.addProperty(TnContext.PROP_LOGIN_NAME , loginName);
        tc.addProperty(TnContext.PROP_CARRIER , carrier);
        tc.addProperty(TnContext.PROP_DEVICE , device);
        tc.addProperty(TnContext.PROP_PRODUCT , product);
        tc.addProperty(TnContext.PROP_VERSION , version);
//      tc.addProperty("user_family", Long.toString(ResourceLoader.getInstance().getUserFamily(userProfile)));
        tc.addProperty("application", applicationName);//"application" should defined in TnContext
        tc.addProperty("c-server class", Constants.CSERVER_CLASS);
        tc.addProperty("c-server url", execContext.getServerUrl());
        //tc.addProperty("map_dpi", mapDpi);
        tc.addProperty("locale", locale);
        //tc.addProperty("program_code", programCode);
        
        tc.addProperty(TnContext.PROP_REQUESTOR, TnContext.TT_REQUESTOR_TNCLIENT);
	}
	
	private void handleError(int statusCode, String responseErrorMsg)
	{
		logger.log(Level.SEVERE, responseErrorMsg);
		procObj.transactionId = procObj.transactionId;
		procObj.statusCode = statusCode;
		procObj.errorMessage = responseErrorMsg;
		
		if (procObj.processedResults != null)
			procObj.processedResults.clear();
	}	

	private void processMetaInfo(RecContext context)
	{
		profile.netIOBegin();
		profile.totalProcessBegin();

		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("handleMeta");

		// handle meta information
		if (context == null)
		{
			String responseMsg = "Unable to parse request meta data.";
			handleError(ResourceConst.SERVER_ERROR, responseMsg);
			cli.setStatus("responseMsg");
			cli.complete();
		}

		logger.log(Level.INFO, "Meta handled successfully");
		// Set context obtained form meta
		procObj.setContext(context);

		// Set profiling data
		setProfileInfo();

		// Obtain processConfig for corresponding recType
		int configId = procObj.getContext().recType;
		processConfig = ProcessConfigurationFactory
				.getConfiguration("" + configId);
		logger.log(Level.INFO, "Obtained process config instance for config id : "
				+ configId);

		// Obtain proxy object
		proxy = processConfig.getProxy();
		proxy.addResultListener(this);
		
		logger.log(Level.INFO, "Obtained proxy instance from process config");

		//proxy.init(procObj);
		cli.complete();

	}

	private void setProfileInfo()
	{

		profile.setCarrier(procObj.getContext().user.getCarrier());
		profile.setDeviceType(procObj.getContext().user.getDevice());
		profile.setPtn(procObj.getContext().user.getUserId());
		logger.log(Level.INFO, "CARRIER: " + procObj.getContext().user.getCarrier()
				+ "; DEVICE: " + procObj.getContext().user.getDevice()
				+ "; USERID: " + procObj.getContext().user.getUserId());
		String cserver_machine_name = null;
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			cserver_machine_name = addr.getHostName().toString();
		}
		catch (Exception e)
		{
			logger.log(Level.WARNING, "fail to get the server name",e);
		}
		profile.setCserver_machine_name(cserver_machine_name);
		procObj.setProfile(profile);
	}
	
	@Override
	public void handleResult(ProcessObject procObj)
	{
		try
		{
			logger.log(Level.INFO, "DSR Status Code: "+procObj.getProfile().getDsrStatusCode());
			if(procObj.getProfile().getDsrStatusCode()==ResourceConst.SERVER_ERROR){
				//DSR Recognition Failure
				logger.log(Level.WARNING, "No reponse bytes return. Failed to recognize speech.");
				String responseMsg = "Failed to recognize speech.[-1]";
				handleError(ResourceConst.SERVER_ERROR, responseMsg);
			}
			else
			{
				//pipeline flow
				for(IResultsHandler handler : processConfig.getHandlers())
					procObj = handler.process(procObj);
				
				procObj.statusCode = ResourceConst.SERVER_SUCCESS;
			}
		}
		catch (Exception e)
		{
			logger.log(Level.WARNING, "Unable to process request due to ",e);
        	String responseMsg = "DSR C-server Error" ;
        	handleError(ResourceConst.SERVER_ERROR, responseMsg) ;
		}
		
		DSRExecutorResponse er = new DSRExecutorResponse();
		er.setStatus(procObj.statusCode);
		er.setErrorMessage(procObj.errorMessage);
		er.setProcessObject(procObj);
		er.setExecutorType(executorType);
		procObj.profile.totalProcessEnd();
		// call back to the framework with the result
		try
		{
			callback.doCallback(null, er);
		}
		catch(Exception e)
		{
			logger.log(Level.WARNING, "Exception calling back to server",e);
		}
		
		//callback.doEnd();
	}
}
