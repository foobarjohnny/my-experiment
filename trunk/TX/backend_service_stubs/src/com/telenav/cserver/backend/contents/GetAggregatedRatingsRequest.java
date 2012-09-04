/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

/**
 * GetAggregatedRatingsRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-7-7
 *
 */
public class GetAggregatedRatingsRequest
{

    private int categoryId;
    private long poiId;

    
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
    @Override
    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append("GetReviewRequest=[");
        sb.append("categoryId=");
        sb.append(categoryId);
        sb.append(", poiId=");
        sb.append(poiId);
        return sb.toString();
    }
    
    

}
