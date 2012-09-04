package com.telenav.cserver.ac.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.executor.TestUtil;

public class CommonValidateAirportHandlerTest {

	private CommonValidateAirportHandler instance = new CommonValidateAirportHandler();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
		
		dataSource.addData(ValidateAirportRequest.class.getName(), getValidateAirportRequest());
		dataSource.addData(ExecutorContext.class.getName(), getExecutorContext());	
		dataSource.addData(PoiSearchResponse.class.getName(), getPoiSearchResponse());	
	}

	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data
	}


	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	

	public ValidateAirportRequest getValidateAirportRequest(){
		ValidateAirportRequest request = new ValidateAirportRequest();
		request.setAirportName("SFO");
		return request;
	}

	public PoiSearchResponse getPoiSearchResponse(){
		return new PoiSearchResponse();
	}
	
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}

}
