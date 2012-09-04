 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.recentstopV40;

/**
 * The comfirm response return from backend service that stand of the confirm request whether
 * handled successful.
 * @author zhjdou
 * 2009-7-28
 */
public class ConfirmResponse
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
        sf.append("confirmResponse[");
        sf.append("statusCode="+ this.statusCode);
        sf.append(", statusMessage="+this.statusMessage);
        return sf.toString();
    }
}
