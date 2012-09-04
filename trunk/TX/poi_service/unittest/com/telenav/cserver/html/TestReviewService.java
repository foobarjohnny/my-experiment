/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlPoiReviewExecutor;
import com.telenav.cserver.html.executor.HtmlPoiReviewRequest;
import com.telenav.cserver.html.executor.HtmlPoiReviewResponse;
import com.telenav.cserver.html.util.HtmlConstants;

import junit.framework.TestCase;



public class TestReviewService extends TestCase
{
	
	HtmlPoiReviewResponse response;
	HtmlPoiReviewRequest request;
	long userId = 0;
	
	@Override
    protected void setUp() throws Exception
    {
		//
		response = new HtmlPoiReviewResponse();
		request = new HtmlPoiReviewRequest();
		request.setPoiId(3000620744l);
		request.setCategoryId(30019);
		String userToken ="AAAAAAA4kbAAAAEuxtdAfCytu5fV7hiBaKSDLIQPCJ8=";
		String strUserId = HtmlCommonUtil.getUserId(userToken);
		userId = Long.parseLong(strUserId);
    }
	
	public void testAddReview()
	{
		request.setOperateType(HtmlConstants.OPERATE_REVIEW_SUBMIT);
		request.setUserId(userId);
		request.setUserName("zhang pan");
		request.setRating(3);
		request.setComments("This is test comments");
		
		HtmlPoiReviewExecutor excutor = new HtmlPoiReviewExecutor();
		try {
			excutor.doExecute(request, response, TestUtil
					.getExecutorContext());
		} catch (ExecutorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetReview()
	{
		request.setOperateType(HtmlConstants.OPERATE_REVIEW_VIEW);
		
		HtmlPoiReviewExecutor excutor = new HtmlPoiReviewExecutor();
		try {
			excutor.doExecute(request, response, TestUtil
					.getExecutorContext());
		} catch (ExecutorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("RateNumber:" + response.getRateNumber());
		System.out.println("Rating:" + response.getRating());
		
	}
}
