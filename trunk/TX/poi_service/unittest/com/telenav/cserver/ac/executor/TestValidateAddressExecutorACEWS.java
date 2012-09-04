package com.telenav.cserver.ac.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.ac.executor.ValidateAddressExecutorACEWS;
import com.telenav.cserver.ac.executor.ValidateAddressRequestACEWS;
import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.framework.executor.ExecutorException;

import junit.framework.TestCase;

public class TestValidateAddressExecutorACEWS extends TestCase {
	private ValidateAddressResponseACEWS resp = new ValidateAddressResponseACEWS();

	public void testDoExecute() throws ExecutorException {
		ValidateAddressExecutorACEWS excutor = new ValidateAddressExecutorACEWS();
		excutor.doExecute(getValidateAddressRequestACEWS(), resp, TestUtil
				.getExecutorContext());
	}

	private ValidateAddressRequestACEWS getValidateAddressRequestACEWS() {
		ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();
		request.setFirstLine("1130 kifer rd");
		request.setLastLine("Sunnyvale,Ca");
		request.setStreet1("kifer rd");
		request.setStreet2("Lawrence expy");
		request.setCity("Sunnyvale");
		request.setState("Ca");
		request.setCountry("US");
		request.setZip("94086");

		request.setUserProfile(TestUtil.getUserProfile());
		return request;
	}
}
