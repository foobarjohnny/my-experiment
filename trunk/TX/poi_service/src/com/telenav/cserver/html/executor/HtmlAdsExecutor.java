/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;
import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @TODO	Call the executor to implement business logic
 * @author jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 *
 */
public class HtmlAdsExecutor extends AbstractExecutor 
{
    private static final Logger logger = Logger.getLogger(HtmlPoiReviewExecutor.class);
    public static boolean isDebug = false;
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
	    
		TnContext tc = context.getTnContext();
		HtmlAdsRequest adsRequest = (HtmlAdsRequest)request;
		HtmlAdsResponse adsResponse = (HtmlAdsResponse)response;
		
		adsResponse.setAdId(adsRequest.getAdId());
		adsResponse.setOperateType(adsRequest.getOperateType());
		
		isDebug = adsRequest.isDummy();
		
	    if( HtmlConstants.OPERATE_ADSVIEW_BASIC.equals(adsRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
		    	adsResponse = HtmlServiceProxyDummy.getInstance().getDummyAds(adsRequest,adsResponse,tc);
	    	}
	    	else
	    	{
	    		adsResponse = HtmlAdsServiceProxy.getInstance().getAdsBasic(adsRequest, adsResponse, tc);
	    	}

	    }
	    else if( HtmlConstants.OPERATE_ADSVIEW_FETCH_DETAIL_DATA.equals(adsRequest.getOperateType()) )
	    {  
	    	if(isDebug)
	    	{
		    	adsResponse = HtmlServiceProxyDummy.getInstance().getDummyAds(adsRequest,adsResponse,tc);
	    	}
	    	else
	    	{
	    		adsResponse = HtmlAdsServiceProxy.getInstance().getAdsDetailData(adsRequest, adsResponse, tc);
	    	}
	    }else if(  HtmlConstants.OPERATE_ADSVIEW_DETAIL.equals(adsRequest.getOperateType()) ){
	        
	    }
	}
	

}
