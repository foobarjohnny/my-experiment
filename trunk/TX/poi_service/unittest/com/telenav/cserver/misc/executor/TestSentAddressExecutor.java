package com.telenav.cserver.misc.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import junit.framework.TestCase;

public class TestSentAddressExecutor extends TestCase {
	private SentAddressResponse resp = new SentAddressResponse();

	public void testDoExecute() throws ExecutorException {
		SentAddressExecutor excutor = new SentAddressExecutor();
		excutor.doExecute(getSentAddressRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private SentAddressRequest getSentAddressRequest() {
		SentAddressRequest request = new SentAddressRequest();
		request.setUserId(9826225);
		request.setAction("");
		request.setId("");
		
		request.setUserProfile(TestUtil.getUserProfile());
		
		System.out.println(request.toString());

		return request;
	}
}
