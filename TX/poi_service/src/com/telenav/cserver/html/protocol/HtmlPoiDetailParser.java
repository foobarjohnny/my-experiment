/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.html.executor.HtmlPoiDetailRequest;
import com.telenav.cserver.html.util.HtmlConstants;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  panzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlPoiDetailParser extends HtmlProtocolRequestParser{

	@Override
	public String getExecutorType() {
		// TODO Auto-generated method stub
		return "getPoiDetailData";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		HtmlPoiDetailRequest request = new HtmlPoiDetailRequest();
		
		String operateType = httpRequest.getParameter("operateType");
		request.setOperateType(operateType);
		
		
		String jsonString = httpRequest.getParameter("jsonStr");
        JSONObject json = new JSONObject(jsonString);
        long poiId = 0;
        try
		{
        	poiId = Long.parseLong(json.getString("poiId"));
		}
		catch(Exception e)
		{
			
		}
        request.setPoiId(poiId);
		if(HtmlConstants.OPERATE_POIDETAIL_MAIN.equals(operateType) || HtmlConstants.OPERATE_POIDETAIL_MENU.equals(operateType))
		{
			request.setWidth(json.getString("width"));
			request.setHeight(json.getString("height"));
			request.setMenuWidth(json.getString("menuWidth"));
			request.setMenuHeight(json.getString("menuHeight"));
		}
		
		if(HtmlConstants.OPERATE_POIDETAIL_MAINNEW.equals(operateType))
		{
			request.setCategoryId(json.getString("categoryId"));
			
			long adsId = 0;
			try
			{
				adsId = Long.parseLong(json.getString("adsId"));
			}
			catch(Exception e)
			{
				
			}
			request.setAdsId(adsId);
			
    		if(0 != adsId)
    		{
    			request.setAdsRequest(true);
    		}
    		//
    		if(0 != request.getPoiId())
    		{
    			request.setPoiRequest(true);
    		}
		}
		
		return request;
	}

}
