package com.telenav.cserver.ac.executor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.executor.TestUtil;;

public class ValidateAddressExecutorACEWSTest {

	private ValidateAddressExecutorACEWS instance = new ValidateAddressExecutorACEWS();
	ValidateAddressRequestACEWS req = new ValidateAddressRequestACEWS();
	ValidateAddressResponseACEWS resp = new ValidateAddressResponseACEWS();
	
	@Before
	public void setUp(){
		req = getValidateAddressRequestACEWS();
		
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
	
	
	public ValidateAddressRequestACEWS getValidateAddressRequestACEWS(){
		ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();
		request.setFirstLine("1130 kifer rd");
		request.setLastLine("Sunnyvale,Ca");
		request.setStreet1("kifer rd");
		request.setStreet2("Lawrence expy");
		request.setCity("Sunnyvale");
		request.setState("Ca");
		request.setCountry("US");
		request.setZip("94086");
		request.setUserProfile(getUserProfile());
		return request;
	}
	
	public UserProfile getUserProfile() {
		return TestUtil.getUserProfile64();
	}
	
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}
}