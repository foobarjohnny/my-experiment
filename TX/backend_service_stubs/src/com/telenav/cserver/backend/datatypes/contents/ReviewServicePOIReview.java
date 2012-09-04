/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.contents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * copy from <code>TnPoiReviewDetail</code>
 * 
 * @author zhjdou 2009-7-31
 */
public class ReviewServicePOIReview
{
    private Date createTime;

    private long poiId;

    //private float popularity;

    private String rating;

    private int ratingCount;

    //private double relevanceIndicator;

    private long reviewId;

    private String reviewSource;

    private String reviewText;

    //private int reviewType;

    private String reviewerName;

    //private long instanceId;
    private Date updateTime;

    private long userId;
    
    private List<ReviewOption> reviewOptions = new ArrayList<ReviewOption>();
    
    
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public long getPoiId()
    {
        return poiId;
    }

    public void setPoiId(long poiId)
    {
        this.poiId = poiId;
    }


    public String getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public int getRatingCount()
    {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount)
    {
        this.ratingCount = ratingCount;
    }

    public long getReviewId()
    {
        return reviewId;
    }

    public void setReviewId(long reviewId)
    {
        this.reviewId = reviewId;
    }

    public String getReviewSource()
    {
        return reviewSource;
    }

    public void setReviewSource(String reviewSource)
    {
        this.reviewSource = reviewSource;
    }

    public String getReviewText()
    {
        return reviewText;
    }

    public void setReviewText(String reviewText)
    {
        this.reviewText = reviewText;
    }

    public String getReviewerName()
    {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }
    
    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }
    
    public List<ReviewOption> getReviewOptions()
    {
        return reviewOptions;
    }

    public void setReviewOptions(List<ReviewOption> reviewOptions)
    {
        this.reviewOptions = reviewOptions;
    }
    
    public void addReviewOption(ReviewOption reviewOption)
    {
        this.reviewOptions.add(reviewOption);
    }

    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append("POIReview=[");
        sb.append("uerId=");
        sb.append(this.userId);
        sb.append(", poiId=");
        sb.append(this.poiId);
        sb.append(", rating=");
        sb.append(this.rating);
        sb.append(", createTime=");
        sb.append(this.createTime.toString());
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append(", ratingCount=");
        sb.append(this.ratingCount);
        sb.append(", reviewId=");
        sb.append(this.reviewId);
        sb.append(", reviewSource=");
        sb.append(this.reviewSource);
        sb.append(", reviewText=");
        sb.append(this.reviewText);
        sb.append(", reviewerName=");
        sb.append(this.reviewerName);
        if(reviewOptions != null)
        {
            sb.append(", reviewOptions=");
            sb.append(reviewOptions.toString());
        }
        return sb.toString();
    }
}
