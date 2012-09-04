/**
 * 
 */
package com.telenav.cserver.dsr.handler;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.audio.log.LoggingService;
import com.telenav.audio.log.dsr.AudioLogFactory;
import com.telenav.audio.orm.Persistable;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.framework.ProcessProfile;
import com.telenav.cserver.framework.reporting.ReportType;
import com.telenav.cserver.framework.reporting.ReportingRequest;
import com.telenav.cserver.framework.reporting.ReportingUtil;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author joses
 *
 * 
 */
public class SaveLogHandler implements IResultsHandler {

	private static Logger logger = Logger.getLogger(SaveLogHandler.class.getName());
	public ProcessObject process(ProcessObject procObj) {

		List<ProcessedResult> results = procObj.getProcessedResults() ;
	       
	       String literal = "" ;
	       if (results.size() > 0)
	    	   literal = results.get(0).getLiteral();
	    	   	   
	        RecContext rc = procObj.getContext() ;
	        ProcessProfile profile = procObj.getProfile();
	        TnContext context = rc.tnContext ;
	        
	        profile.setPtn(rc.user.getMin());
	        profile.setRecType(rc.recType);
	        profile.setLat(rc.location.lat);
	        profile.setLon(rc.location.lon);
	        profile.setAudioFormat(rc.audioFormat);
	        profile.setLiteral(literal);
        	        
	        Vector<String> vector = new Vector<String>();
	        int i = 0;
	        
	        for(ProcessedResult procResult : procObj.getProcessedResults()){
	        	vector.add(procResult.toString());
	        	logger.log(Level.FINE, procResult.toString());
	        }
	        String cServerResult = vector.toString();
	        if(cServerResult.length() > 255)
	        	profile.setCserverresult(cServerResult.substring(0, 255));
	        else
	        	profile.setCserverresult(vector.toString());
	        profile.totalProcessEnd();
	        procObj.setProfile(profile);
	        
	        
	        
	        Persistable log = AudioLogFactory.getCserverLog(procObj.getTransactionId(), 
	        													profile.getPtn(), 
	        													profile.getRecType(),	
	        													profile.getLat(), 
	        													profile.getLon(), 
	        													profile.getAudioFormat(), 
	        													procObj.getAudioData(), 
	        													profile.getCserverresult(),
	        													(int)profile.getNetIOTime(), 
	        													(int)profile.getRecProxyTime(), 
	        													(int)profile.getExternalProcessTime(), 
	        													(int)profile.getTotalProcessTime(), 
	        													profile.getRequestTimestamp(), 
	        													profile.getDsrStart(),
	        													profile.getCarrier(), 
	        													profile.getDeviceType(), 
	        													profile.getLiteral(), 
	        													profile.getCserver_machine_name());
	        										
	        
	       LoggingService.instance.saveLog(log) ;
	       
		
		ReportingRequest misLog = getMisLog(procObj);
		saveMISLog(procObj, misLog);
		
        
        logger.log(Level.FINE, "Audio Log : transactionId = " + procObj.transactionId);
  
		return procObj;
	}

    private static ReportingRequest getMisLog(ProcessObject procObj) {
    	RecContext context = procObj.getContext();
    	ProcessProfile profile = procObj.getProfile();
        ReportingRequest misLog = new ReportingRequest(ReportType.SERVER_MIS_LOG_REPORT, context.user, context.tnContext);
        misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME, "dsr_service");
        misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, "DsrClient");
        //if(procObj.isPilot())
        	misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID, "103");   //Watson ID is 103
        //else
        	//misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID, "102"); //Nuance ID was 102

        misLog.addServerMisLogField(ServerMISReportor.CUSTOM00, "" + context.recType);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM01, ServerMISReportor.COMPLETE_SUCCEED);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM02, "0");           

        //Additional logging - Begin
        misLog.addServerMisLogField(ServerMISReportor.PTN, profile.getPtn());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM09, Float.toString(profile.getLat()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM10, Float.toString(profile.getLon()));
        misLog.addServerMisLogField(ServerMISReportor.EXEC_TIMESPAN, Long.toString(profile.getTotalProcessTime()));
        misLog.addServerMisLogField(ServerMISReportor.TIMESTAMP, Long.toString(profile.getCserverRequestTime()));
        misLog.addServerMisLogField(ServerMISReportor.HOST_NAME, profile.getCserver_machine_name());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM13, profile.getDeviceType());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM17, profile.getCarrier());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM05, Long.toString(profile.getRecProxyTime()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM06, Long.toString(profile.getNetIOTime()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM07, Integer.toString(profile.getAudioFormat()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM08, profile.getCserverresult());
        //Additional logging - End
        
        return misLog;
    }
    
    private static void saveMISLog(ProcessObject procObj, ReportingRequest misLog) {
    	
        List<ProcessedResult> results = procObj.getProcessedResults();
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM03, "" + results.size());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM04, "" + (results.isEmpty() ? "" : results.get(0).getLiteral()));
        misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG, ServerMISReportor.COMPLETE_SUCCEED);
        logger.fine("-----Mis Log-----");
        ReportingUtil.report(misLog);
    }    
}
