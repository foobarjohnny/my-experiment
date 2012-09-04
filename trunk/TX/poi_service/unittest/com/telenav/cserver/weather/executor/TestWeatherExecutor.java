package com.telenav.cserver.weather.executor;

import org.json.me.JSONException;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.Stop;

import junit.framework.TestCase;

public class TestWeatherExecutor extends TestCase {
	private WeatherResponse resp = new WeatherResponse();

	public void testDoExecute() throws ExecutorException, JSONException {
		WeatherExecutor excutor = new WeatherExecutor();
		excutor.doExecute(getWeatherRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private WeatherRequest getWeatherRequest() throws JSONException {
		WeatherRequest request = new WeatherRequest();

		Stop address = PoiUtil.convertAddress(TestUtil.getLocationJSON());
		request.setLocation(address);
		request.setCanadianCarrier(false);
		
		System.out.println(request.toString());

		return request;
	}
}
