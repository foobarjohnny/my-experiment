package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlPoiDetailParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(
				HtmlConstants.OPERATE_POIDETAIL_MAIN).anyTimes();
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(
				getParamJson().toString()).anyTimes();
		HtmlPoiDetailParser requestParser = new HtmlPoiDetailParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject getParamJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("poiId", "0");
			json.put("width", "480");
			json.put("height", "800");
			json.put("menuWidth", "200");
			json.put("menuHeight", "100");
			json.put("categoryId", "-1");
			json.put("adsId", "0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
