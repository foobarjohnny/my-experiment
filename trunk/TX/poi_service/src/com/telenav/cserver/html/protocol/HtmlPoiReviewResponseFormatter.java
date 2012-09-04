/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.html.datatypes.PoiReview;
import com.telenav.cserver.html.executor.HtmlPoiReviewResponse;
import com.telenav.cserver.html.util.HtmlConstants;

/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiReviewResponseFormatter extends HtmlProtocolResponseFormatter
{

	private Logger logger =  Logger.getLogger(HtmlPoiReviewResponseFormatter.class);
    @Override
    public void parseBrowserResponse(HttpServletRequest httpServletRequest, ExecutorResponse response) throws Exception
    {
        HtmlPoiReviewResponse poiReviewResponse =  (HtmlPoiReviewResponse)response;
        String operationType = poiReviewResponse.getOperateType();
        
        if( HtmlConstants.OPERATE_REVIEW_SUBMIT.equals(operationType) )
	    {
            httpServletRequest.setAttribute("ajaxResponse", toJSONString(poiReviewResponse)); 
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_VIEW.equals(operationType) )
	    {
	    	httpServletRequest.setAttribute("ajaxResponse", toJSONString(poiReviewResponse)); 
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_SHOW_ADDREVIEW.equals(operationType) )
	    {
	    	httpServletRequest.setAttribute("reviewOptions", poiReviewResponse.getReviewOptions()); 
	    }
	    else if( HtmlConstants.OPERATE_REVIEW_GET_REVIEWOPTIONS.equals(operationType) )
	    {
	    	httpServletRequest.setAttribute("ajaxResponse", toJSONStringForReviewOption(poiReviewResponse.getReviewOptions())); 
	    }
    }
    
    private String toJSONStringForReviewOption(List<ReviewOption> reviewOptions)
    {
    	JSONArray optionsJSON = new JSONArray();
	    	for (ReviewOption option : reviewOptions)
	    	{ 
				try {
					JSONObject oneOption = new JSONObject();
					oneOption.put("id", option.getId());
					oneOption.put("name", option.getName());
					optionsJSON.put(oneOption);
				} catch (JSONException e) {
					logger.error("JSONException error occured !! option is "+option.toString());
					e.printStackTrace();
				}
	        } 
    	return optionsJSON.toString();
    }
    
    /**
     * 
     * @param response
     * @return
     * @throws JSONException
     */
    private String toJSONString(HtmlPoiReviewResponse response) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("poiId", response.getPoiId());
        json.put("rating", response.getRating());
        json.put("rateNumber", response.getRateNumber());
        json.put("reviewOptions", toJSONStringForReviewOption(response.getReviewOptions()));
        
        if (response.getYelpReview() != null)
        {
            json.put("yelp", response.getYelpReview().toJSON());
        }
        
        if (response.getTripAdvisor() != null)
        {
            json.put("tripAdvisor", response.getTripAdvisor().toJSON());
        }
        
        Iterator<Entry<ReviewOption,Integer>> reviewOptionsStatistic = response.getReviewOptionsStatistic().entrySet().iterator();
        JSONArray reviewOptionsStatisticJSON = new JSONArray();
        while(reviewOptionsStatistic.hasNext())
        {
            Entry<ReviewOption,Integer> entry = reviewOptionsStatistic.next();
            ReviewOption reviewOption = entry.getKey();
            JSONObject everyOptionStatistic = new JSONObject(); 
            everyOptionStatistic.put("name", reviewOption.getName());
            everyOptionStatistic.put("value", reviewOption.getValue());
            everyOptionStatistic.put("count", entry.getValue());
            reviewOptionsStatisticJSON.put(everyOptionStatistic);
        }
        json.put("ratingPropertiesStatistic", reviewOptionsStatisticJSON);
        
        Iterator<PoiReview> poiReivews = response.getPoiReviewList().iterator();
        JSONArray poiReivewsJSON = new JSONArray();
        while(poiReivews.hasNext())
        {
            poiReivewsJSON.put(poiReivews.next().toJSON());
        }
        json.put("reviewList", poiReivewsJSON);
        return json.toString();
    }

}
