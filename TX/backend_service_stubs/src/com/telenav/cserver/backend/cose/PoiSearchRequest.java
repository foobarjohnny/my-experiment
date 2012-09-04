/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import java.util.List;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.GeoCode;

/**
 * PoiSearchRequest.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-9
 */
public class PoiSearchRequest
{
	private long userId;
	private String transactionId;
	//private String contextString;
	private int poiSortType;
	private String poiQuery;
	private long categoryId;
	private String categoryVersion;
	private long hierarchyId;
	private Address anchor;
	private int pageNumber;
	private int pageSize;
	private int maxResult;
	private int minReturnedItems;
	private boolean onlyMostPopular;
	private boolean needSponsoredPois;
	private boolean needUserPreviousRating;
	private boolean needHighlyRelevantPois;
	private boolean needUserGeneratePois;
	private boolean autoExpandSearchRadius;
	private int ugcPreferenceType;
	private int radiusInMeter;
	private List<GeoCode> routePoints;
	private String region;
	private int sponsorListingNumber = 1;
	
	public int getSponsorListingNumber() {
		return sponsorListingNumber;
	}

	public void setSponsorListingNumber(int sponsorListingNumber) {
		this.sponsorListingNumber = sponsorListingNumber;
	}

	public String toString() {
	    StringBuilder sb=new StringBuilder();
	    sb.append("PoiSearchRequest==>[");
	    sb.append(", region=").append(this.region);
	    sb.append("userid=").append(this.userId);
	    sb.append(", transactionId=").append(this.transactionId);
	    sb.append(", poiSortType=").append(poiSortType);
	    sb.append(", poiQuery=").append(this.poiQuery);
	    sb.append(", categoryId=").append(this.categoryId);
	    sb.append(", categoryVersion=").append(this.categoryVersion);
	    sb.append(", hierarchyId=").append(this.hierarchyId);
	    sb.append(", anchor=").append(this.anchor.toString());
	    sb.append(", pageNumber=").append(this.pageNumber);
	    sb.append(", pageSize=").append(this.pageSize);
	    sb.append(", maxResult=").append(this.maxResult);
	    sb.append(", minReturnedItems=").append(this.minReturnedItems);
	    sb.append(", onlyMostPopular=").append(this.onlyMostPopular);
	    sb.append(", needSponsoredPois=").append(this.needSponsoredPois);
	    sb.append(", needUserPreviousRating=").append(this.needUserPreviousRating);
	    sb.append(", needHighlyRelevantPois=").append(this.needHighlyRelevantPois);
	    sb.append(", needUserGeneratePois=").append(this.needUserGeneratePois);
	    sb.append(", autoExpandSearchRadius=").append(this.autoExpandSearchRadius);
	    sb.append(", ugcPreferenceType=").append(this.ugcPreferenceType);
	    sb.append(", radiusInMeter=").append(this.radiusInMeter);
	    sb.append(", routePoints=");
	    if(routePoints!=null) {
	        for(int i=0;i<routePoints.size();i++) {
	            sb.append("\n");
	            sb.append(routePoints.get(i).toString());
	        }
	    }
	    sb.append("]");
	    return sb.toString();
	}
	
