/**
* (c) Copyright 2012 TeleNav.
* All Rights Reserved.
*/
package com.telenav.cserver.html.datatypes;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * @TODO
 * @author  panzhang@telenav.cn
 * @version 1.0 2012-8-23
 */

public class TripAdvisor {
	@Override
	public String toString()
	{
		return "TripAdvisor [avgRatingReviews=" + avgRatingReviews + ", reviewCount=" + reviewCount + ", id=" + id + "]";
	}

	public static final String AVG_RATING_REVIEWS 	= "avgRating";
	public static final String REVIEW_COUNT 		= "reviewCount";
	public static final String ID			= "id";
	
	private int avgRatingReviews;
	private int reviewCount;
	private String id;
	
	public void setAvgRatingReviews(int avgRatingReviews)
	{
		this.avgRatingReviews = avgRatingReviews;
	}
	
	public void setReviewCount(int reviewCount)
	{
		this.reviewCount = reviewCount;
	}

	public int getAvgRatingReviews()
	{
		return this.avgRatingReviews;
	}
	
	public int getReviewCount()
	{
		return this.reviewCount;
	}
	


    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put(AVG_RATING_REVIEWS, this.getAvgRatingReviews());
        json.put(REVIEW_COUNT, this.getReviewCount());
        json.put(ID, this.getId());
        
        return json;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

