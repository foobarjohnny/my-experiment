/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * PoiReview.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jan 27, 2011
 *
 */
public class PoiReview
{
	private String reviewerId;
    private String reviewerName;
    private double rating;
    private String comments;
    private Map<String, String> reviewOptions = new LinkedHashMap<String,String>();
    
    public double getRating()
    {
        return rating;
    }
    public void setRating(double rating)
    {
        this.rating = rating;
    }
    public String getComments()
    {
        return comments;
    }
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    public Map<String, String> getReviewOptions()
    {
        return reviewOptions;
    }
    public void setReviewOptions(Map<String, String> reviewOptions)
    {
        this.reviewOptions = reviewOptions;
    }
    

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("reviewerName", reviewerName);
        json.put("rating", rating+"");
        json.put("comments", comments);
        if(  reviewOptions != null && reviewOptions.size() != 0 )
        {
            JSONObject ratingPropertiesJSON = new JSONObject();
            Iterator<String> keys = reviewOptions.keySet().iterator();
            while(keys.hasNext())
            {
                String key = keys.next();
                ratingPropertiesJSON.put(key, reviewOptions.get(key));
            }
            json.put("ratingProperties", ratingPropertiesJSON);
        }
        return json;
    }
	public String getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
}
