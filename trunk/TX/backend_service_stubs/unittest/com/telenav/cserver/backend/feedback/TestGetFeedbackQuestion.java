 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.feedback;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.feedback.Comment;
import com.telenav.cserver.backend.datatypes.feedback.Feedback;
import com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic;
import com.telenav.cserver.backend.datatypes.feedback.GeoCode;
import com.telenav.cserver.backend.datatypes.feedback.MediumType;
import com.telenav.cserver.backend.datatypes.feedback.NavigationContext;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestGetFeedbackQuestion
{
	FeedbackServiceProxy proxy = null;
	TnContext tc = null;
	@Before
	public void init()
	{
		proxy=FeedbackServiceProxy.getInstance();
		tc = new TnContext();
		tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
		tc.addProperty("dataset", "Navteq");
		tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
		tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH");
		tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
	}

	@Test
	public void getFeedbackQuestions()
	{
		GetFeedbackQuestionsRequest getRequest=new GetFeedbackQuestionsRequest();
		getRequest.setCarrier("Cingular");
		getRequest.setDevice("9000");
		getRequest.setPlatform("RIM");
		getRequest.setTopic(new FeedbackTopic(FeedbackTopic._CANCELLATION));
		
		getRequest.setUserClient("AN");
		getRequest.setUserClientVersion("6.0.01");
		getRequest.setLocale("en_US");
		try
		{
			GetFeedbackQuestionsResponse response=proxy.getFeedbackQuestions(getRequest, tc);
			Assert.assertNotNull(response);
			Assert.assertNotNull(response.getQuestion());
			Assert.assertTrue(response.getQuestion().length > 0);
		}
		catch (ThrottlingException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void createFeedback()
	{
		FeedbackServiceRequest request = new FeedbackServiceRequest();
		
		Feedback feedback = new Feedback();
		feedback.setFeedbackTime(System.currentTimeMillis());
	    feedback.setPtn("4088366110");
        feedback.setUserId(798010);
		feedback.setTitle("Test_Title");
		
		Comment comment = new Comment();
		comment.setComments("Test_Comment");
		comment.setChoice(new String[] { "Test_Choice1", "Test_Choice2" });
		comment.setCommentator("Test_Commentator");
		
		MediumType text = new MediumType(MediumType._TEXT);
		comment.setMediumType(text);
		text.toString();
		
		NavigationContext navContext = new NavigationContext();
		GeoCode geoCode = new GeoCode();
		geoCode.setLongitude(123.32112);
		geoCode.setLatitude(23.45678);
		GeoCode geoCode1 = new GeoCode();
		geoCode1.setLongitude(129.12345);
		geoCode1.setLatitude(33.45678);
		
		navContext.setCurrentLocation(new GeoCode[]{geoCode});
		navContext.setDestination(geoCode1);
		navContext.setOriginFix(new GeoCode[]{geoCode});
		navContext.setDeviationFix(new GeoCode[]{geoCode1});
		System.out.println(navContext.toString());
		
		feedback.setComment(new Comment[]{comment});
		
		request.setComment(new Comment[]{comment});
		request.setFeedback(feedback);
		
		FeedbackServiceResponse response = null;
		try {
			request.toString();
			response = proxy.createFeedBack(request, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(response);
		Assert.assertEquals("OK", response.getStatusCode());
		response.toString();
	}
}
