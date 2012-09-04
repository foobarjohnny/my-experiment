package com.telenav.cserver.ac.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.ac.executor.ValidateAirportExecutor;
import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.ac.executor.ValidateAirportResponse;
import com.telenav.cserver.framework.executor.ExecutorException;

import junit.framework.TestCase;

public class TestValidateAirportExecutor extends TestCase{
	private ValidateAirportResponse resp = new ValidateAirportResponse();
	
	public void testDoExecute() throws ExecutorException {
		ValidateAirportExecutor excutor = new ValidateAirportExecutor();
		excutor.doExecute(getValidateAirportRequest(), resp, TestUtil
				.getExecutorContext());
	}
	
	private ValidateAirportRequest getValidateAirportRequest(){
		ValidateAirportRequest request = new ValidateAirportRequest();
		
		request.setAirportName("SFO");
        request.setRegion("");
        
        request.setUserProfile(TestUtil.getUserProfile());
		return request;
	}

}
