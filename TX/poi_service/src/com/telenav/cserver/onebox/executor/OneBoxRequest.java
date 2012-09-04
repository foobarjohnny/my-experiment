/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.onebox.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.datatypes.Stop;

/**
 * Request for onebox search from client.
 * 
 * @author weiw
 */

public class OneBoxRequest extends ExecutorRequest {

	/** Search parameters */
	private String searchString;
	private Stop stop;
	private Stop stopDest;
	private int sortType;
	private int maxResults;
	private int pageNumber;
	private int distanceUnit;
	private int searchType;
    private int searchFromType;
    private int routeID;
    private int segmentId;
    private int edgeId;
    private int shapePointId;
    private int range;
    private int currentLat;
    private int currentLon;
    private int searchAlongType;
    private int sponsorListingNumber;
    private String transactionId;
    
    public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public int getSponsorListingNumber() {
		return sponsorListingNumber;
	}

	public void setSponsorListingNumber(int sponsorListingNumber) {
		this.sponsorListingNumber = sponsorListingNumber;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("searchString:" + searchString + ";");
		buffer.append("stop:" + stop.toString());
		return buffer.toString();
	}
    
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the stop
	 */
	public Stop getStop() {
		return stop;
	}

	/**
	 * @param stop
	 *            the stop to set
	 */
	public void setStop(Stop stop) {
		this.stop = stop;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults
	 *            the maxResults to set
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
	 * @param sortType
	 *            the sortType to set
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
	 * @param searchString
	 *            the searchString to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * @param distanceUnit
	 *            the distanceUnit to set
	 */
	public void setDistanceUnit(int distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	/**
	 * @return the distanceUnit
	 */
	public int getDistanceUnit() {
		return distanceUnit;
	}

	public int getSearchFromType() {
		return searchFromType;
	}

	public void setSearchFromType(int searchFromType) {
		this.searchFromType = searchFromType;
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

	public int getSearchAlongType() {
		return searchAlongType;
	}

	public void setSearchAlongType(int searchAlongType) {
		this.searchAlongType = searchAlongType;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public Stop getStopDest() {
		return stopDest;
	}

	public void setStopDest(Stop stopDest) {
		this.stopDest = stopDest;
	}

}
