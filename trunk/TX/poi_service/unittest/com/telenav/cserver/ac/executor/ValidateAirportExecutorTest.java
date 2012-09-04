package com.telenav.cserver.ac.executor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.executor.TestUtil;

public class ValidateAirportExecutorTest {
	
	private ValidateAirportExecutor instance = new ValidateAirportExecutor();
	ValidateAirportRequest req = new ValidateAirportRequest();
	ValidateAirportResponse resp = new ValidateAirportResponse();
	
	@Before
	public void setUp(){
		req.setAirportName("SFO");
		req.setUserProfile(getUserProfile());
		req.setRegion("NA");
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSuite(){
		try {
			instance.doExecute(req, resp, getExecutorContext());
			Assert.assertEquals(ExecutorResponse.STATUS_OK, resp.getStatus());
		} catch (ExecutorException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	public UserProfile getUserProfile() {
		return TestUtil.getUserProfile64();
	}
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}
}
