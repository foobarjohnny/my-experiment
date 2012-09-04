package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.executor.GetRestaurantDetailRequest;

public class TestGetRestaurantDetailRequestParser extends TestRequestParser{
	public void testParseBrowserRequest(){
		EasyMock.expect(httpRequest.getParameter("partnerId")).andReturn(getPartnerId()+"").anyTimes();
		GetRestaurantDetailRequestParser requestParser = new GetRestaurantDetailRequestParser();
		PowerMock.replayAll();
		try {
			GetRestaurantDetailRequest weatherRequest = (GetRestaurantDetailRequest) requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			assertEquals("The two Object should be same.",getGetRestaurantDetailRequest().toString(), weatherRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GetRestaurantDetailRequest getGetRestaurantDetailRequest(){
		GetRestaurantDetailRequest req = new GetRestaurantDetailRequest();
		req.setPartnerPoiId(getPartnerId());
		return req;
	}
	
	private Long getPartnerId(){
		return 100001L;
	}
}
