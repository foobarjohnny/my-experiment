/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;


/**
 * ReviewRequest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-7-7
 * 
 */
public class GetReviewRequest
{
    public static final long NOEXIST_REVIEW_ID = -1;
    
    private int categoryId;
    private long poiId ;
    
    //for getRatingReviewDetail
    private long revewId = NOEXIST_REVIEW_ID;  
    
    //for getReviewTagList and getRatingReviewDetailList
    private int startIndex;
    private int endIndex;
    
    //all should set to true
    private boolean IsOnlySummarizableAttributes;
    
    //getRatingReviewDetailList should set to true
    private boolean IsExcludeReviewsOfEmptyComments;
    
    @Override
    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append("GetReviewRequest=[");
        sb.append("categoryId=");
        sb.append(categoryId);
        sb.append(", poiId=");
        sb.append(poiId);
        sb.append(", revewId=");
        sb.append(revewId);
        sb.append(", startIndex=");
        sb.append(startIndex);
        sb.append(", endIndex=");
        sb.append(endIndex);
        sb.append(", IsOnlySummarizableAttributes=");
        sb.append(IsOnlySummarizableAttributes);
        sb.append(", IsExcludeReviewsOfEmptyComments=");
        sb.append(IsExcludeReviewsOfEmptyComments);
        return sb.toString();
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
    public int getStartIndex()
    {
        return startIndex;
    }
    public void setStartIndex(int startIndex)
    {
        this.startIndex = startIndex;
    }
    public boolean isExcludeReviewsOfEmptyComments()
    {
        return IsExcludeReviewsOfEmptyComments;
    }
    public void setExcludeReviewsOfEmptyComments(boolean isExcludeReviewsOfEmptyComments)
    {
        IsExcludeReviewsOfEmptyComments = isExcludeReviewsOfEmptyComments;
    }
    public int getEndIndex()
    {
        return endIndex;
    }
    public void setEndIndex(int endIndex)
    {
        this.endIndex = endIndex;
    }
    public boolean isOnlySummarizableAttributes()
    {
        return IsOnlySummarizableAttributes;
    }
    public void setOnlySummarizableAttributes(boolean isOnlySummarizableAttributes)
    {
        IsOnlySummarizableAttributes = isOnlySummarizableAttributes;
    }
  
    public long getRevewId()
    {
        return revewId;
    }
    public void setRevewId(long revewId)
    {
        this.revewId = revewId;
    }
    
    

}
