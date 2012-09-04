/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.traffic;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.datatypes.traffic.TrafficIncident;
import com.telenav.cserver.backend.datatypes.traffic.TrafficIncidentReport;
import com.telenav.cserver.backend.datatypes.traffic.TrafficIncidentSource;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.services.ResponseCodeEnum;

import junit.framework.TestCase;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class TestTrafficServiceProxy extends TestCase
{

	private TnContext tc;
	
	private TrafficServiceProxy proxy;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		proxy = TrafficServiceProxy.getInstance();
		
		// init the tncontext
		tc = new TnContext();
		tc.addProperty("GENERATE_LANE_INFO", "true");
		tc.addProperty("device", "9630");
		tc.addProperty("c-server url", "localhost:8080");
		tc.addProperty("c-server class", "CServer6x_HTTP");
		tc.addProperty("requestor", "tnclient");
		tc.addProperty("dataset", "Navteq");
		tc.addProperty("poidataset", "TA");
		tc.addProperty("carrier", "SprintPCS");
		tc.addProperty("version", "6.0.01");
		tc.addProperty("application", "SN_prem");
		tc.addProperty("login", "5198887465");
		tc.addProperty("userid", "3698214");
		tc.addProperty("product", "RIM");
	}

	/**
	 * Test method for {@link com.telenav.cserver.backend.traffic.TrafficServiceProxy#createIncident(java.util.List, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws ThrottlingException 
	 */
	public void testCreateIncident() throws ThrottlingException
	{
		List<TrafficIncidentReport> reports = new ArrayList<TrafficIncidentReport>();
		
		TrafficIncidentReport report = new TrafficIncidentReport();
		
		//TODO init report
		TrafficIncidentSource source = new TrafficIncidentSource();
		source.setReporterId("3698214");
		source.setSourceReportId("");
		source.setType(TrafficIncidentSource.TYPE_TELENAV_USER);
		source.setCarrier("ATT");
		source.setDevice("9000");
		source.setVersion("6.0.01");
		report.setSource(source);
		
		TrafficIncident incident = new TrafficIncident();
		incident.setLatitude(3735548);
		incident.setLongitude(-12195426);
		incident.setSeverity(TrafficIncident.SEVERITY_TYPE_LOW_IMPACT);
		incident.setMarketID("UNKNOWN");
		incident.setType(TrafficIncident.TYPE_TRAFFIC_CAMERA);
		incident.setDescription("Traffic Camera");
		
		incident.setStreetName("WARBURTON AVE");
		incident.setCrossStreet("FILLMORE ST");
		
		report.setIncident(incident);
		
		reports.add(report);
		
		TrafficCreateResponse response = proxy.createIncident(reports, tc);
		
		assertNotNull(response);
		
		if (!ResponseCodeEnum._OK.equals(response.getStatusCode())) {
			fail(response.getStatusMessage());
		}
	}

}
