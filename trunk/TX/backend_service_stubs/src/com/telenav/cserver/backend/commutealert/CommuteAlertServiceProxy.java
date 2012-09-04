/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.commutealert;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.datatypes.commutealert.CommuteAlertDetails;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.alerts.AlertDetails;
import com.telenav.ws.datatypes.alerts.AlertsServiceRequestDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceResponseDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceStatus;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.services.alerts.AlertsFault;
import com.telenav.ws.services.alerts.AlertsServiceStub;

/**
 * copy from nav_map_service's CommuteAlertOperator.java
 * @author mmwang
 * @author bhu@telenav.cn
 * @version 1.0 2010-07-15
 */
public class CommuteAlertServiceProxy
{
	public final static String SERVICE_ALERT = "SERVICE_ALERT";
	
	public final static String WS_COMMUTE_ALERT = "COMMUTE_ALERT";
	
	private static Logger logger = Logger.getLogger(CommuteAlertServiceProxy.class);
	
	private static CommuteAlertServiceProxy instance = new CommuteAlertServiceProxy();
	
	private CommuteAlertServiceProxy() 
	{
		// can't instant in constructor way;
	}
	
	/**
	 * the only way to get a CommuteAlertServiceProxy instance
	 * @return
	 */
	public static CommuteAlertServiceProxy getInstance() 
	{
		return instance;
	}
	
	 /**
     * get alert
     * 
     * @param alertID
     * @return
     * @throws Exception
     */
	public CommuteAlertDetails getAlert(long alertID, TnContext tc) throws ThrottlingException 
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("Get_Alert");
		boolean startAPICall = false;
        AlertsServiceStub stub = null;
        try
        {
        	startAPICall = ThrottlingManager.startAPICall(SERVICE_ALERT, tc);
        	if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
        		logger.error("can't call this API anymore");
				throw new ThrottlingException();
			}
        	AlertsServiceRequestDTO request = createRequest();
        	request.setAlertId(alertID);
        	
        	try
			{
				stub = createStub();
				
				cli.addData("Get_Alert_request_id", String.valueOf(alertID));
				AlertsServiceResponseDTO response = stub.getAlert(request);
				if (response != null && response.getResponseStatus() != null)
				{
					ResponseStatus status = response.getResponseStatus();
					cli.addData("Get_Alert_response_status", "statusCode=" + status.getStatusCode() + "&message=" + status.getStatusMessage());
					if (AlertsServiceStatus._OK.equalsIgnoreCase(status.getStatusCode()))
					{
						if (response.getAlertList() != null && response.getAlertList().length > 0)
						{
							AlertDetails details = response.getAlertList()[0];
							return DataConverter.convertAlertDetailsToCommuteAlertDetails(details, tc);
						}
					}
				}
			} catch (Exception e)
			{
				cli.setStatus(e);
				logger.fatal("exception:", e);
			}
        }
        finally
		{
        	cli.complete();
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_ALERT, tc);
			}
			
			WebServiceUtils.cleanupStub(stub);
		}
        
        // return null if don't get any alert
        return null;
	}

    /**
     * get TrafficSummary from WS by given alert ID
     * 
     * @param alertID
     * @return
     * @throws Exception
     */
	public CommuteAlertsServiceResponse getTrafficSummary(long alertID,
			TnContext tnContext) throws ThrottlingException
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("Get_Traffic_Summary");
		boolean startAPICall = false;
        AlertsServiceStub stub = null;
        try
        {
        	startAPICall = ThrottlingManager.startAPICall(SERVICE_ALERT, tnContext);
        	if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
        		logger.error("can't call this API anymore");
				throw new ThrottlingException();
			}
        	AlertsServiceRequestDTO request = createRequest();
	        request.setAlertId(alertID);
	        
        	try
			{
				stub = createStub();
				
				cli.addData("Get_Traffic_Summary_request_id", String.valueOf(alertID));
				AlertsServiceResponseDTO response = stub.getCommuteTrafficReport(request);
				if (response != null) 
				{
					ResponseStatus responseStatus = response.getResponseStatus();
					cli.addData("Get_Traffic_Summary_response_status", "statusCode=" + responseStatus.getStatusCode() + "&message=" + responseStatus.getStatusMessage());
					return new CommuteAlertsServiceResponse(response);
				}
			} catch (Exception e)
			{
				cli.setStatus(e);
				logger.fatal("exception:", e);
			} 
        } 
        finally
		{
        	cli.complete();
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_ALERT, tnContext);
			}
			
			WebServiceUtils.cleanupStub(stub);
		}
        
        // return null if some exception occur or nothing exist
        return null;
	}
	
	
    /**
     * create an AlertsServiceRequestDTO
     * 
     * @return
     */
    private AlertsServiceRequestDTO createRequest()
    {
        AlertsServiceRequestDTO request = new AlertsServiceRequestDTO();
        request.setClientName("c-server");
        request.setClientVersion("1.0");
        request.setTransactionId("unknown");
        
        return request;
    }
    
    /**
	 * 
	 * @return
     * @throws AxisFault 
     * @throws CommuteAlertException 
	 */
	private AlertsServiceStub createStub() throws AxisFault
	{
		WebServiceConfigInterface config = WebServiceConfiguration.getInstance().getServiceConfig(WS_COMMUTE_ALERT);
		return new AlertsServiceStub(config.getServiceUrl());
	}
	
}
