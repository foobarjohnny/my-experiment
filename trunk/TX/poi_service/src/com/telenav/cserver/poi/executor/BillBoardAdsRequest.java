/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * BillBoardAdsRequest
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-3-15
 */
public class BillBoardAdsRequest extends ExecutorRequest 
{
    private long routeId;
    public long getRouteId()
    {
        return routeId;
    }
    public void setRouteId(long routeId)
    {
        this.routeId = routeId;
    }
 
}
