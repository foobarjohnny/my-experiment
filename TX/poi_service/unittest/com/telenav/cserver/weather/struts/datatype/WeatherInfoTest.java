package com.telenav.cserver.weather.struts.datatype;

import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

import org.json.me.JSONObject;

public class WeatherInfoTest {

	private WeatherInfo instance = new WeatherInfo();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
		
		dataSource.addData(int.class.getName(), Integer.valueOf(1));             // the int value should bigger than 0
		dataSource.addData(float.class.getName(), Float.valueOf(0.1f));
		dataSource.addData(JSONObject.class.getName(), new JSONObject());
		dataSource.addData(String.class.getName(), String.valueOf("just for test"));
		dataSource.addData(Date.class.getName(), new Date(System.currentTimeMillis()));
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}

}
