/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @TODO	Define the request Object
 * @author	jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiReviewRequest extends ExecutorRequest
{
    private String operateType;
    
    //review key
    private long userId;
    private String userName;
    private int categoryId;
    private long poiId;
    private String locale;
    //review values
    private int rating;
    private String comments;
    private List<ReviewOption> reviewOptions = new ArrayList<ReviewOption>();
    
    private boolean isYelpSupported;
    private boolean isTripAdvisorSupported;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nuserId:");
		sb.append(this.getUserId());
		sb.append("\nuserName:");
		sb.append(this.getUserName());
		sb.append("\noperateType:");
		sb.append(this.getOperateType());

		sb.append("\ncategoryId:");
		sb.append(this.getCategoryId());
		sb.append("\npoiId:");
		sb.append(this.getPoiId());
        sb.append("\nisYelpSupported:");
		sb.append(this.isYelpSupported());
        sb.append("\nisTripAdvisorSupported:");
		sb.append(this.isTripAdvisorSupported());
		
		return sb.toString();
	}
	
    public String getOperateType()
    {
        return operateType;
    }
    public void setOperateType(String operateType)
    {
        this.operateType = operateType;
    }

    public String getComments()
    {
        return comments;
    }
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    public List<ReviewOption> getReviewOptions()
    {
        return reviewOptions;
    }
    public void setReviewOptions(List<ReviewOption> reviewOptions)
    {
        this.reviewOptions = reviewOptions;
    }
    
    public void addRatingProperty(long id, String name, String value)
    {
        ReviewOption option = new ReviewOption();
        option.setId(id);
        option.setName(name);
        option.setValue(value);
        this.reviewOptions.add(option);
    }
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public long getPoiId() {
		return poiId;
	}
	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}  
	
    public void setIsYelpSupported(boolean isYelpSupported) {
        this.isYelpSupported = isYelpSupported;
    }
    
    public boolean isYelpSupported() {
        return this.isYelpSupported;
    }

	public boolean isTripAdvisorSupported() {
		return isTripAdvisorSupported;
	}

	public void setTripAdvisorSupported(boolean isTripAdvisorSupported) {
		this.isTripAdvisorSupported = isTripAdvisorSupported;
	}
	
}
