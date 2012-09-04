package com.telenav.cserver.weather.struts.datatype;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import static junit.framework.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeatherViewTest {

	public static final int dayofWeek = 7;
	public static final String parameterOfWeatherInfo = "weatherinfo";
	public static final String parameterOfListWeatherInfo = "listofweatherinfo";
	
	private boolean issetCurrentWeatherAlreadyInvoked = false;
	private boolean issetWeekWeatherAlreadyInvoked = false;
	
	
	WeatherInfo currentWeather = null;
	List<WeatherInfo> weekWeather = null;
	private WeatherView instanceForTest = new WeatherView();
	HashMap<String, Object> dataCenter = new HashMap();
	
	@Before
	public void setUp() throws Exception {
		// setup data for testing 
		if(null == instanceForTest){
			instanceForTest = new WeatherView();
		}
		
		if(null == currentWeather){
			currentWeather = new WeatherInfo();
		}
		
		if(null == weekWeather){
			weekWeather = new ArrayList<WeatherInfo>();
			for(int i = 0; i < WeatherViewTest.dayofWeek; i++){
				weekWeather.add(currentWeather);
			}
		}
		
		dataCenter.put(WeatherViewTest.parameterOfWeatherInfo, currentWeather);
		dataCenter.put(WeatherViewTest.parameterOfListWeatherInfo, weekWeather);
			
	}

	@After
	public void tearDown() throws Exception {
		//
		dataCenter.clear();
	}

	@Test
	public void testGetCurrentWeather() {
		if(!issetCurrentWeatherAlreadyInvoked){
			testSetCurrentWeather();
			issetCurrentWeatherAlreadyInvoked = true;
		}
		
		String methodName = "getCurrentWeather";
		try{
			Class<?>[] parameter = {};
			Object[] parameterObj = {};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);

			Object expected = dataCenter.get(WeatherViewTest.parameterOfWeatherInfo);
			Object actual = m.invoke(instanceForTest, parameterObj);
			
			Assert.assertEquals(expected, actual);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
		}
	}

	@Test
	public void testSetCurrentWeather() {
		
		if(issetCurrentWeatherAlreadyInvoked){
			return;
		}
		
		String methodName = "setCurrentWeather";
		try{
			Class<?>[] parameter = {WeatherInfo.class};
			Object[] parameterObj = {dataCenter.get(WeatherViewTest.parameterOfWeatherInfo)};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			m.invoke(instanceForTest, parameterObj);
			
			assertTrue(true);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
		}
		
		issetCurrentWeatherAlreadyInvoked = true;
	}

	@Test
	public void testGetWeekWeather() {
		if(!issetWeekWeatherAlreadyInvoked){
			testSetWeekWeather();
			issetWeekWeatherAlreadyInvoked = true;
		}
		
		String methodName = "getWeekWeather";
		try{
			Class<?>[] parameter = {};
			Object[] parameterObj = {};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);

			Object expected = dataCenter.get(WeatherViewTest.parameterOfListWeatherInfo);
			Object actual = m.invoke(instanceForTest, parameterObj);
			
			Assert.assertEquals(expected, actual);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
		}
	}

	@Test
	public void testSetWeekWeather() {
		if(issetWeekWeatherAlreadyInvoked){
			return;
		}
		
		String methodName = "setWeekWeather";
		try{
			Class<?>[] parameter = {List.class};
			Object[] parameterObj = {dataCenter.get(WeatherViewTest.parameterOfListWeatherInfo)};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			m.invoke(instanceForTest, parameterObj);
			
			assertTrue(true);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
		}
		
		issetWeekWeatherAlreadyInvoked = true;
	}

}
