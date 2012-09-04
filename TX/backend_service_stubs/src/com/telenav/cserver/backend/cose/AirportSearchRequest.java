/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import com.telenav.cserver.backend.datatypes.GeoCode;

/**
 * AirportSearchRequest.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-9-4
 */
public class AirportSearchRequest
{
    private long userId;
    private String transactionId;
    private String airportQuery;
    private int poiSortType;
    private String countryList;
    private GeoCode anchorPoint;
    private int radiusInMeter;
    private String region;
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
     * Not must set, need set country list for dump all airport in country.
     * Country should split by ",". eg. US,CA,MX
     * @param countryList the countryList to set
     */
    public void setCountryList(String countryList)
    {
        this.countryList = countryList;
    }
    /**
     * @return the countryList
     */
    public String getCountryList()
    {
        return countryList;
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
     * @param anchorPoint the anchorPoint to set
     */
    public void setAnchorPoint(GeoCode anchorPoint)
    {
        this.anchorPoint = anchorPoint;
    }
    /**
     * Set search anchor point. Only need set for PoiSearchType.COUNTRYLIST_AND_ANCHOR.
     * The point can used in close ahead or near destination.
     * @return the anchorPoint
     */
    public GeoCode getAnchorPoint()
    {
        return anchorPoint;
    }
    /**
     * Set search radius. Only need set for PoiSearchType.COUNTRYLIST_AND_ANCHOR.
     * 
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
     * @param airportQuery the airportQuery to set
     */
    public void setAirportQuery(String airportQuery)
    {
        this.airportQuery = airportQuery;
    }
    /**
     * @return the airportQuery
     */
    public String getAirportQuery()
    {
        return airportQuery;
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
