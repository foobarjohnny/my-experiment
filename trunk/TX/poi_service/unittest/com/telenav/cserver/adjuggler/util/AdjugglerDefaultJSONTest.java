package com.telenav.cserver.adjuggler.util;

import junit.framework.Assert;

import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class AdjugglerDefaultJSONTest {
	
	DataHandler handler = null;
	MockHttpServletRequest request = null;
	
	@Before
	public void setUp() throws Exception {
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request,"mock", 110);
		handler = (DataHandler)request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
	}

	@Test
	public void testGetDefaultJSON() {
		Object o = AdjugglerDefaultJSON.getDefaultJSON("poi", "0", handler);
		Assert.assertEquals(String.class.getName(), o.getClass().getName());
	}

	@Test
	public void testGetDefaultJSONForPrem() {
		Object o = AdjugglerDefaultJSON.getDefaultJSONForPrem("poi");
		Assert.assertEquals(JSONObject.class.getName(), o.getClass().getName());
	}

	@Test
	public void testGetDefaultJSONForNonPrem() {
		Object o = AdjugglerDefaultJSON.getDefaultJSONForNonPrem("poi", handler);
		Assert.assertEquals(JSONObject.class.getName(), o.getClass().getName());
	}

	@Test
	public void testGetDefaultJSONForPOIBannerAd() {
		Object o = AdjugglerDefaultJSON.getDefaultJSONForPOIBannerAd("poi", handler);
		Assert.assertEquals(JSONObject.class.getName(), o.getClass().getName());
	}

}
