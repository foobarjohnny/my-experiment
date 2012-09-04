package com.telenav.cserver.ac.struts.action.v20;

import java.util.Vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.telenav.cserver.ac.protocol.v20.ShareAddressRequestParser;
import com.telenav.cserver.ac.protocol.v20.ShareAddressResponseFormatter;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestShareAddressAction extends TestCase {
	private ActionMapping mapping;
	private MockHttpServletRequest request;

	@Override
	protected void setUp() throws Exception {
		TxNode node = new TxNode();
		node.addMsg("{\"poiString\":\"{\\\"poiOrStop\\\":\\\"poi\\\",\\\"stop\\\":{\\\"zip\\\":\\\"94085\\\",\\\"lastLine\\\":\\\"\\\",\\\"lon\\\":-12198815,\\\"isGeocoded\\\":1,\\\"stopStatus\\\":0,\\\"state\\\":\\\"CA\\\",\\\"hashCode\\\":0,\\\"label\\\":\\\"Innkeeper Residence Inn\\\",\\\"type\\\":0,\\\"country\\\":\\\"US\\\",\\\"city\\\":\\\"SUNNYVALE\\\",\\\"poiOrStop\\\":\\\"stop\\\",\\\"typeStop\\\":28,\\\"stopId\\\":\\\"0\\\",\\\"firstLine\\\":\\\"750 LAKEWAY DR\\\",\\\"lat\\\":3738739},\\\"category\\\":\\\"\\\",\\\"distance\\\":\\\"0\\\",\\\"poiId\\\":3432104066,\\\"phoneNumber\\\":\\\"4087741853\\\",\\\"name\\\":\\\"Innkeeper Residence Inn\\\",\\\"ad\\\":{},\\\"rating\\\":0}\",\"addressString\":\"{\\\"zip\\\":\\\"94085\\\",\\\"lastLine\\\":\\\"\\\",\\\"lon\\\":-12198815,\\\"isGeocoded\\\":1,\\\"stopStatus\\\":0,\\\"state\\\":\\\"CA\\\",\\\"hashCode\\\":0,\\\"label\\\":\\\"Innkeeper Residence Inn\\\",\\\"type\\\":0,\\\"country\\\":\\\"US\\\",\\\"city\\\":\\\"SUNNYVALE\\\",\\\"poiOrStop\\\":\\\"stop\\\",\\\"typeStop\\\":28,\\\"stopId\\\":\\\"0\\\",\\\"firstLine\\\":\\\"750 LAKEWAY DR\\\",\\\"lat\\\":3738739}\",\"label\":\"Innkeeper Residence Inn\",\"sendTo\":\"[{\\\"ptn\\\":\\\"5555215556\\\",\\\"name\\\":\\\"\\\"}]\"}");
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

	public void testShareAddressAction() throws Exception {
		ShareAddressAction action = new ShareAddressAction();
		action.setRequestParser(new ShareAddressRequestParser());
		action.setResponseFormatter(new ShareAddressResponseFormatter());
		action.execute(mapping, null, request, null);
		System.out.println(request.getAttribute("node"));
		
		// test result rely on mock server, release this until mock server is stable
		// Assert.assertNotNull(request.getAttribute("node"));
		// Assert.assertEquals("Y", ((TxNode)request.getAttribute("node")).msgAt(0));
		
	}
}
