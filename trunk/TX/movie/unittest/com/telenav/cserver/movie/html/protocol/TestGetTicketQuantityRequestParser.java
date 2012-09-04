package com.telenav.cserver.movie.html.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.GetTicketQuantityRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class TestGetTicketQuantityRequestParser {

	GetTicketQuantityRequestParser requestParser;
	MockHttpServletRequest httpRequest;
	GetTicketQuantityRequest executorRequest;
	
	@Before
	public void setUp() throws Exception {
		JSONObject jo = new JSONObject();
		jo.put(RRKey.GTQ_MOVIE_ID, "12345");
		jo.put(RRKey.GTQ_THEATER_ID, "23456");
		jo.put(RRKey.GTQ_SHOW_DATE, "2012-02-02");
		jo.put(RRKey.GTQ_SHOW_TIME, "10£º10:10");
		
		httpRequest = new MockHttpServletRequest();
		httpRequest.addParameter("jsonStr", jo.toString());
		httpRequest.addParameter("ssoToken", "AAAAAACYW7gAAAE4Wgj5sMKSwUahHZGbuCb1W123VpY=");

		requestParser = new GetTicketQuantityRequestParser();
	}

	@Test
	public void testParseBrowserRequest() {
		try {
			executorRequest = (GetTicketQuantityRequest) requestParser.parseBrowserRequest(httpRequest);
			Assert.assertEquals("12345", executorRequest.getMovieId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
