package com.telenav.cserver.weather.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.ws.datatypes.content.weather.WeatherDetails;

public class WeatherProxyTest {

	private WeatherProxy instanceForTest = new WeatherProxy();
	private DataSource dataCenter = new DataSource();

	
	
	@Before
	public void setUp() throws Exception {

		dataCenter.addData(boolean.class.getName(), Boolean.valueOf(true));		
		dataCenter.addData(int.class.getName(), Integer.valueOf(1));	
		dataCenter.addData(WeatherDetails.class.getName(), getWeatherDetails());
	
		dataCenter.addData(Stop.class.getName(), getStop());		
		dataCenter.addData(String.class.getName(), String.valueOf("just for test"));
		dataCenter.addData(WeatherView.class.getName(), getWeatherView());
		dataCenter.addData(WeatherProxy.class.getName(), instanceForTest);
		
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing
		dataCenter.clear();
		
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instanceForTest, dataCenter);
	}
	
	/*
	 * [unfinished ...]
	 */
	public Stop getStop(){
		
		Stop stop = new Stop();
		return stop;
	}
	
	public WeatherView getWeatherView(){
		
		WeatherView weatherView = new WeatherView();
		return weatherView;
	}
	
	public WeatherDetails getWeatherDetails(){
		
		return new WeatherDetails();
	}

}
