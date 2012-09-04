package com.telenav.cserver.movie.html.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONArray;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.BookTicketRequest;

public class TestBookTicketRequestParser {
	
	BookTicketRequestParser requestParser;
	MockHttpServletRequest httpRequest;
	BookTicketRequest executorRequest;
	
	@Before
	public void setUp() throws Exception {
		
		JSONObject ticket = new JSONObject();
		ticket.put("id", "12345");
		ticket.put("price", 10);
		ticket.put("quantity", 3);
		ticket.put("currency", "currency");
		ticket.put("type", "type");
		JSONArray tickets = new JSONArray();
		tickets.put(ticket);
		
		JSONObject jo = new JSONObject();
		jo.put("ticketArray", tickets);
		jo.put("movieId", "54321");
		jo.put("theaterId", "654321");
		jo.put("showDate", "2012-02-02");
		jo.put("confirmEmail", "xwfeng@telenavsoftware.com");
		jo.put("showTime", "12:00:00");
		jo.put("totalAmount", 3);
		
		httpRequest = new MockHttpServletRequest();
		httpRequest.addParameter("jsonStr", jo.toString());
		httpRequest.addParameter("ssoToken", "AAAAAACYW7gAAAE4Wgj5sMKSwUahHZGbuCb1W123VpY=");
		
		requestParser = new BookTicketRequestParser();
	}

	@Test
	public void testParseBrowserRequest() {
		try {
			executorRequest = (BookTicketRequest) requestParser.parseBrowserRequest(httpRequest);
			Assert.assertNotNull(executorRequest.getBookingInfo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
