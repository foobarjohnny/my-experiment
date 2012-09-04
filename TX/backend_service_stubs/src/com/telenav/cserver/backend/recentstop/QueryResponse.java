/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstop;

import com.telenav.cserver.backend.datatypes.recentstop.RecentStop;

/**
 *
 * Response contain all recent stops
 * @author zhjdou
 *  2007-07-15
 */
public class QueryResponse
{   
    public final static int STATUS_OK = 0;// 0 stand for success
    
    private RecentStop[] stops;

    private int statusCode;

    private String statusMessage;
    
    /**
     * @return the statusCode
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage()
    {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the stops
     */
    public RecentStop[] getStops()
    {
        return stops;
    }

    /**
     * @param stops the stops to set
     */
    public void setStops(RecentStop[] stops)
    {
        this.stops = stops;
    }
    
    /**
     * The element to String
     */
    public String toString() {
        StringBuffer sf=new StringBuffer();
        sf.append("QueryResponse[");
        sf.append("statusCode="+ this.statusCode);
        sf.append(", statusMessage="+this.statusMessage);
        return sf.toString();
    }
}
