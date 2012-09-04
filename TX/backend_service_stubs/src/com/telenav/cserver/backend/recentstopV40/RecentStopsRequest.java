/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstopV40;

import com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop;

/**
 * @author zhjdou
 * 
 * Encapsulate the request <code>RecentStopRequestDTO</code>
 */
public class RecentStopsRequest
{      
	private long userId; //

    private RecentStop[] deleteStops;// we only care about the delete stop IDs

    private RecentStop[] addStops;

    private RecentStop[] updateStops;

    private String contextString;//key=value format

    private boolean isNeedPoiInfo; 

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
     * @return the deleteStops
     */
    public RecentStop[] getDeleteStops()
    {
        return deleteStops;
    }

    /**
     * @param deleteStops the deleteStops to set
     */
    public void setDeleteStops(RecentStop[] deleteStops)
    {
        this.deleteStops = deleteStops;
    }

    /**
     * @return the addStops
     */
    public RecentStop[] getAddStops()
    {
        return addStops;
    }

    /**
     * @param addStops the addStops to set
     */
    public void setAddStops(RecentStop[] addStops)
    {
        this.addStops = addStops;
    }

    /**
     * @return the updateStops
     */
    public RecentStop[] getUpdateStops()
    {
        return updateStops;
    }

    /**
     * @param updateStops the updateStops to set
     */
    public void setUpdateStops(RecentStop[] updateStops)
    {
        this.updateStops = updateStops;
    }

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
     * @return the isNeedPoiInfo
     */
    public boolean isNeedPoiInfo()
    {
        return isNeedPoiInfo;
    }

    /**
     * @param isNeedPoiInfo the isNeedPoiInfo to set
     */
    public void setNeedPoiInfo(boolean isNeedPoiInfo)
    {
        this.isNeedPoiInfo = isNeedPoiInfo;
    }

    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("RecentStopsRequest[");
        sb.append("userId="+this.userId);
        sb.append(",contextString="+this.contextString);
        sb.append(", isNeedPoiInfo"+this.isNeedPoiInfo);
        if(this.addStops!=null) {
            sb.append(",AddStops=");
            for(int i=0;i<this.addStops.length;i++) {
                sb.append(this.addStops[i]+"\n");
            }
        }
        if(this.deleteStops!=null) {
            sb.append(", DeleteStops=");
            for(int i=0;i<this.deleteStops.length;i++) {
                sb.append(this.deleteStops[i]+"\n");
            }
        }
        if(this.updateStops!=null) {
            sb.append(", UpdateStops=");
            for(int i=0;i<this.updateStops.length;i++) {
                sb.append(this.updateStops[i]+"\n");
            }
        }
        return sb.toString();
    }
}