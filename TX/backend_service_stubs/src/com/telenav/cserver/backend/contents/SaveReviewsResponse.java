/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

/**
 * SaveReviewsResponse.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-7-7
 * 
 */
public class SaveReviewsResponse
{

    private String statusCode;

    private String statusMessage;
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SaveReviewsResponse[");
        sb.append("statusCode=");
        sb.append(statusCode);
        sb.append(", statusMessage=");
        sb.append(statusMessage);
        return sb.toString();
    }
    

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }
    
    
}
