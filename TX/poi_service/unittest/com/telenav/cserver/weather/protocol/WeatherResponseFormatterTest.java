package com.telenav.cserver.weather.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.telenav.cserver.util.helper.DataSource;


public class WeatherResponseFormatterTest {

	private WeatherResponseFormatter instance = new WeatherResponseFormatter();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		dataSource.clear();
	}

	@Test
	public void testParseBrowserResponseHttpServletRequestExecutorResponse() {	
		System.out.println("WeatherResponseFormatter will be tested in WeatherActionTest");
		//GenericTest.getInstance().startTest(instanceForTest, dataCenter);
	}

}
