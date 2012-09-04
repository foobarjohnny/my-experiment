 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.recentstopV40;

/**
 * Delete stops response
 * @author zhjdou
 * 2009-8-11
 */
public class DeleteStopsResponse
{
    private String statusCode;

    private String statusMessage;

    /**
     * @return the statusCode
     */
    public String getStatusCode()
    {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode)
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
     * The element to String
     */
    public String toString() {
        StringBuffer sf=new StringBuffer();
        sf.append("deleteResponse[");
        sf.append("statusCode="+ this.statusCode);
        sf.append(", statusMessage="+this.statusMessage);
        return sf.toString();
    }
}
