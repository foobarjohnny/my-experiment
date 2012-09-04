/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstopV40;

import com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop;


/**
 * Delete all stops on user id
 * @author zhjdou 2009-8-11
 */
public class DeleteStopsRequest
{
    private long userId;

    private String context;
    
    private RecentStop[] delStops;

    /**
     * @return the delStops
     */
    public RecentStop[] getDelStops()
    {
        return delStops;
    }

    /**
     * @param delStops the delStops to set
     */
    public void setDelStops(RecentStop[] delStops)
    {
        this.delStops = delStops;
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
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("DeleteStopsRequest[");
        bf.append("UserId=");
        bf.append(this.userId);
        if(this.delStops!=null) {
            bf.append(",AddStops=");
            for(int i=0;i<this.delStops.length;i++) {
                bf.append(this.delStops[i].toString()+"\n");
            }
        }
        bf.append(", contextString=");
        bf.append(this.context);
        bf.append("]");
        return bf.toString();
    } 
    
}
