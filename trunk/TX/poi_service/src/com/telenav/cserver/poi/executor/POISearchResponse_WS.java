/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.resource.data.PromptItem;

/**
 * POISearchResponse_WS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-20
 */
public class POISearchResponse_WS extends ExecutorResponse
{
    /** POI response. */
    private long totalCount;
    private int totalMaxPageIndex;
    private List<POI> poiList;
    private List<POI> sponsorPoiList;
    
    //== For 70 only
    private List<BasePoi> basePoiList;
	//private List<BasePoi> baseSponsorPoiList;//For TN70, Sponsor poi should still return full content
	
    //private TxNode audio;
    private int distanceUnit;
    private PromptItem[] promptItems;
    private int sponsorListingNumber;
    private String poiDetailUrl;

    public int getSponsorListingNumber() {
		return sponsorListingNumber;
	}

	public void setSponsorListingNumber(int sponsorListingNumber) {
		this.sponsorListingNumber = sponsorListingNumber;
	}

	public int getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(int distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the poiList
     */
    public List<POI> getPoiList() {
        return poiList;
    }

    /**
     * @param poiList the poiList to set
     */
    public void setPoiList(List<POI> poiList) {
        this.poiList = poiList;
    }

	/**
	 * @param sponsorPoiList the sponsorPoiList to set
	 */
	public void setSponsorPoiList(List<POI> sponsorPoiList)
	{
		this.sponsorPoiList = sponsorPoiList;
	}

	/**
	 * @return the sponsorPoiList
	 */
	public List<POI> getSponsorPoiList()
	{
		return sponsorPoiList;
	}

	public PromptItem[] getPromptItems() {
		return promptItems;
	}

	public void setPromptItems(PromptItem[] promptItems) {
		this.promptItems = promptItems;
	}

	public int getTotalMaxPageIndex() {
		return totalMaxPageIndex;
	}

	public void setTotalMaxPageIndex(int totalMaxPageIndex) {
		this.totalMaxPageIndex = totalMaxPageIndex;
	}


    public List<BasePoi> getBasePoiList() {
		return basePoiList;
	}

	public void setBasePoiList(List<BasePoi> basePoiList) {
		this.basePoiList = basePoiList;
	}

    public String getPoiDetailUrl()
    {
        return poiDetailUrl;
    }

    public void setPoiDetailUrl(String poiDetailUrl)
    {
        this.poiDetailUrl = poiDetailUrl;
    }
    
}
