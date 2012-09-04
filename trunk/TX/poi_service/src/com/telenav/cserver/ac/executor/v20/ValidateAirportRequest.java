/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor.v20;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * ValidateAddressRequestACEWS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAirportRequest extends ExecutorRequest
{
	private String airportName;
	private String region;
    public String getAirportName() {
        return airportName;
    }
    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }


}
