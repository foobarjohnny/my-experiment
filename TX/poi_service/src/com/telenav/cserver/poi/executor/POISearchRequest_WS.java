/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.datatypes.Stop;

/**
 * POISearchRequest_WS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-21
 */
public class POISearchRequest_WS extends ExecutorRequest 
{
    /** Search parameters */
    private String searchString;
    private long categoryId;
    private Stop stop;
    private int radiusInMeter;
    private int searchType;
    private int sortType;
    private int maxResults;
    private boolean mostPopular;
    private int searchFromType;
    private int pageNumber;
    private int pageSize;
    private int routeID;
    private int segmentId;
    private int edgeId;
    private int shapePointId;
    private int range;
    private int currentLat;
    private int currentLon;
    private int distanceUnit;
    private int searchAlongType;
    private boolean needAudio;
    private String productType;
    private String audioFormat;
    private int sponsorListingNumber;
    private String transactionID;
    
    public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public int getSponsorListingNumber() {
		return sponsorListingNumber;
	}

	public void setSponsorListingNumber(int sponsorListingNumber) {
		this.sponsorListingNumber = sponsorListingNumber;
	}

	public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return 	pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public boolean isNeedAudio() {
        return needAudio;
    }

    public void setNeedAudio(boolean needAudio) {
        this.needAudio = needAudio;
    }

    public int getSearchAlongType() {
        return searchAlongType;
    }

    public void setSearchAlongType(int searchAlongType) {
        this.searchAlongType = searchAlongType;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(int edgeId) {
        this.edgeId = edgeId;
    }

    public int getShapePointId() {
        return shapePointId;
    }

    public void setShapePointId(int shapePointId) {
        this.shapePointId = shapePointId;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(int currentLat) {
        this.currentLat = currentLat;
    }

    public int getCurrentLon() {
        return currentLon;
    }

    public void setCurrentLon(int currentLon) {
        this.currentLon = currentLon;
    }

    /**
     * @return the categoryId
     */
    public long getCategoryId() {
        return categoryId;
    }

    public int getSearchFromType() {
        return searchFromType;
    }

    public void setSearchFromType(int searchFromType) {
        this.searchFromType = searchFromType;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }


    /**
     * @return the stop
     */
    public Stop getStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(Stop stop) {
        this.stop = stop;
    }

    /**
     * @return the searchType
     */
    public int getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the maxResults
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * @param maxResults the maxResults to set
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * @return the sortType
     */
    public int getSortType() {
        return sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    /**
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * @return the mostPopular
     */
    public boolean isMostPopular() {
        return mostPopular;
    }

    /**
     * @param mostPopular the mostPopular to set
     */
    public void setMostPopular(boolean mostPopular) {
        this.mostPopular = mostPopular;
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
	 * @param distanceUnit the distanceUnit to set
	 */
	public void setDistanceUnit(int distanceUnit)
	{
		this.distanceUnit = distanceUnit;
	}

	/**
	 * @return the distanceUnit
	 */
	public int getDistanceUnit()
	{
		return distanceUnit;
	}

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }


}
