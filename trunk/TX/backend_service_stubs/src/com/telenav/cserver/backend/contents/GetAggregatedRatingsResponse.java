/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

import java.util.Date;

/**
 * GetAggregatedRatingsResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-7-7
 *
 */
public class GetAggregatedRatingsResponse
{

    private long poiId;
    private int numberOfRatings;
    private double ratingValue;
    private double popularity;
    private int numberOfComments;
    private Date lastUpdatedTime;
    private String statusCode;
    private String statusMessage;
    
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("GetAggregatedRatingsResponse[");
        sb.append("poiId=");
        sb.append(poiId);
        sb.append(", numberOfRatings=");
        sb.append(numberOfRatings);
        sb.append(", ratingValue=");
        sb.append(ratingValue);
        sb.append(", popularity=");
        sb.append(popularity);
        sb.append(", numberOfComments=");
        sb.append(numberOfComments);
        sb.append(", lastUpdatedTime=");
        sb.append(lastUpdatedTime);
        sb.append(", statusCode=");
        sb.append(statusCode);
        sb.append(", statusMessage=");
        sb.append(statusMessage);
        return sb.toString();
    }
    
    public double getPopularity()
    {
        return popularity;
    }
    public void setPopularity(double popularity)
    {
        this.popularity = popularity;
    }
    public long getPoiId()
    {
        return poiId;
    }
    public void setPoiId(long poiId)
    {
        this.poiId = poiId;
    }
    public int getNumberOfRatings()
    {
        return numberOfRatings;
    }
    public void setNumberOfRatings(int numberOfRatings)
    {
        this.numberOfRatings = numberOfRatings;
    }
    public double getRatingValue()
    {
        return ratingValue;
    }
    public void setRatingValue(double ratingValue)
    {
        this.ratingValue = ratingValue;
    }
    public int getNumberOfComments()
    {
        return numberOfComments;
    }
    public void setNumberOfComments(int numberOfComments)
    {
        this.numberOfComments = numberOfComments;
    }
    public Date getLastUpdatedTime()
    {
        return lastUpdatedTime;
    }
    public void setLastUpdatedTime(Date lastUpdatedTime)
    {
        this.lastUpdatedTime = lastUpdatedTime;
    }
    public String getStatusCode()
    {
        return statusCode;
    }
    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }
    public String getStatusMessage()
    {
        return statusMessage;
    }
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }
    
    
    
}
