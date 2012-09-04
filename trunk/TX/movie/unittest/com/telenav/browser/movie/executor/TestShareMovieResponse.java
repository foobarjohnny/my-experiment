package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorResponse;

public class TestShareMovieResponse {
	
	ShareMovieResponse response;
	@Before
	public void setUp() throws Exception {
		response = new ShareMovieResponse();
		response.setErrorMessage("test error message");
		response.setStatus(ExecutorResponse.STATUS_OK);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(response.getErrorMessage(), "test error message");
		assertEquals(response.getStatus(),ExecutorResponse.STATUS_OK);
	}

}
