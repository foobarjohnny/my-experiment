package com.telenav.cserver.weather.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorResponse;

import com.telenav.cserver.util.helper.DataSource;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;


public class I18NWeatherResponseFormatterTest {

	private I18NWeatherResponseFormatter instance = new I18NWeatherResponseFormatter();
	private DataSource dataCenter = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		dataCenter.clear();
	}

	@Test
	public void testI18NWeatherResponseFormatter() {
		System.out.println("I18NWeatherResponseFormatter will be tested in I18NWeatherActionTest");
		//GenericTest.getInstance().startTest(instanceForTest, dataCenter);
	}
	
	public I18NWeatherResponse getI18NWeatherResponse(){
		
		I18NWeatherResponse response = new I18NWeatherResponse();
		response.setStatus(ExecutorResponse.STATUS_OK);
		return response;
	}
}
