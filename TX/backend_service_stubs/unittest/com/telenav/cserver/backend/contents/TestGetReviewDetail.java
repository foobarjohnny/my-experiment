/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
/**
 *
 * @author zhjdou 2009-7-31
 */
public class TestGetReviewDetail
{
    /**getReviewDetail()   :review id 81, poi id 13000000237L

getReviewDetailList  : start Index 1  end Index 10  poi id 13000000237L

getReviewTagList: start Index 1  end Index 10  poi id 13000000237L

     * @param args
     */
	ContentManagerServiceProxy proxy;
	
	TnContext tc;
	
	@Before
	public void init()
	{
		proxy = ContentManagerServiceProxy.getInstance();
		tc = new TnContext();
	}
	
	@Test
	public void getReviews()
	{
		GetReviewRequest req = new GetReviewRequest();
		req.setPoiId(3000538954L);
		req.setStartIndex(1);
		req.setEndIndex(20);
		req.setOnlySummarizableAttributes(false);
		req.setExcludeReviewsOfEmptyComments(true);
		GetReviewResponse resp = null;
		try {
			resp = proxy.getReviews(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(resp);
		Assert.assertSame("OK", resp.getStatusCode());
		if(resp.getReview().length > 0)
		{
			System.out.println("getReviews() >>>>>>> " + resp.toString());
		}
	}
	
	@Test
	public void getReviewOptions()
	{
		SaveReviewsRequest req = new SaveReviewsRequest();
		req.setPoiId(3000538954L);
		req.setUserId(12345678);
		req.setComment("test");
		
		List<ReviewOption> roList = null;
		try {
			roList = proxy.getReviewOptions(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(roList);
		if(roList.size()>0)
		{
			System.out.println("getReviewOptions() >>>>>>>>> response size =" + roList.size());
			System.out.println("getReviewOptions() >>>>>>>>> " + roList.get(0).toString());
		}
	}
	
	@Test
	public void getAggregatedRating()
	{
		GetAggregatedRatingsRequest req = new GetAggregatedRatingsRequest();
		req.setPoiId(3000538954L);
		
		GetAggregatedRatingsResponse resp = null;
		try {
			resp = proxy.getAggregatedRatings(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(resp);
		Assert.assertSame("OK", resp.getStatusCode());
		System.out.println("getAggregatedRating() >>>>>>>>>>>>>>>>> " + resp.toString());
	}
	
	@Test
	public void saveReviews()
	{
		SaveReviewsRequest req = new SaveReviewsRequest();
		req.setPoiId(3000538954L);
		req.setReviewerName("Tester");
		req.setRating("5");
		req.setReviewSourceId("147258");
		
		SaveReviewsResponse resp = null;
		try {
			resp = proxy.saveReviews(req, tc);
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(resp);
		Assert.assertSame("OK", resp.getStatusCode());
		System.out.println("saveReviews() >>>>>>>>>>>>> " + resp.toString());
	}
}
