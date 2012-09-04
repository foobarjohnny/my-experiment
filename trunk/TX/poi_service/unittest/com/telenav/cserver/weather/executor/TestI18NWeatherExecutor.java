package com.telenav.cserver.weather.executor;

import junit.framework.TestCase;

public class TestI18NWeatherExecutor extends TestCase{
	private I18NWeatherResponse resp = new I18NWeatherResponse();
	
	public void testCase(){
		System.out.println("do noting now, just for pass the testing, otherwise it will report No tests found in com.telenav.cserver.adjuggler.executor.TestAdJugglerExecutor");
		System.out.println("complete this test in other place");
	}
	
	private I18NWeatherRequest getI18NWeatherRequest(){
		I18NWeatherRequest request = new I18NWeatherRequest();
		request.setCanadianCarrier(false);
		request.setLocale("en_US");
		request.setLocation(null);
		
		System.out.println(request.toString());
		return request;
	}

}
