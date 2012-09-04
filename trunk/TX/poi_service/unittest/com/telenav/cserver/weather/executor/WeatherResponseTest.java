package com.telenav.cserver.weather.executor;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;

public class WeatherResponseTest {
	
	private WeatherResponse instance = new WeatherResponse();
	private DataSource dataSource = new DataSource();

	
	@Before
	public void setUp() throws Exception {

		dataSource.addData(Stop.class.getName(), getStop());		
		dataSource.addData(String.class.getName(), String.valueOf("just for test"));
		dataSource.addData(WeatherView.class.getName(), getWeatherView());
		
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
	

	public Stop getStop(){
		
		Stop stop = new Stop();
		return stop;
	}
	
	public WeatherView getWeatherView(){
		
		return new WeatherView();
	}

}
