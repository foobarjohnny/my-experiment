/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.weather.struts.datatype;

import java.util.List;

import com.telenav.j2me.framework.protocol.ProtoWeatherView;

/**
 * Data structure for weather view
 * 
 * @author yqchen
 * @version 1.0 2007-11-19 16:53:38
 */
public class WeatherView
{

    WeatherInfo currentWeather;
    
    List<WeatherInfo> weekWeather;

    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if(this.getCurrentWeather() != null)
    	{
    		sb.append("currentWeather:" + this.getCurrentWeather().toString());
    	}
    	return sb.toString();
	}
    
    public WeatherInfo getCurrentWeather()
    {
        return currentWeather;
    }

    public void setCurrentWeather(WeatherInfo currentWeather)
    {
        this.currentWeather = currentWeather;
    }

    public List<WeatherInfo> getWeekWeather() {
        return weekWeather;
    }

    public void setWeekWeather(List<WeatherInfo> weekWeather) {
        this.weekWeather = weekWeather;
    }
    
    public ProtoWeatherView toProtobuf()
    {
    	ProtoWeatherView.Builder builder = ProtoWeatherView.newBuilder();
    	builder.setCurrentWeatherInfo(this.currentWeather.toProtobuf());
    	for(int i = 0 ; i < weekWeather.size(); i++)
    	{
    		builder.addElementWeekWeatherInfos(weekWeather.get(i).toProtobuf());
    	}
    	return builder.build();
    }
    
}
