/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstopV40;

import com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop;

/**
 * @author zhjdou
 * 
 */
public class RecentStopsResponse
{
    public final static int STATUS_OK = 0;

    private RecentStop[] insertedStops;

    private RecentStop[] deletedStopIds;

    private int totalRecentStops;

    private int status;// status code

    private String statusMessage;
    
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
     * @return Returns the deletedStopIds.
     */
    public RecentStop[] getDeletedStopIds()
    {
        return deletedStopIds;
    }

    /**
     * @param deletedStopIds The deletedStopIds to set.
     */
    public void setDeletedStopIds(RecentStop[] deletedStopIds)
    {
        this.deletedStopIds = deletedStopIds;
    }

    /**
     * @return Returns the insertedStops.
     */
    public RecentStop[] getInsertedStops()
    {
        return insertedStops;
    }

    /**
     * @param insertedStops The insertedStops to set.
     */
    public void setInsertedStops(RecentStop[] insertedStops)
    {
        this.insertedStops = insertedStops;
    }

    /**
     * @return Returns the totalRecentStops.
     */
    public int getTotalRecentStops()
    {
        return totalRecentStops;
    }

    /**
     * @param totalRecentStops The totalRecentStops to set.
     */
    public void setTotalRecentStops(int totalRecentStops)
    {
        this.totalRecentStops = totalRecentStops;
    }

    /**
     * @return Returns the status.
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("RecentStopsResponse[");
        sb.append("status="+this.status);
        sb.append(", statusMessage"+this.statusMessage);
        sb.append(", totalRecentStops="+this.totalRecentStops);
        if(this.insertedStops!=null) {
            sb.append(",insertedStops=");
            for(int i=0;i<this.insertedStops.length;i++) {
                sb.append(this.insertedStops[i].toString()+"\n");
            }
        }
        if(this.deletedStopIds!=null) {
            sb.append(", deletedStopIds=");
            for(int i=0;i<this.deletedStopIds.length;i++) {
                sb.append(this.deletedStopIds[i].toString()+"\n");
            }
        }
        return sb.toString();
    }
}
