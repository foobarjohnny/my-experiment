/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.image;

import junit.framework.TestCase;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class TestImageServiceProxy extends TestCase
{

	private ImageServiceProxy proxy;
	private TnContext tc;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		proxy = ImageServiceProxy.getInstance();
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
	 * Test method for {@link com.telenav.cserver.backend.image.ImageServiceProxy#getMapTile(com.telenav.cserver.backend.image.MapTileRequest, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws ThrottlingException 
	 */
	public void testGetMapTile() throws ThrottlingException
	{
		MapTileRequest request = new MapTileRequest();
		request.setMapType("NA_TA");
		long x = (3096401877689166L >> 24) & 0xFFFFFF;;
		long y = 3096401877689166L & 0xFFFFFF;
		request.setX(x);
		request.setY(y);
		request.setZoom(1);
		request.setPixel(128);
		byte[] data = proxy.getMapTile(request , tc);
		assertNotNull(data);
		
		assertTrue(data.length > 0);
	}

}
