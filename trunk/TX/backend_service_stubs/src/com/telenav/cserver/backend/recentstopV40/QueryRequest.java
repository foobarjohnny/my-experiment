/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstopV40;

/**
 * The request to retrieve all recent stop
 * @author zhjdou
 * 2009-07-15
 */
public class QueryRequest
{
    private long userId;

    private int maxResult; // max value

    private String context;

    /**
     * @return the userId
     */
    public long getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    /**
     * @return the count
     */
    public int getMaxResult()
    {
        return maxResult;
    }

    /**
     * @param count the count to set
     */
    public void setMaxResult(int maxResult)
    {
        this.maxResult = maxResult;
    }

    /**
     * @return the context
     */
    public String getContext()
    {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * the string record the query request details
     * The format: QueryReuqest[UserId=xxxx,MaxResult=xxx]
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("QueryReuqest[");
        bf.append("UserId=");
        bf.append(this.userId);
        bf.append(", MaxResult=");
        bf.append(this.maxResult);
        bf.append(", context=");
        bf.append(this.context);
        bf.append("]");
        return bf.toString();
    }
}
