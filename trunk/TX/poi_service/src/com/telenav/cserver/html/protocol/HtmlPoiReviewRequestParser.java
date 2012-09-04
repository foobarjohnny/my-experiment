/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFeatureHelper;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.executor.HtmlPoiReviewRequest;
import com.telenav.cserver.html.util.HtmlConstants;

/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiReviewRequestParser extends HtmlProtocolRequestParser
{

    private static final Logger logger = Logger.getLogger(HtmlPoiReviewRequestParser.class);
    @Override
    public String getExecutorType()
    {
        return "poiReview";
    }

    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpServletRequest) throws Exception
    {
        HtmlPoiReviewRequest poiReviewRequest = new HtmlPoiReviewRequest();
        String operateType = httpServletRequest.getParameter("operateType");
        poiReviewRequest.setOperateType(operateType);
        String jsonString = httpServletRequest.getParameter("jsonStr");
        long poiId = -1;
        int categoryId = -1;
        JSONObject json = new JSONObject(jsonString);
        try
        {
            poiId = Long.parseLong(json.getString("poiId"));
            categoryId = Integer.parseInt(json.getString("categoryId"));
        }
        catch(NumberFormatException e)
        {
        }
        poiReviewRequest.setPoiId(poiId);
        poiReviewRequest.setCategoryId(categoryId);
        
        //view/display addreview
        
        //if yelp supported
        HtmlClientInfo clientInfo = (HtmlClientInfo)httpServletRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
        poiReviewRequest.setIsYelpSupported(isSupportYelpReview(clientInfo));
        
        boolean supportTripAdvisor = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.TRIPADVISOR);
        poiReviewRequest.setTripAdvisorSupported(supportTripAdvisor);
        //submit add review
        if (HtmlConstants.OPERATE_REVIEW_SUBMIT.equals(operateType))
        {
            long userId = -1;
            try
            {
                userId = Long.parseLong(clientInfo.getUserId());
            }
            catch(NumberFormatException e)
            {
            }
            poiReviewRequest.setUserId(userId);
            poiReviewRequest.setUserName(json.getString("reviewerName"));
            poiReviewRequest.setRating(json.getInt("rating"));
            poiReviewRequest.setComments(json.getString("comments"));
            String ratingPropertiesString = json.getString("ratingProperties");
            if (ratingPropertiesString != null)
            {
                JSONArray ratingProperties= new JSONArray(ratingPropertiesString);
               
                for(int i=0; i<ratingProperties.length(); i++)
                {    
                    JSONObject option = ratingProperties.getJSONObject(i); 
                    if ( !"-1".equals(option.getString("value") ) )
                    {
                        poiReviewRequest.addRatingProperty(option.getInt("id"), option.getString("name"), option.getString("value"));
                    }
                }
                
            }
        }
        return poiReviewRequest;
    }
    
    private boolean isSupportYelpReview(HtmlClientInfo clientInfo)
    {
        //Yelp minimum supported version matrix
        //          7.0 7.1 Europa  Luna    Charon
        //Android   No  Yes Yes     N/A     N/A
        //  IOS     N/A N/A N/A     No      Yes

        /*
        String currentVersion = clientInfo.getVersion();
        String comparedVersion;
        if ("android".equalsIgnoreCase(clientInfo.getPlatform()))
        {
            comparedVersion = "7.1";
        }
        else
        {
            comparedVersion = "7.2";
        }
        
        VersionCompare vc = new VersionCompare(currentVersion);
        return vc.compareTo(comparedVersion) >= 0;
        */
        
        boolean supportYelp = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.YELP);
        return supportYelp;
    }
    
    
}
