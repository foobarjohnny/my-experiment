package com.telenav.cserver.movie.html.protocol;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.datatypes.TicketItem;
import com.telenav.cserver.movie.html.executor.GetTicketQuantityResponse;

public class TestGetTicketQuantityResponseFormatter {

	GetTicketQuantityResponseFormatter responseFormattor;
	MockHttpServletRequest httpRequest;
	GetTicketQuantityResponse executorResponse;
	
	@Before
	public void setUp() throws Exception {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setCurrency("currency");
		ticketItem.setPrice(123);
		ticketItem.setQuantity(2);
		ticketItem.setTicketId("54321");
		ticketItem.setType("type");
		ArrayList<TicketItem> ticketList = new ArrayList<TicketItem>();
		ticketList.add(ticketItem);
		
		executorResponse = new GetTicketQuantityResponse();
		executorResponse.setTicketList(ticketList);
		executorResponse.setSurcharge(12345);
		executorResponse.setConvenienceCharge(1234565);
		
		responseFormattor = new GetTicketQuantityResponseFormatter();
		httpRequest = new MockHttpServletRequest();
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormattor.parseBrowserResponse(httpRequest, executorResponse);
			Assert.assertNotNull(httpRequest.getAttribute("ajaxResponse"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
