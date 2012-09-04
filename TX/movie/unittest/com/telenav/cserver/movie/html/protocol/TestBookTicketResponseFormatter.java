package com.telenav.cserver.movie.html.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.BookTicketResponse;

public class TestBookTicketResponseFormatter {

	MockHttpServletRequest httpRequest;
	BookTicketResponseFormatter responseFormattor;
	BookTicketResponse executorResponse;
	
	@Before
	public void setUp() throws Exception {
		httpRequest = new MockHttpServletRequest();
		responseFormattor = new BookTicketResponseFormatter();
		executorResponse = new BookTicketResponse();
		
		executorResponse.setOrderId("12345");
	}

	@Test
	public void test() {
		try {
			responseFormattor.parseBrowserResponse(httpRequest, executorResponse);
			Assert.assertEquals("12345", httpRequest.getAttribute("ajaxResponse"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
