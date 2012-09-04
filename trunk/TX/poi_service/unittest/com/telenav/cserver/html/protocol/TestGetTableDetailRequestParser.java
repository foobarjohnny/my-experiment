package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.executor.GetTableDetailRequest;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestGetTableDetailRequestParser extends TestRequestParser{
	public void testParseBrowserRequest() {
		EasyMock.expect(httpRequest.getParameter("jsonStr")).andReturn(getJSONParam().toString());
		GetTableDetailRequestParser requestParser = new GetTableDetailRequestParser();
		PowerMock.replayAll();
		try {
			GetTableDetailRequest req = (GetTableDetailRequest) requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			assertEquals("The two Object should be same.",getGetTableDetailRequest().toString(), req.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GetTableDetailRequest getGetTableDetailRequest(){
		GetTableDetailRequest request = new GetTableDetailRequest();
		JSONObject json = getJSONParam();
		try {
			request.setPartnerPoiId(json.getLong(HtmlConstants.RRKey.RESTAURANT_PARTNER_POI_ID));
			request.setPartySize(json.getInt(HtmlConstants.RRKey.PARTY_SIZE));
			request.setSearchTime((String) json.getString(HtmlConstants.RRKey.SEARCH_REQUEST_TIME));
			request.setSearchDate((String) json.getString(HtmlConstants.RRKey.SEARCH_REQUEST_DATE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return request;
	}
	
	private JSONObject getJSONParam(){
		JSONObject json = new JSONObject();
		try {
			json.put(HtmlConstants.RRKey.RESTAURANT_PARTNER_POI_ID, 10001L);
			json.put(HtmlConstants.RRKey.PARTY_SIZE, 1);
			json.put(HtmlConstants.RRKey.SEARCH_REQUEST_TIME, "");
			json.put(HtmlConstants.RRKey.SEARCH_REQUEST_DATE, "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}

}
