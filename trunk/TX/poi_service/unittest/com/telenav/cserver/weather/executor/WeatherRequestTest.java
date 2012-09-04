package com.telenav.cserver.weather.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.Stop;

public class WeatherRequestTest {

	private WeatherRequest instance = new WeatherRequest();
	private DataSource dataSource = new DataSource();
	
	
	@Before
	public void setUp() throws Exception {

		dataSource.addData(Stop.class.getName(), getStop());		
		dataSource.addData(boolean.class.getName(), Boolean.valueOf(true));
		dataSource.addData(String.class.getName(), String.valueOf("just for test"));
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
	public Stop getStop(){
		
		Stop stop = new Stop();
		return stop;
	}

}
