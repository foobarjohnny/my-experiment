package com.telenav.cserver.weather.protocol.protobuf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;
import com.telenav.cserver.weather.struts.datatype.WeatherInfo;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.datatypes.Stop;

public class I18NWeatherProtoResponseFormatterTest extends I18NWeatherProtoResponseFormatter{
	
	ProtocolBuffer formatTarget = new ProtocolBuffer();
	I18NWeatherResponse resp = new I18NWeatherResponse();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		resp.setLocale("en_US");
		Stop location = new Stop();
		resp.setLocation(location);
        location.firstLine = "Semiconductor Dr at Kifer Rd";
        location.city = "Santa Clara";
		location.state = "CA";
		location.country = "USA";
		location.lon = -12199916;
		location.lat = 3737456;
		location.zip = "95051";
		location.label = "";
		
		resp.setCanadianCarrier(false);
		

		WeatherInfo currentWeather = new WeatherInfo();
		WeatherView weatherView = new WeatherView();
		List<WeatherInfo> weekWeather = new ArrayList<WeatherInfo>();
		for(int i = 0; i < 7; i++)
		{
			weekWeather.add(currentWeather);
		}
		weatherView.setCurrentWeather(currentWeather);
		weatherView.setWeekWeather(weekWeather);
		
		resp.setWeatherView(weatherView);
		
		currentWeather.setTemp("70");
		currentWeather.setStatus("");
		currentWeather.setHigh("75");
		currentWeather.setLow("60");
		currentWeather.setDayOfWeek(7);
		currentWeather.setFeel("feel");
		currentWeather.setWind("wind");
		currentWeather.setHumidity("humidity");
		currentWeather.setDate(new Date(System.currentTimeMillis()));
		currentWeather.setWindDirection("E");
		currentWeather.setWindSpeed(13);
		currentWeather.setWeatherCode(1);
		currentWeather.setTemperatureCode(1);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}


}
