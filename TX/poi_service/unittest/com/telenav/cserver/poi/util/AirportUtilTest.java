package com.telenav.cserver.poi.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;


public class AirportUtilTest {

	private AirportUtil instance = new AirportUtil();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {

		dataSource.addData(ValidateAirportRequest.class.getName(), getValidateAirportRequest());		
		dataSource.addData(com.telenav.cserver.ac.executor.v20.ValidateAirportRequest.class.getName(), getValidateAirportRequestV20());
		dataSource.addData(AirportSearchRequest.class.getName(), new AirportSearchRequest());	
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing
		dataSource.clear();
		
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	/*
	 * [unfinished ...]
	 */
	public ValidateAirportRequest getValidateAirportRequest(){
		
		ValidateAirportRequest request = new ValidateAirportRequest();
		request.setAirportName("SFO");
		return request;
	}

	public com.telenav.cserver.ac.executor.v20.ValidateAirportRequest getValidateAirportRequestV20(){
		
		com.telenav.cserver.ac.executor.v20.ValidateAirportRequest request = new com.telenav.cserver.ac.executor.v20.ValidateAirportRequest();
		request.setAirportName("SFO");
		return request;
	}	
	
}
