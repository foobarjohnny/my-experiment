/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class WeatherRequest extends ExecutorRequest{
    private Stop location;
    private boolean isCanadianCarrier;

    public boolean isCanadianCarrier() {
		return isCanadianCarrier;
	}

	public void setCanadianCarrier(boolean isCanadianCarrier) {
		this.isCanadianCarrier = isCanadianCarrier;
	}

	public Stop getLocation() {
        return location;
    }

    public void setLocation(Stop location) {
        this.location = location;
    }
    
    public String toString() {
		String locationStr = location != null ? location.toString() : "";
		return "[location] = " + locationStr;
	}
}
