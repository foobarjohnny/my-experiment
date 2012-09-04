/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.commutealert;

import com.telenav.cserver.backend.datatypes.commutealert.CommuteAlertDetails;
import com.telenav.cserver.backend.datatypes.commutealert.TrafficSummaryReport;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class TestCommuteAlertServiceProxy extends TestCase
{

	private CommuteAlertServiceProxy proxy;
	
	private TnContext tc;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		this.proxy = CommuteAlertServiceProxy.getInstance();
		
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
	 * Test method for {@link com.telenav.cserver.backend.commutealert.CommuteAlertServiceProxy#getAlert(long, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws CommuteAlertException 
	 */
	public void testGetAlert() throws ThrottlingException
	{
		int alertId = 3521;
		CommuteAlertDetails alerts = proxy.getAlert(alertId, tc);
		assertNotNull(alerts);
		
		assertEquals(alertId, alerts.getAlertId());
		
	}

	/**
	 * Test method for {@link com.telenav.cserver.backend.commutealert.CommuteAlertServiceProxy#getTrafficSummary(long, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws CommuteAlertException 
	 */
	public void testGetTrafficSummary() throws ThrottlingException
	{
		int alertId = 3521;
		CommuteAlertsServiceResponse response = proxy.getTrafficSummary(alertId, tc);
		
		assertNotNull(response);
		
		TrafficSummaryReport report = response.getTrafficSummaryReport();
		assertNotNull(report);
		
		assertTrue(report.getSegment() != null);
	}

}
