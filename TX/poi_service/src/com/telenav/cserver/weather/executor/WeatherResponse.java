/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class WeatherResponse extends ExecutorResponse{
    private WeatherView weatherView = null;
    private Stop location;
    private String path = "";
    
    public WeatherView getWeatherView() {
        return weatherView;
    }

    public void setWeatherView(WeatherView weatherView) {
        this.weatherView = weatherView;
    }

    public Stop getLocation() {
        return location;
    }

    public void setLocation(Stop location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public String toString() {
		String weatherViewStr = weatherView != null ? weatherView.toString()
				: "";
		String locationStr = location != null ? location.toString() : "";
		return "[weatherView] = " + weatherViewStr + "; [location] = "
				+ locationStr + "; [path] = " + path;
	}

}
