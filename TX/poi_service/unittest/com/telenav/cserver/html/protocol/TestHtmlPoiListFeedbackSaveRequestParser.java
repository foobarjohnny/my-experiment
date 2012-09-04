package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;

public class TestHtmlPoiListFeedbackSaveRequestParser extends TestRequestParser{
	public void testParseBrowserRequest(){
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getParamJson().toString()).anyTimes();
		HtmlPoiListFeedbackSaveRequestParser requestParser = new HtmlPoiListFeedbackSaveRequestParser();
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
			json.put("searchCatName", "Any");
			json.put("searchKeyword", "");
			JSONObject searchLocation = new JSONObject();
			searchLocation.put("stop", TestUtil.getLocationJSON().toString());
			json.put("searchLocation", searchLocation.toString());
			json.put("feedbackPage", "");
			json.put("feedbackQuestion", "");
			JSONArray jarray = new JSONArray();
			json.put("feedbacks", jarray.toString());
			json.put("comment", "");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
