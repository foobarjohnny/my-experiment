/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.traffic;

import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.traffic.tims.v10.CreateRequestDTO;
import com.telenav.services.traffic.tims.v10.CreateResponseDTO;
import com.telenav.services.traffic.tims.v10.IncidentReport;
import com.telenav.services.traffic.tims.v10.TIMServiceStub;

/**
 * TrafficProxy.java
 * @author njiang
 * @version 1.0 2012-7-25
 */
@BackendProxy
@ThrottlingConf("TrafficProxy")
public class TrafficProxy extends HttpClientProxy {
	
	public final static String SERVICE_TRAFFIC = "SERVICE_TRAFFIC";
	
	private static ConfigurationContext configurationContext = WebServiceUtils.createConfigurationContext("TIM_SERVICE");
	
	private static Logger logger = Logger.getLogger(TrafficProxy.class);
	
	private CreateRequestDTO createRequest(TnContext tc)
	{
		CreateRequestDTO request = new CreateRequestDTO();
		request.setClientName("CServer");
		request.setClientVersion("6.0");
		request.setTransactionId("UNKNOWN");
		request.setContextString(tc.toContextString());
		return request;
	}
	
	public CreateResponseDTO createIncident(List<IncidentReport> reports, TnContext tc) throws ThrottlingException
	{
		TIMServiceStub stub = null;
		CreateRequestDTO request = createRequest(tc);
		CreateResponseDTO response = null;
		for(IncidentReport report : reports)
		{
			request.addReport(report);
		}
		
		WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig("TIM_SERVICE");
		try
		{
			stub = new TIMServiceStub(configurationContext, ws.getServiceUrl());
			response = stub.createIncident(request);
		}
		catch(Exception e)
		{
		    logger.fatal("TrafficOperator::createIncident",e);
		}
		
		if(stub != null){
			WebServiceUtils.cleanupStub(stub);
		}
		
		return response;
	}

	@Override
	public String getProxyConfType() {
		return "TIM_SERVICE";
	}
	
	

}
