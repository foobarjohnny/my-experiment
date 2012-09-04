/*
 * Copyright (c) 2011 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author Derek Liu
 *
 * @version 1.0, 2011-3-1
 */
public class I18NWeatherRequest extends ExecutorRequest{
    private Stop location;
    private boolean isCanadianCarrier;
    private boolean isCelciusUnit;
    private boolean isKilometerUnit;
    private String locale; 
    private DataHandler handler;
    private boolean isUseOriginalPicCode = false;

    public DataHandler getHandler() {
		return handler;
	}

	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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

	public Stop getLocation() {
        return location;
    }

    public void setLocation(Stop location) {
        this.location = location;
    }
    
	public boolean isUseOriginalPicCode() {
		return isUseOriginalPicCode;
	}

	public void setUseOrinalPicCode(boolean isUseOriginalPicCode) {
		this.isUseOriginalPicCode = isUseOriginalPicCode;
	}

	@Override
	public String toString() {
		return "I18NWeatherRequest [isCelciusUnit=" + isCelciusUnit
				+ ",isKilometerUnit=" + isKilometerUnit +
				", locale=" + locale + ", location=" + location + 
				", isUseOriginalPicCode="+isUseOriginalPicCode+
				"]";
	}   
   
}
