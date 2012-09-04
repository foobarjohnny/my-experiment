/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.traffic;

import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.datatypes.traffic.TrafficIncidentReport;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.traffic.tims.v10.CreateRequestDTO;
import com.telenav.services.traffic.tims.v10.CreateResponseDTO;
import com.telenav.services.traffic.tims.v10.TIMServiceStub;
import com.telenav.ws.datatypes.services.ResponseStatus;

/**
 * TrafficServiceProxy
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class TrafficServiceProxy
{

	private static Logger logger = Logger.getLogger(TrafficServiceProxy.class);
	
	private static TrafficServiceProxy instance = new TrafficServiceProxy();
	
	public final static String SERVICE_TRAFFIC = "SERVICE_TRAFFIC";
	
	public final static String TRAFFIC_WEB_SERVICE = "TRAFFIC_SERVICE";
	
	private TrafficServiceProxy() 
	{
		// can't instant out the class
	}
	
	public static TrafficServiceProxy getInstance()
	{
		return instance;
	}
	
	public TrafficCreateResponse createIncident(List<TrafficIncidentReport> reports, TnContext tc) throws ThrottlingException
	{
		CliTransaction cli = CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("Create_Incident");
		boolean startAPICall = false;
		TIMServiceStub stub = null;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_TRAFFIC, tc);
			if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			CreateRequestDTO request = createRequest();
			for(TrafficIncidentReport report : reports)
			{
				request.addReport(DataConverter.convertReport(report));
			}
			CreateResponseDTO response = null;
			try
			{
				stub = createStub();
				response = stub.createIncident(request);
				if (response != null && response.getResponseStatus() != null) 
				{
					ResponseStatus status = response.getResponseStatus();
					cli.addData("Create_Incident_response", "statusCode=" + status.getStatusCode() + "&message=" + status.getStatusMessage());
				}
			}
			catch(Exception e)
			{
				cli.setStatus(e);
			    logger.fatal("TrafficOperator::createIncident",e);
			}
			
			return TrafficCreateResponse.fromWsResponse(response);
			
		}
		finally
		{
			if (cli != null)
			{
				cli.complete();
			}
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_TRAFFIC, tc);
			}
			if(stub != null)
			{
				WebServiceUtils.cleanupStub(stub);
			}
		}
	}
	
	/**
	 * @return
	 * @throws AxisFault 
	 */
	private TIMServiceStub createStub() throws AxisFault
	{
		WebServiceConfigInterface config = WebServiceConfiguration.getInstance().getServiceConfig(TRAFFIC_WEB_SERVICE);
		return new TIMServiceStub(config.getServiceUrl());
	}

	private CreateRequestDTO createRequest()
	{
		CreateRequestDTO request = new CreateRequestDTO();
		request.setClientName("CServer");
		request.setClientVersion("6.0");
		request.setTransactionId("UNKNOWN");
		return request;
	}

}
