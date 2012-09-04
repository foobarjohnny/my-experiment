package com.telenav.cserver.backend.contents;
import junit.framework.TestCase;

/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */

/**
 * TestGetRviews.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-8-12
 *
 */
public class TestContentManagerServiceProxy extends TestCase
{
    
    private  ContentManagerServiceProxy proxy = ContentManagerServiceProxy.getInstance();
    
    public void testGetReviews() throws Exception
    {
        GetReviewRequest request = new GetReviewRequest();
        //GetReviewRequest=[categoryId=0, poiId=24667, revewId=-1, startIndex=0, endIndex=9, IsOnlySummarizableAttributes=false, IsExcludeReviewsOfEmptyComments=false
        request.setCategoryId(0);
        request.setPoiId(24667);
        request.setRevewId(-1);
        request.setStartIndex(0);
        request.setEndIndex(9);
        request.setOnlySummarizableAttributes(false);
        request.setExcludeReviewsOfEmptyComments(false);
        GetReviewResponse resp = proxy.getReviews(request, null);
        assertEquals("OK", resp.getStatusCode());
    }
}
