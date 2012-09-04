package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlPoiReviewRequestParser extends TestRequestParser{
	public void testParseBrowserRequest() {
		EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(HtmlConstants.OPERATE_REVIEW_SUBMIT).anyTimes();
		
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(
				getParamJson().toString()).anyTimes();
		HtmlPoiReviewRequestParser requestParser = new HtmlPoiReviewRequestParser();
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
			json.put("categoryId", "-1");
			json.put("ssoToken", "000");
			json.put("reviewerName", "ccc");
			json.put("rating", "100");
			json.put("comments", "-1");
			
			JSONArray ratingProperties= new JSONArray();
			JSONObject option = new JSONObject();
			option.put("value", "1");
			option.put("id", "1");
			option.put("name", "xx");
			ratingProperties.put(option);
			json.put("ratingProperties", ratingProperties.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
