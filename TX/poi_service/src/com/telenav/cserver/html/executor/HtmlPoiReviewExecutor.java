/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.GetAggregatedRatingsRequest;
import com.telenav.cserver.backend.contents.GetAggregatedRatingsResponse;
import com.telenav.cserver.backend.contents.GetReviewRequest;
import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.contents.SaveReviewsRequest;
import com.telenav.cserver.backend.contents.SaveReviewsResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.html.datatypes.PoiReview;
import com.telenav.cserver.html.datatypes.TripAdvisor;
import com.telenav.cserver.html.datatypes.YelpReview;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @TODO	Call the executor to implement business logic
 * @author jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiReviewExecutor extends AbstractExecutor 
{

    private static final Logger logger = Logger.getLogger(HtmlPoiReviewExecutor.class);
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
	    
		TnContext tc = context.getTnContext();
	    HtmlPoiReviewRequest poiReviewRequest = (HtmlPoiReviewRequest)request;
	    HtmlPoiReviewResponse poiReviewResponse = (HtmlPoiReviewResponse)response;
	    poiReviewResponse.setOperateType(poiReviewRequest.getOperateType());
	    if( HtmlConstants.OPERATE_REVIEW_SUBMIT.equals(poiReviewRequest.getOperateType()) )
	    {
	        poiReviewResponse = addReview(poiReviewRequest,poiReviewResponse,tc);
	        poiReviewResponse = getReviews(poiReviewRequest,poiReviewResponse,tc);
	        
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_VIEW.equals(poiReviewRequest.getOperateType()) )
	    {
	        //poiReviewResponse = createMockResponse(poiReviewResponse);
	    	poiReviewResponse = getReviews(poiReviewRequest,poiReviewResponse,tc);
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_SHOW_ADDREVIEW.equals(poiReviewRequest.getOperateType()) )
	    {
	    	poiReviewResponse = getReviewOptions(poiReviewRequest,poiReviewResponse,tc);
	    	//poiReviewResponse = getMockReviewOptions(poiReviewRequest,poiReviewResponse,tc);
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_GET_REVIEWOPTIONS.equals(poiReviewRequest.getOperateType()) )
	    {
	    	poiReviewResponse = getReviewOptions(poiReviewRequest,poiReviewResponse,tc);
	    	//poiReviewResponse = getMockReviewOptions(poiReviewRequest,poiReviewResponse,tc);
	    }
	    else
	    {
	        poiReviewResponse.setStatus(ExecutorResponse.STATUS_FAIL);
	        logger.error("HtmlPoiReviewExecutor: can't recognize opeateType ["+poiReviewRequest.getExecutorType()+"]");
	    }
	}
	
	private HtmlPoiReviewResponse getReviewOptions(HtmlPoiReviewRequest poiReviewRequest,HtmlPoiReviewResponse poiReviewResponse,TnContext tc)
	{
        try
        {
        	poiReviewResponse.setReviewOptions(getReviewOptions(poiReviewRequest,tc));
        }
        catch (ThrottlingException ex)
        {
            poiReviewResponse.setStatus(ExecutorResponse.STATUS_FAIL);
        }
		return poiReviewResponse;
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param poiId
	 * @param tc
	 * @return
	 * @throws ThrottlingException
	 */
	private List<ReviewOption> getReviewOptions(HtmlPoiReviewRequest poiReviewRequest, TnContext tc) throws ThrottlingException
	{
		String locale = poiReviewRequest.getUserProfile().getLocale();;
	    CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getReviewOptions");
        ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();

        SaveReviewsRequest request = new SaveReviewsRequest();
        
        request.setContextString(tc.toContextString());
        request.setLocale(locale);
        request.setCategoryId(poiReviewRequest.getCategoryId());
        request.setPoiId(poiReviewRequest.getPoiId());
        List<ReviewOption> results = new ArrayList<ReviewOption>();
        try
        {
            results = server.getReviewOptions(request, tc);
        }
        finally
        {
            cli.complete();
        }
        return results;
	}
	
	/**
	 * 
	 * @param poiReviewRequest
	 * @param poiReviewResponse
	 * @param tc
	 * @return
	 */
	private HtmlPoiReviewResponse addReview(HtmlPoiReviewRequest poiReviewRequest,HtmlPoiReviewResponse poiReviewResponse,TnContext tc)
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("addReview");
		ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();

        SaveReviewsRequest request = new SaveReviewsRequest();
        request.setUserId(poiReviewRequest.getUserId());
        request.setReviewerName(poiReviewRequest.getUserName());
        request.setCategoryId(poiReviewRequest.getCategoryId());
        request.setComment(poiReviewRequest.getComments());
        request.setPoiId(poiReviewRequest.getPoiId());
        request.setRating(String.valueOf(poiReviewRequest.getRating()));
        request.setReadonly(true);
        request.setReviewSourceId("0");

        List<ReviewOption> userReviewOptions = poiReviewRequest.getReviewOptions();
        List<ReviewOption> requestReviewOptions = new ArrayList<ReviewOption>();
        for(ReviewOption reviewOption: userReviewOptions){
            ReviewOption copy = new ReviewOption();
            copy.setId(reviewOption.getId());
            copy.setName(reviewOption.getName());
            copy.setValue(reviewOption.getValue());
            requestReviewOptions.add(copy);
        }
        request.setReviewOptions(requestReviewOptions);
        
        //
        try
        {
        	SaveReviewsResponse response = server.saveReviews(request, tc);
            String statusCode = response.getStatusCode();// list the status of response
            String statusMsg = response.getStatusMessage();
            if (logger.isDebugEnabled())
            {
                logger.debug("statusCode=" + statusCode + "statusMsg=" + statusMsg);// log4j
            }
            cli.addData("status", "statusCode=" + statusCode + "statusMsg=" + statusMsg);// cli log

            if (statusCode != null && statusCode.equalsIgnoreCase("OK"))// the status is ok
            {
            	poiReviewResponse.setStatus(ExecutorResponse.STATUS_OK);
            }
            else
            {
            	poiReviewResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "Edit POI EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            poiReviewResponse.setMessage("Status Code =" + response.getStatusCode() + " Message=" + response.getStatusMessage());
        }
        catch (Exception ex)
        {
            logger.error(ex);
            poiReviewResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.setStatus(ex);
            cli.setState(CliConstants.STATUS_FAIL);
        }
        finally
        {
            cli.complete();
        }
		return poiReviewResponse;
	}
	
	/**
	 * 
	 * @param poiReviewRequest
	 * @param poiReviewResponse
	 * @param tc
	 * @return
	 */
	private HtmlPoiReviewResponse getReviews(HtmlPoiReviewRequest poiReviewRequest,HtmlPoiReviewResponse poiReviewResponse,TnContext tc)
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getReviews");

        ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();
		GetAggregatedRatingsRequest getAggregatedRatingsRequest = new GetAggregatedRatingsRequest();
		getAggregatedRatingsRequest.setCategoryId(poiReviewRequest.getCategoryId());
		getAggregatedRatingsRequest.setPoiId(poiReviewRequest.getPoiId());
		try {
			GetAggregatedRatingsResponse getAggregatedRatingsResponse =  server.getAggregatedRatings(getAggregatedRatingsRequest, tc);
			String getAggregatedRatingsStatusCode = getAggregatedRatingsResponse.getStatusCode();// list the status of response
			String getAggregatedRatingsStatusMsg = getAggregatedRatingsResponse.getStatusMessage();
			if (logger.isDebugEnabled())
			{
			    logger.debug("getAggregatedRatingsStatusCode=" + getAggregatedRatingsStatusCode + "getAggregatedRatingsStatusMsg=" + getAggregatedRatingsStatusMsg);// log4j
			}
			cli.addData("getAggregatedRatings status", "getAggregatedRatingsStatusCode=" + getAggregatedRatingsStatusCode + "getAggregatedRatingsStatusMsg=" + getAggregatedRatingsStatusMsg);// cli log
			 
			int ratingNumber = getAggregatedRatingsResponse.getNumberOfRatings();
		    int avgRating = (int)(getAggregatedRatingsResponse.getRatingValue()*10);
		    poiReviewResponse.setRating(avgRating);
		    poiReviewResponse.setRateNumber(ratingNumber);
		    
		    //get review options
		    poiReviewResponse.setReviewOptions(getReviewOptions(poiReviewRequest,tc));
		}
		catch (Exception e) {
        } 

         
		ContentManagerServiceProxy contentManagerServiceProxy = ContentManagerServiceProxy.getInstance();
//		ReviewDetailListRequest request = new ReviewDetailListRequest();
		GetReviewRequest request = new GetReviewRequest();
		//request.setOnlySummarizableAttributes(true);
//		request.setContext(tc.toContextString());
		request.setCategoryId(poiReviewRequest.getCategoryId());
		request.setPoiId(poiReviewRequest.getPoiId());
		request.setStartIndex(0);
		request.setEndIndex(100);
		try {
			GetReviewResponse getReviewResponse = contentManagerServiceProxy.getReviews(request, tc);
			ReviewServicePOIReview[] reviews = getReviewResponse.getReview();
			ReviewServicePOIReview review = null;
			PoiReview poiReview = null;
			if(reviews != null)
			{
				for(int i=0;i< reviews.length;i++)
				{
					review = reviews[i];
					poiReview = new PoiReview();
					poiReview.setReviewerName(HtmlCommonUtil.getString(review.getReviewerName()));
					poiReview.setReviewerId(String.valueOf(review.getUserId()));
					poiReview.setRating(Double.parseDouble(review.getRating())*10);
					poiReview.setComments(HtmlCommonUtil.getString(review.getReviewText()));
					//TODO: get options from backend
					Map<String,String> perperties1 = new LinkedHashMap<String,String>();
					List<ReviewOption> reviewOptions = review.getReviewOptions();
					for(ReviewOption reviewOption : reviewOptions )
					{
						perperties1.put(reviewOption.getId()+"", reviewOption.getValue());
					}
			        poiReview.setReviewOptions(perperties1);
			        
					poiReviewResponse.addPoiReview(poiReview);
				}
			}
			
			if (poiReviewRequest.isTripAdvisorSupported() || poiReviewRequest.isYelpSupported()) {
				// get Yelp And TripAdvisor review
				poiReviewResponse = HtmlPoiDetailServiceProxy.getInstance().getYelpAndTripAdvisorReview(poiReviewRequest, poiReviewResponse, tc);
			}
        }catch(Exception e) {
            logger.error("getReviews",e);
            cli.setStatus(e);
            cli.setState(CliConstants.STATUS_FAIL);
        }
        finally
        {
            cli.complete();
        }
        
		return poiReviewResponse;
	}
}
