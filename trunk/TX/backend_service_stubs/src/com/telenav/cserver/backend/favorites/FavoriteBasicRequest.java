/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favorites;

/**
 * 
 * @author zhjdou
 * 2010-4-1
 */
public class FavoriteBasicRequest
{
    private long userId;
    private String contextString;


    /**
     * @return the context
     */
    public String getContextString() {
        return contextString;
    }

    /**
     * @param context the context to set
     */
    public void setContextString(String context) {
        this.contextString = context;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    /**
     * for log
     */
    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append("BasicRequest: ");
        sb.append("userId=");
        sb.append(this.userId);
        return sb.toString();
    }
}
