/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.map.LatLonPoint;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class TestMapServiceProxy
{

	private TnContext tc;
	
	private MapServiceProxy proxy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		proxy = MapServiceProxy.getInstance();
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
	 * Test method for {@link com.telenav.cserver.backend.map.MapServiceProxy#queryMapImage(com.telenav.cserver.backend.map.QueryMapImageRequest, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws ThrottlingException 
	 */
	@Test
	public void testQueryMapImage() throws ThrottlingException
	{
		LatLonPoint minCorner = new LatLonPoint();
		minCorner.setLat(37.29528);
		minCorner.setLon(-122.44516);
		LatLonPoint maxCorner = new LatLonPoint();
		maxCorner.setLat(37.69225);
		maxCorner.setLon(-121.94483);
		QueryMapImageRequest imageSetting = new QueryMapImageRequest(minCorner, maxCorner, 200, 200, new Vector(), null, QueryMapImageRequest.IMGSETTINGS_PHONE_VIEW, QueryMapImageRequest.FORMAT_PNG);
		QueryMapImageResponse status = proxy.queryMapImage(imageSetting , tc);
		assertNotNull(status);
		assertNotNull(status.getImageData());
		assertTrue(status.getImageData().length > 0);
	}

	/**
	 * Test method for {@link com.telenav.cserver.backend.map.MapServiceProxy#findNearbyCross(com.telenav.cserver.backend.map.FindNearbyCrossRequest, com.telenav.kernel.util.datatypes.TnContext)}.
	 */
	@Test
	public void testFindNearbyCross()
	{
		FindNearbyCrossRequest request = new FindNearbyCrossRequest();
		
		LatLonPoint latLonPoint = new LatLonPoint();
		latLonPoint.setLat(37.69225);
		latLonPoint.setLon(-121.94483);
		request.setLatLonPoint(latLonPoint);
		
		Vector<String> v = new Vector<String>();
		request.setEdgeIDs(v);
		
		NearbyCrossStatusResponse response = null;
		try {
			response = proxy.findNearbyCross(request, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(response);
	}

	/**
	 * Test method for {@link com.telenav.cserver.backend.map.MapServiceProxy#queryRGC(com.telenav.cserver.backend.datatypes.map.LatLonPoint, double, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws ThrottlingException 
	 */
	@Test
	public void testQueryRGC() throws ThrottlingException
	{
		double radius = 0.2;
		LatLonPoint latLonPoint = new LatLonPoint();
		latLonPoint.setLat(37.35548);
		latLonPoint.setLon(-121.95426);
		RGCStatusResponse response = proxy.queryRGC(latLonPoint, radius, tc);
		assertNotNull(response);
		assertTrue(response.isSuccess());
		assertNotNull(response.getAddresses());
		assertTrue(response.getAddresses().size() > 0);
		
	}

}
