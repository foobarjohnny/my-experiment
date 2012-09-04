/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.guidetone;

/**
 * BackendGetGuideToneOrderRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-9-3
 *
 */
public class GetUserGuideToneInfoRequest
{

    private long userId;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("userId="+userId);
        return sb.toString();
    }
    
    
    
}
