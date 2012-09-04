/*
 * Copyright (c) 2011 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;

/**
 * @author Derek Liu
 *
 * @version 1.0, 2011-3-1
 */
public class I18NWeatherResponse extends ExecutorResponse{
    private WeatherView weatherView = null;
    private Stop location;
    private String path = "";
    private boolean isCanadianCarrier;
    private boolean isCelciusUnit;
    private boolean isKilometerUnit;
    private String locale;    
    
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
    
	public boolean isCanadianCarrier() {
		return isCanadianCarrier;
	}

	public void setCanadianCarrier(boolean isCanadianCarrier) {
		this.isCanadianCarrier = isCanadianCarrier;
	}
    
	public boolean isCelciusUnit() {
		return isCelciusUnit;
	}

	public void setCelciusUnit(boolean isCelciusUnit) {
		this.isCelciusUnit = isCelciusUnit;
	}

	
	public boolean isKilometerUnit() {
		return isKilometerUnit;
	}

	public void setKilometerUnit(boolean isKilometerUnit) {
		this.isKilometerUnit = isKilometerUnit;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
		
    public String toString() {
		String weatherViewStr = weatherView != null ? weatherView.toString()
				: "";
		String locationStr = location != null ? location.toString() : "";
		return "[weatherView] = " + weatherViewStr + "; [location] = "
				+ locationStr + "; [path] = " + path;
	}

}
