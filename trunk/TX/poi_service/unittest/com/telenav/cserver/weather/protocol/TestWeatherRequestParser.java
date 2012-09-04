package com.telenav.cserver.weather.protocol;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.TestCase;

public class TestWeatherRequestParser extends TestCase{
	public void testParseBrowserRequest() {
		HttpServletRequest httpRequest = PowerMock
				.createMock(HttpServletRequest.class);
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler).anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(getBodyTxNode()).anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER)).andReturn("Rogers").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_LOCALE)).andReturn("en_US").anyTimes();
		WeatherRequestParser requestParser = new WeatherRequestParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TxNode getBodyTxNode(){
		TxNode node = new TxNode();
		JSONObject jo = new JSONObject();
		try {
			jo.put("addressString", TestUtil.getLocationJSON().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		node.addMsg(jo.toString());
		return node;
	}
}
