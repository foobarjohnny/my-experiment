package com.telenav.browser.movie.protocol;

import static org.junit.Assert.*;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.telenav.browser.movie.executor.ShareMovieResponse;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;

public class TestShareMovieResponseFormatter {
	
	private ShareMovieResponseFormatter responseFormatter;
	private MockHttpServletRequest request;
	private ShareMovieResponse response;

	@Before
	public void setUp() throws Exception {
		responseFormatter = new ShareMovieResponseFormatter();
		request = new MockHttpServletRequest();
		response = new ShareMovieResponse();
		response.setStatus(ExecutorResponse.STATUS_OK);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormatter.parseBrowserResponse(request, response);
			
			TxNode node = (TxNode) request.getAttribute("node");
			assertEquals(ExecutorResponse.STATUS_OK, node.valueAt(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
