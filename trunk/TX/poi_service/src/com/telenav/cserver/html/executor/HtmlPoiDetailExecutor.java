/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;
import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @TODO	Call the executor to implement business logic
 * @author  panzhang@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiDetailExecutor extends AbstractExecutor 
{

	public static boolean isDebug = false;
    private static final Logger logger = Logger.getLogger(HtmlPoiReviewExecutor.class);
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
	    
		
		HtmlServiceProxyDummy dummy = new HtmlServiceProxyDummy();
		
		
		TnContext tc = context.getTnContext();
		HtmlPoiDetailRequest poiRequest = (HtmlPoiDetailRequest)request;
		HtmlPoiDetailResponse poiResponse = (HtmlPoiDetailResponse)response;
		poiResponse.setOperateType(poiRequest.getOperateType());
	    if(HtmlConstants.OPERATE_POIDETAIL_MAIN.equals(poiRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
	    		poiResponse = dummy.getDummyPoiDetails(poiRequest,poiResponse,tc);
	    	}
	    	else
	    	{
	    		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getPoiDetails(poiRequest,poiResponse,tc);
	    	}
	    }
	    else if(HtmlConstants.OPERATE_POIDETAIL_EXTRA.equals(poiRequest.getOperateType()) )
	    {
	    		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getPoiExtras(poiRequest,poiResponse,tc);
	    }
	    else if(HtmlConstants.OPERATE_POIDETAIL_MENU.equals(poiRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
	    		poiResponse = dummy.getDummyPoiMenu(poiRequest,poiResponse,tc);
	    	}
	    	else
	    	{
	    		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getPoiMenu(poiRequest,poiResponse,tc);
	    	}
	    }
	    else if(HtmlConstants.OPERATE_POIDETAIL_ADSPOI.equals(poiRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
	    		poiResponse = dummy.getDummyOrganicAds(poiRequest,poiResponse,tc);
	    	}
	    	else
	    	{
	    		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getOrganicAds(poiRequest,poiResponse,tc);
	    	}
	    }
	    else if(HtmlConstants.OPERATE_POIDETAIL_MAINNEW.equals(poiRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
	    		poiResponse = dummy.getDummyPoiDetailsNew(poiRequest,poiResponse,tc);
	    	}
	    	else
	    	{
	    		poiResponse.setAdsRequest(poiRequest.isAdsRequest());
	    		poiResponse.setPoiRequest(poiRequest.isPoiRequest());
	    		
	    		//check to call ads server or cose server
	    		//
    			HtmlAdsRequest adsRequest = new HtmlAdsRequest();
	    		HtmlAdsResponse adsResponse = new HtmlAdsResponse();
	    		
	    		if(poiRequest.isAdsRequest())
	    		{
	    			adsRequest.setFromPoiDetail(true);
		    		adsRequest.setAdId(poiRequest.getAdsId());
	    			adsResponse = HtmlAdsServiceProxy.getInstance().getAdsDetailData(adsRequest, adsResponse, tc);
	    		}
	    		//
	    		//
	    		if(poiRequest.isPoiRequest())
	    		{	
	    			poiResponse = HtmlPoiDetailServiceProxy.getInstance().getPoiDetailsNew(poiRequest,poiResponse,tc);
	    		}
	    		
	    		if(poiRequest.isAdsRequest() && poiRequest.isPoiRequest())
	    		{
	    			//combine two result
	    			combineToPoiResponse(adsResponse,poiResponse);
	    		}
	    		
	    		if(poiRequest.isAdsRequest() && !poiRequest.isPoiRequest())
	    		{
	    			//convert ads to poi
	    			convertToPoiResponse(adsResponse,poiResponse);
	    		}	
	    	}
	    }
	    else if(HtmlConstants.OPERATE_POIDETAIL_GASPRICE.equals(poiRequest.getOperateType()) )
	    {
	    	if(isDebug)
	    	{
	    		poiResponse = dummy.getDummyGasPrice(poiRequest,poiResponse,tc);
	    	}
	    	else
	    	{
	    		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getGasPrice(poiRequest,poiResponse,tc);
	    	}
	    }
	}
	
	/**
	 * 
	 * @param adsResponse
	 * @param poiResponse
	 */
	private void combineToPoiResponse(HtmlAdsResponse adsResponse,HtmlPoiDetailResponse poiResponse)
	{
		PoiDetail poidetail = poiResponse.getPoiDetail();
		AdsDetail  adsdetail = adsResponse.getAdsDetail();
		if(poidetail == null)
		{
			convertToPoiResponse(adsResponse,poiResponse);
		}
		else
		{
			if(adsdetail != null)
			{
				//set deal from ads service
				if("".equals(poidetail.getDescription()))
				{
					poidetail.setDescription(adsdetail.getTagline());
				}
				if("".equals(poidetail.getLogoName()))
				{
					poidetail.setLogoName(adsdetail.getLogoName());
				}
				poidetail.setAdSource(adsdetail.getAdSource());
				poidetail.setHasDeal(adsdetail.isHasDeal());
				poiResponse.setOfferList(adsdetail.getOfferList());
				
				
				if(adsdetail.isHasPoiMenu() == true)
				{
					poidetail.setHasPoiMenu(true);
					poiResponse.setMenu(adsdetail.getMenu());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param adsResponse
	 * @param poiResponse
	 */
	private void convertToPoiResponse(HtmlAdsResponse adsResponse,HtmlPoiDetailResponse poiResponse)
	{
		PoiDetail poidetail = new PoiDetail();
		AdsDetail  adsdetail = adsResponse.getAdsDetail();
		if(adsdetail == null)
		{
			adsdetail = new AdsDetail();
		}
		else
		{
			poidetail.setDescription(adsdetail.getTagline());
			poidetail.setLogoName(adsdetail.getLogoName());
			poidetail.setAdSource(adsdetail.getAdSource());
			
			poidetail.setHasDeal(adsdetail.isHasDeal());
			poidetail.setHasPoiMenu(adsdetail.isHasPoiMenu());
			
			poiResponse.setPoiDetail(poidetail);
			poiResponse.setMenu(adsdetail.getMenu());
			poiResponse.setOfferList(adsdetail.getOfferList());
		}
	}
}
