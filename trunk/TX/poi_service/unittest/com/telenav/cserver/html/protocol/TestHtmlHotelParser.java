package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;

public class TestHtmlHotelParser extends TestRequestParser{
	public void testParseBrowserRequest(){
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getParamJson().toString()).anyTimes();
		HtmlHotelParser requestParser = new HtmlHotelParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject getParamJson(){
		JSONObject json = new JSONObject();
		try {
			json.put("poiId", "0");
			json.put("isDummy", "true");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
