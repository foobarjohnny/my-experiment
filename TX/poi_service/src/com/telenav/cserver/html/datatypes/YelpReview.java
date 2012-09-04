/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import org.json.me.JSONException;
import org.json.me.JSONObject;


/**
 * YelpReview.java
 *
 * xinrongl@telenav.com
 * @version 1.0 Apr 3, 2012
 *
 */

public class YelpReview 
{
	@Override
	public String toString()
	{
		return "YelpReview [avgRatingReviews=" + avgRatingReviews + ", reviewCount=" + reviewCount + ", yelpPoiId=" + yelpPoiId + "]";
	}

	public static final String AVG_RATING_REVIEWS 	= "avgRating";
	public static final String REVIEW_COUNT 		= "reviewCount";
	public static final String YELP_POI_ID			= "yelpPoiId";
	
	private int avgRatingReviews;
	private int reviewCount;
	private String yelpPoiId;
	
	public void setAvgRatingReviews(int avgRatingReviews)
	{
		this.avgRatingReviews = avgRatingReviews;
	}
	
	public void setReviewCount(int reviewCount)
	{
		this.reviewCount = reviewCount;
	}
	
	public void setYelpPoiId(String yelpPoiId)
	{
		this.yelpPoiId = yelpPoiId;
	}
	
	public int getAvgRatingReviews()
	{
		return this.avgRatingReviews;
	}
	
	public int getReviewCount()
	{
		return this.reviewCount;
	}
	
	public String getYelpPoiId()
	{
		return this.yelpPoiId;
	}

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put(AVG_RATING_REVIEWS, this.getAvgRatingReviews());
        json.put(REVIEW_COUNT, this.getReviewCount());
        json.put(YELP_POI_ID, this.getYelpPoiId());
        
        return json;
    }
}
