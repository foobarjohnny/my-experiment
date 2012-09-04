/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favorites;

/**
 * @author zhjdou
 * 2010-4-1
 */
public class FavoriteBasicResponse
{
    public static final String _OK = "OK";
    private String status_code;
    private String status_message;
    /**
     * @return the status_code
     */
    public String getStatus_code()
    {
        return status_code;
    }
    /**
     * @return the status_message
     */
    public String getStatus_message()
    {
        return status_message;
    }
    /**
     * @param status_code the status_code to set
     */
    public void setStatus_code(String status_code)
    {
        this.status_code = status_code;
    }
    /**
     * @param status_message the status_message to set
     */
    public void setStatus_message(String status_message)
    {
        this.status_message = status_message;
    }
    
    /**
     * The element to String
     */
    public String toString() {
        StringBuffer sf=new StringBuffer();
        sf.append("basicResponse[");
        sf.append("statusCode="+ this.status_code);
        sf.append(", statusMessage="+this.status_message);
        return sf.toString();
    }
}
