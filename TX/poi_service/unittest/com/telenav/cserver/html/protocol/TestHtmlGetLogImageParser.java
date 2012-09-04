package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlGetLogImageParser extends TestRequestParser{
	public void testParseBrowserRequest() {
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getParamJson().toString());
		EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(HtmlConstants.OPERATE_FETCH_IMAGE);
		PowerMock.replayAll();
		HtmlGetLogImageParser requestParser = new HtmlGetLogImageParser();
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
			json.put("imageName", "");
			json.put("width", "480");
			json.put("height", "800");
			json.put("center", "");
			json.put("markers", "");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
