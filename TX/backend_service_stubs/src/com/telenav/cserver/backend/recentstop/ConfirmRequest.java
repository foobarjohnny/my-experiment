 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.recentstop;

/**
 * When client received the sync response, this request will send confirm request to backend service
 * @author zhjdou
 * 2009-7-28
 */
public class ConfirmRequest
{   //user id
    private long userId;
    //all stops in sync response inserted stops
    private long[] insertStops;
    //deleted stops
    private long[] deleteStops;
    
    private String contextString;//key=value format
    
  
    /**
     * @return the contextString
     */
    public String getContextString()
    {
        return contextString;
    }

    /**
     * @param contextString the contextString to set
     */
    public void setContextString(String contextString)
    {
        this.contextString = contextString;
    }

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
     * @return the insertStops
     */
    public long[] getInsertStops()
    {
        return insertStops;
    }

    /**
     * @param insertStops the insertStops to set
     */
    public void setInsertStops(long[] insertStops)
    {
        this.insertStops = insertStops;
    }

    /**
     * @return the deleteStops
     */
    public long[] getDeleteStops()
    {
        return deleteStops;
    }

    /**
     * @param deleteStops the deleteStops to set
     */
    public void setDeleteStops(long[] deleteStops)
    {
        this.deleteStops = deleteStops;
    }

    /**
     * the string record the query request details
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("ConfirmRequest[");
        bf.append("UserId=");
        bf.append(this.userId);
        if(this.insertStops!=null) {
            bf.append(",AddStops=");
            for(int i=0;i<this.insertStops.length;i++) {
                bf.append(this.insertStops[i]+"\n");
            }
        }
        if(this.deleteStops!=null) {
            bf.append(", DeleteStops=");
            for(int i=0;i<this.deleteStops.length;i++) {
                bf.append(this.deleteStops[i]+"\n");
            }
        }
        bf.append(", contextString=");
        bf.append(this.contextString);
        bf.append("]");
        return bf.toString();
    }    
}
