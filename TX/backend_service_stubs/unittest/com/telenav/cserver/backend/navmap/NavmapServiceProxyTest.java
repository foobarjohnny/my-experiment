/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navmap;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class NavmapServiceProxyTest extends TestCase
{
	
	private TnContext tc;
	
	private NavmapServiceProxy proxy;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		proxy = NavmapServiceProxy.getInstance();

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
	 * Test method for {@link com.telenav.cserver.backend.navmap.NavmapServiceProxy#sendRequest(com.telenav.j2me.datatypes.TxNode, com.telenav.kernel.util.datatypes.TnContext)}.
	 * @throws ThrottlingException 
	 */
	public void testSendRequest() throws ThrottlingException
	{
		TxNode request = new TxNode();
		
		request.addMsg(tc.toContextString());
		
		TxNode child = new TxNode();
		
		child.addValue(DataConstants.NAVMAP_TYPE_ALL_IN_ONE_VECTOR); // type
		child.addValue(715833575584L); // copy from regression test titleId
		child.addValue(1); // zoom level;
		
		// set to parent
		request.addChild(child);
		
		// start request
		TxNode response = proxy.sendRequest(request, tc);

		assertNotNull(response);
		
		assertEquals(1, response.childrenSize());
		
		TxNode title = response.childAt(0);
		assertNotNull(title);
		assertEquals(DataConstants.NAVMAP_TYPE_ALL_IN_ONE_VECTOR, title.valueAt(0));
		assertEquals(715833575584L, title.valueAt(1));
		assertEquals(1, title.valueAt(2));
		byte[] binary = title.getBinData();
		assertTrue(binary != null && binary.length > 0);
	}

}
