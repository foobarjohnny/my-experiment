/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.datatypes.contents.ReviewOption;

/**
 * SaveReviewsRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-7-7
 *
 */
public class SaveReviewsRequest
{
    
    private int categoryId;
    private long poiId ;
    private long userId;
    private String reviewerName;
    private String reviewSourceId;
    private boolean readonly;
    private String comment;
    private String rating;
    private List<ReviewOption> reviewOptions = new ArrayList<ReviewOption>();
    private String locale = "";
    private String contextString;

    @Override
    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append("SaveReviewsRequest=[");
        sb.append("categoryId=");
        sb.append(categoryId);
        sb.append(", userId=");
        sb.append(userId);
        sb.append(", poiId=");
        sb.append(poiId);
        sb.append(", reviewerName=");
        sb.append(reviewerName);
        sb.append(", reviewSourceId=");
        sb.append(reviewSourceId);
        sb.append(", readonly=");
        sb.append(readonly);
        sb.append(", comment=");
        sb.append(comment);
        sb.append(", rating=");
        sb.append(rating);
        sb.append(", locale=");
        sb.append(locale);
        return sb.toString();
    }

    public String getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public long getPoiId()
    {
        return poiId;
    }

    public void setPoiId(long poiId)
    {
        this.poiId = poiId;
    }
    
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getReviewerName()
    {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }

    public String getReviewSourceId()
    {
        return reviewSourceId;
    }

    public void setReviewSourceId(String reviewSourceId)
    {
        this.reviewSourceId = reviewSourceId;
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
    }

    public List<ReviewOption> getReviewOptions()
    {
        return reviewOptions;
    }

    public void setReviewOptions(List<ReviewOption> reviewOptions)
    {
        this.reviewOptions = reviewOptions;
    }

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getContextString() {
		return contextString;
	}

	public void setContextString(String contextString) {
		this.contextString = contextString;
	}

    
    

}