	/**
	 * @param poiSortType the poiSortType to set
	 */
	public void setPoiSortType(int poiSortType)
	{
		this.poiSortType = poiSortType;
	}
	/**
	 * @return the poiSortType
	 */
	public int getPoiSortType()
	{
		return poiSortType;
	}
	/**
	 * @param poiQuery the poiQuery to set
	 */
	public void setPoiQuery(String poiQuery)
	{
		this.poiQuery = poiQuery;
	}
	/**
	 * @return the poiQuery
	 */
	public String getPoiQuery()
	{
		return poiQuery;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId)
	{
		this.categoryId = categoryId;
	}
	/**
	 * @return the categoryId
	 */
	public long getCategoryId()
	{
		return categoryId;
	}
	/**
	 * @param categoryVersion the categoryVersion to set
	 */
	public void setCategoryVersion(String categoryVersion)
	{
		this.categoryVersion = categoryVersion;
	}
	/**
	 * @return the categoryVersion
	 */
	public String getCategoryVersion()
	{
		return categoryVersion;
	}
	/**
	 * @param hierarchyId the hierarchyId to set
	 */
	public void setHierarchyId(long hierarchyId)
	{
		this.hierarchyId = hierarchyId;
	}
	/**
	 * @return the hierarchyId
	 */
	public long getHierarchyId()
	{
		return hierarchyId;
	}
	/**
	 * @param anchor the anchor to set
	 */
	public void setAnchor(Address anchor)
	{
		this.anchor = anchor;
	}
	/**
	 * @return the anchor
	 */
	public Address getAnchor()
	{
		return anchor;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(int pageNumber)
	{
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the pageNumber
	 */
	public int getPageNumber()
	{
		return pageNumber;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return pageSize;
	}
	
	/**
	 * @param maxResult, set the maxResult
	 */
	public void setMaxResult(int maxResult)
	{
		this.maxResult = maxResult;
	}
	/**
	 * @return the maxResult
	 */
	public int getMaxResult()
	{
		return maxResult;
	}
	
	/**
	 * @param minReturnedItems the minReturnedItems to set
	 */
	public void setMinReturnedItems(int minReturnedItems)
	{
		this.minReturnedItems = minReturnedItems;
	}
	/**
	 * @return the minReturnedItems
	 */
	public int getMinReturnedItems()
	{
		return minReturnedItems;
	}
	/**
	 * @param onlyMostPopular the onlyMostPopular to set
	 */
	public void setOnlyMostPopular(boolean onlyMostPopular)
	{
		this.onlyMostPopular = onlyMostPopular;
	}
	/**
	 * @return the onlyMostPopular
	 */
	public boolean isOnlyMostPopular()
	{
		return onlyMostPopular;
	}
	/**
	 * @param needSponsoredPois the needSponsoredPois to set
	 */
	public void setNeedSponsoredPois(boolean needSponsoredPois)
	{
		this.needSponsoredPois = needSponsoredPois;
	}
	/**
	 * @return the needSponsoredPois
	 */
	public boolean isNeedSponsoredPois()
	{
		return needSponsoredPois;
	}
	/**
	 * @param needUserPreviousRating the needUserPreviousRating to set
	 */
	public void setNeedUserPreviousRating(boolean needUserPreviousRating)
	{
		this.needUserPreviousRating = needUserPreviousRating;
	}
	/**
	 * @return the needUserPreviousRating
	 */
	public boolean isNeedUserPreviousRating()
	{
		return needUserPreviousRating;
	}
	/**
	 * @param needHighlyRelevantPois the needHighlyRelevantPois to set
	 */
	public void setNeedHighlyRelevantPois(boolean needHighlyRelevantPois)
	{
		this.needHighlyRelevantPois = needHighlyRelevantPois;
	}
	/**
	 * @return the needHighlyRelevantPois
	 */
	public boolean isNeedHighlyRelevantPois()
	{
		return needHighlyRelevantPois;
	}
	/**
	 * @param autoExpandSearchRadius the autoExpandSearchRadius to set
	 */
	public void setAutoExpandSearchRadius(boolean autoExpandSearchRadius)
	{
		this.autoExpandSearchRadius = autoExpandSearchRadius;
	}
	/**
	 * @return the autoExpandSearchRadius
	 */
	public boolean isAutoExpandSearchRadius()
	{
		return autoExpandSearchRadius;
	}
	/**
	 * @param ugcPreferenceType the ugcPreferenceType to set
	 */
	public void setUgcPreferenceType(int ugcPreferenceType)
	{
		this.ugcPreferenceType = ugcPreferenceType;
	}
	/**
	 * @return the ugcPreferenceType
	 */
	public int getUgcPreferenceType()
	{
		return ugcPreferenceType;
	}
	
	/**
	 * @param needUserGeneratePois the needUserGeneratePois to set
	 */
	public void setNeedUserGeneratePois(boolean needUserGeneratePois)
	{
		this.needUserGeneratePois = needUserGeneratePois;
	}
	/**
	 * @return the needUserGeneratePois
	 */
	public boolean isNeedUserGeneratePois()
	{
		return needUserGeneratePois;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId)
	{
		this.userId = userId;
	}
	/**
	 * @return the userId
	 */
	public long getUserId()
	{
		return userId;
	}
	/**
	 * @param radiusInMeter the radiusInMeter to set
	 */
	public void setRadiusInMeter(int radiusInMeter)
	{
		this.radiusInMeter = radiusInMeter;
	}
	/**
	 * @return the radiusInMeter
	 */
	public int getRadiusInMeter()
	{
		return radiusInMeter;
	}
	/**
	 * @param routePoints the routePoints to set
	 */
	public void setRoutePoints(List<GeoCode> routePoints)
	{
		this.routePoints = routePoints;
	}
	/**
	 * @return the routePoints
	 */
	public List<GeoCode> getRoutePoints()
	{
		return routePoints;
	}

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId()
    {
        return transactionId;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region)
    {
        this.region = region;
    }

    /**
     * @return the region
     */
    public String getRegion()
    {
        return region;
    }
}
