package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.protocol.HtmlFeedbackSaveRequestParser;

public class TestHtmlFeedbackSaveRequestParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		HtmlClientInfo clientInfo = TestUtil.getHtmlClientInfo(); 
		EasyMock.expect((HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO)).andReturn(clientInfo).anyTimes();
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getPoilistJson().toString()).anyTimes();
		PowerMock.replayAll();
		HtmlFeedbackSaveRequestParser requestParser = new HtmlFeedbackSaveRequestParser();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testParseBrowserRequestForPoidetail() {
		HttpServletRequest httpRequest = PowerMock.createMock(HttpServletRequest.class);
		HtmlClientInfo clientInfo = TestUtil.getHtmlClientInfo(); 
		EasyMock.expect((HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO)).andReturn(clientInfo).anyTimes();
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getPoidetailJson().toString()).anyTimes();
		PowerMock.replayAll();
		HtmlFeedbackSaveRequestParser requestParser = new HtmlFeedbackSaveRequestParser();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final String[][] userAgents= {
		{"Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7", " CPU iPhone OS 4_0 like Mac OS X"},
		{"Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17"," Android 2.0"},
		{"Mozilla/5.0 (Linux; U; Android 1.5; en-us; MB200 Build/CUPCAKE) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1"," Android 1.5"},
		{"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3", " CPU iPhone OS 5_0 like Mac OS X"},
		{"Nokia3650/1.0 UP.Browser/6.2", ""}
	};
	
	public void testGetOSVersion(){
		HtmlFeedbackSaveRequestParser requestParser = new HtmlFeedbackSaveRequestParser();
		for(String[] userAgent: userAgents){
			String osVersion = requestParser.getOSVersion(userAgent[0]);
			assertEquals("Expected osVersion [" + userAgent[1] + "] but was [" + osVersion + "]", userAgent[1], osVersion);
		}
	}
	
	private JSONObject getPoilistJson(){
		JSONObject json = new JSONObject();
		try {
			json.put("currentLocation", TestUtil.getLocationJSON());
			json.put("email", "");
			json.put("userAgent", "1;2;3");
			json.put("pageName", "poilist");
			json.put("ssoToken", "");
			JSONObject jobject = new JSONObject();
			jobject.put("stop", TestUtil.getLocationJSON().toString());
			json.put("searchLocation", jobject.toString());
			json.put("feedbacks", new JSONArray());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private JSONObject getPoidetailJson(){
		JSONObject json = new JSONObject();
		try {
			json.put("currentLocation", TestUtil.getLocationJSON());
			json.put("email", "");
			json.put("userAgent", "1;2;3");
			json.put("pageName", "poidetail");
			json.put("ssoToken", "");
			JSONObject jobject = new JSONObject();
			jobject.put("stop", TestUtil.getLocationJSON().toString());
			json.put("poiLocation", jobject.toString());
			json.put("feedbacks", new JSONArray());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
