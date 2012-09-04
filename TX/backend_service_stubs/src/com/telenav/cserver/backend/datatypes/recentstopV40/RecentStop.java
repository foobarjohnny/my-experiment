/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.recentstopV40;

/**
 * @author <b>zhjdou</b> <br>
 *         Recent stop is the based unit for recentSTOP searching, that contain recentAddress or recentPOI, The backend
 *         service only face with the recentSTOP variable, so we will do some transformation work when when we receive
 *         the original request.
 */
public class RecentStop
{
    private long recentStopId;// stop id

    private long ownerUserId;// user id
    
    private String label = "";

    private com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress recentAddress;// the address

//    private com.telenav.cserver.backend.datatypes.recentstop.RecentPoi recentPoi; // POI
    private com.telenav.cserver.backend.datatypes.TnPoi recentPoi;
    
    private boolean isPoi = false;

    private String syncFlag;
    
    /**
     * @return the syncFlag
     */
    public String getSyncFlag()
    {
        return syncFlag;
    }

    /**
     * @param syncFlag the syncFlag to set
     */
    public void setSyncFlag(String syncFlag)
    {
        this.syncFlag = syncFlag;
    }

    /**
     * @return the isPoi
     */
    public boolean isPoi()
    {
        return isPoi;
    }

    /**
     * @param isPoi the isPoi to set
     */
    public void setPoi(boolean isPoi)
    {
        this.isPoi = isPoi;
    }
    
    /*
     * @param set the label
     */
    public void setLabel(String label){
    	this.label = label;
    }
    
    /*
     * @return the label
     */
    public String getLabel(){
    	return this.label;
    }
    

    /**
     * @return the recentStopId
     */
    public long getRecentStopId()
    {
        return recentStopId;
    }

    /**
     * @param recentStopId the recentStopId to set
     */
    public void setRecentStopId(long recentStopId)
    {
        this.recentStopId = recentStopId;
    }

    /**
     * @return the ownerUserId
     */
    public long getOwnerUserId()
    {
        return ownerUserId;
    }

    /**
     * @param ownerUserId the ownerUserId to set
     */
    public void setOwnerUserId(long ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    /**
     * @return the recentAddress
     */
    public com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress getRecentAddress()
    {
        return recentAddress;
    }

    /**
     * @param recentAddress the recentAddress to set
     */
    public void setRecentAddress(com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress recentAddress)
    {
        this.recentAddress = recentAddress;
    }

    /**
     * @return the recentPoi
     */
    public com.telenav.cserver.backend.datatypes.TnPoi getRecentPoi()
    {
        return recentPoi;
    }

    /**
     * @param recentPoi the recentPoi to set
     */
    public void setRecentPoi(com.telenav.cserver.backend.datatypes.TnPoi recentPoi)
    {
        this.recentPoi = recentPoi;
    }

   
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("RecentStop[");
        sb.append("recentStopId=" +this.recentStopId);
        sb.append(", ownerUserId=" +this.ownerUserId);
        sb.append(", isPoi="+ isPoi);
        sb.append(", RecentAddress=" +this.recentAddress);
        sb.append(", recentPoi="+this.recentPoi);
        return sb.toString();
    }
}
