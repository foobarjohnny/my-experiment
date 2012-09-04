package com.telenav.cserver.ac.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.executor.TestUtil;

public class BRValidateAddressHandlerTest {

	private BRValidateAddressHandler instance = new BRValidateAddressHandler();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
		
		dataSource.addData(ValidateAddressRequestACEWS.class.getName(), getValidateAddressRequestACEWS());
		dataSource.addData(ValidateAddressResponseACEWS.class.getName(), getValidateAddressResponseACEWS());
		dataSource.addData(ExecutorContext.class.getName(), getExecutorContext());	
	}

	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data
	}


	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public ValidateAddressRequestACEWS getValidateAddressRequestACEWS(){
		
		/*
		 * city=Santa Clara, state=CA, country=, county=, postalCode=95051, 
		 * label=Semiconductor Dr at Kifer Rd, latitude=37.37456, longitude-121.99916, 
		 * firstLine=Semiconductor Dr at Kifer Rd, streetNumberHigh=0, streetNumberLow=0, lastline=null, 
		 * landmark=null, postalCodePlus4=null, streetName=null, streetNumber=null, isIntersection=false, 
		 * suite=null, poBoxnull], recentPoi=null
		 */
		
		ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();
		request.setCity("Santa Clara");
		request.setState("CA");
		request.setZip("95051");
		request.setFirstLine("Semiconductor Dr at Kifer Rd");
		return request;
	}
	
	public ValidateAddressResponseACEWS getValidateAddressResponseACEWS(){
		return new ValidateAddressResponseACEWS();
	}
	
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}
}
