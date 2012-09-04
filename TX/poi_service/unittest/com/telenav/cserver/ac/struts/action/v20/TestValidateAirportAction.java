package com.telenav.cserver.ac.struts.action.v20;

import java.util.Vector;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.ac.protocol.v20.ValidateAirportRequestParser;
import com.telenav.cserver.ac.protocol.v20.ValidateAirportResponseFormatter;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestValidateAirportAction extends TestCase {
	private ActionMapping mapping;
	private MockHttpServletRequest request;

	@Override
	protected void setUp() throws Exception {
		TxNode node = new TxNode();
		node.addMsg("DEL");
		TxNode ajaxNode = new TxNode();
		ajaxNode.addChild(node);
		request = new MockHttpServletRequest(TxNode.toByteArray(ajaxNode));
		Vector<String> header = new Vector<String>();
		request.setHeaderNames(header.elements());

		request.addParameter("deviceid", "test123");
		request.addParameter("guidetone", "test123");
		request.addParameter("locale", "en_IN");
		request.addParameter("region", "IN");
		request.addParameter("audioformat", "test123");
		request.addParameter("carrier", "MMI");
		request.addParameter("platform", "RIM");
		request.addParameter("devicemodel", "sdk");
		request.addParameter("buildnumber", "6.4.01");
		request.addParameter("width", "480-360");
		request.addParameter("height", "360-480");
		request.addParameter("browserversion", "6.4.01");
		request.addParameter("useraccount", "TEST_1234567890");
		request.addParameter("userpin", "1234");
		request.addParameter("userid", "12345678");
		request.addParameter("producttype", "TN");
		request.addParameter("version", "6_4_01");
		request.addParameter("client_support_screen_width", "480-360");
		request.addParameter("client_support_screen_height", "360-480");

		DataHandler handler = new DataHandler(request, true);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		addGlobeMapping();
	}

	public void addGlobeMapping() {
		mapping = new ActionMapping();
		mapping.addForwardConfig(new ActionForward("success",
				"/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception",
				"/jsp/ErrorPage.jsp", false));
	}

	public void testValidateAirportAction() throws Exception {
		ValidateAirportAction action = new ValidateAirportAction();
		action.setRequestParser(new ValidateAirportRequestParser());
		action.setResponseFormatter(new ValidateAirportResponseFormatter());
		action.execute(mapping, null, request, null);
		System.out.println(request.getRequestURL());
		
		// test result rely on mock server, release this until mock server is stable
		// System.out.println(request.getAttribute("node"));
		// Assert.assertNotNull(request.getAttribute("node"));
		
	}
}
