/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor.v20;

import java.util.List;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * ValidateAddressResponseACEWS.java
 *
 * @author pzhang@telenav.cn
 * @version 2.0 2010-8-18
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAirportResponse extends ExecutorResponse
{
    /** Validate Address response. */
    private List<TnPoi> airportList;

	public List<TnPoi> getAirportList() {
		return airportList;
	}

	public void setAirportList(List<TnPoi> airportList) {
		this.airportList = airportList;
	}
}
