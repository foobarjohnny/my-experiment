/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.commutealert;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.alerts.AlertDetails;
import com.telenav.ws.datatypes.alerts.AlertsServiceRequestDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceResponseDTO;
import com.telenav.ws.datatypes.alerts.AlertsServiceStatus;
import com.telenav.ws.services.alerts.AlertsServiceStub;

/**
 * CommuteAlertProxy.java
 * @author njiang
 * @version 1.0 2012-7-25
 */
@BackendProxy
@ThrottlingConf("CommuteAlertProxy")
public class CommuteAlertProxy extends HttpClientProxy {
	
	private static ConfigurationContext configurationContext = WebServiceUtils.createConfigurationContext("ALERT_SERVICE");
	
	private static WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig("ALERT_SERVICE");
	
	 /**
	 * get alert
	 * 
	 * @param alertID
	 * @return
	 * @throws Exception
	 */
	public AlertDetails getAlert(long alertID, TnContext tc) throws CommuteAlertException 
	{
        AlertsServiceStub stub = null;
        try
        {
        	AlertsServiceRequestDTO request = createRequest();
        	request.setAlertId(alertID);
        	
        	try
	        {
	            stub = new AlertsServiceStub(configurationContext, ws.getServiceUrl());
	        } catch (AxisFault e)
	        {
	        	throw new CommuteAlertException(CommuteAlertConstants.STATUS_CANNOT_CONNECTTO_WS, e);
	        }
	        
	        AlertsServiceResponseDTO response;
	        try
	        {
	            response = stub.getAlert(request);
	        } 
	        catch(Exception e)
	        {
	        	throw new CommuteAlertException(CommuteAlertConstants.STATUS_WS_EXCEPTION, e);
	        }
	        if(AlertsServiceStatus._OK.equalsIgnoreCase(response.getResponseStatus().getStatusCode()) )
	        {
	        	if(response.getAlertList()!= null && response.getAlertList().length > 0 )
	        	{
		        	AlertDetails details = response.getAlertList()[0];
		        	return details;
	        	}
	        }
	        else
	        {
	        	throw new CommuteAlertException(CommuteAlertConstants.STATUS_WS_NULL_ALERT, response.getResponseStatus().getStatusMessage());
	        }
	       
        }
        finally
		{
			if(stub != null)
			{
				WebServiceUtils.cleanupStub(stub);
			}
		}
        return null;
	}
	
    /**
     * get TrafficSummary from WS by given alert ID
     * 
     * @param alertID
     * @return
     * @throws Exception
     */
	public AlertsServiceResponseDTO getTrafficSummary(long alertID,
			TnContext tnContext) throws CommuteAlertException
	{
        AlertsServiceStub stub = null;
        try
        {
        	AlertsServiceRequestDTO request = createRequest();
	        request.setAlertId(alertID);
	        try
	        {
	        	stub = new AlertsServiceStub(configurationContext, ws.getServiceUrl());
	        }
	        catch (AxisFault e)
	        {
	        	throw new CommuteAlertException(CommuteAlertConstants.STATUS_CANNOT_CONNECTTO_WS, e);
	        }
	        
	        AlertsServiceResponseDTO response;
	        try
	        {
	            response = stub.getCommuteTrafficReport(request);
	        } catch (Exception e)
	        {
	        	throw new CommuteAlertException(CommuteAlertConstants.STATUS_WS_EXCEPTION, e);
	        } 
	        return response;
        }
        finally
		{
			if(stub != null)
			{
				WebServiceUtils.cleanupStub(stub);
			}
		}
	}
    /**
     * create an AlertsServiceRequestDTO
     * 
     * @return
     */
    private static AlertsServiceRequestDTO createRequest()
    {
        AlertsServiceRequestDTO request = new AlertsServiceRequestDTO();
        request.setClientName("c-server");
        request.setClientVersion("1.0");
        request.setTransactionId("unknown");
        
        return request;
    }

	@Override
	public String getProxyConfType() {
		return "ALERT_SERVICE";
	}

}
