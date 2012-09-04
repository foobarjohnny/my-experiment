/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlAdsRequest;
import com.telenav.cserver.html.util.HtmlConstants;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  chfzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlAdsParser extends HtmlProtocolRequestParser{

	@Override
	public String getExecutorType() {
		// TODO Auto-generated method stub
		return "adsinfo";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		HtmlAdsRequest request = new HtmlAdsRequest();
		
		//get the isDummy
		String isDummy = httpRequest.getParameter("isDummy");
		if("true".equals(isDummy)){
			request.setDummy(true);
		}else{
			request.setDummy(false);
		}
		
		// get the operate type
		String operateType = HtmlCommonUtil.getString(httpRequest.getParameter("operateType"));
	    if( operateType == null || "".equals(operateType) ){
	        operateType = HtmlConstants.OPERATE_ADSVIEW_BASIC;
	    }
	    request.setOperateType(operateType);
	    
	    // get the ads id
        String adsId = httpRequest.getParameter("adsId");
        if(adsId == null||"".equals(adsId)){
            adsId = "0";
        }
        request.setAdId(Long.parseLong(adsId));
	           	    
	    if( HtmlConstants.OPERATE_ADSVIEW_FETCH_DETAIL_DATA.equals(operateType) ){
	        String logoHeight = HtmlCommonUtil.getString(httpRequest.getParameter("logoHeight"));
	        String logoWidth = HtmlCommonUtil.getString(httpRequest.getParameter("logoWidth"));
	        request.setLogoHeight(logoHeight);
	        request.setLogoWidth(logoWidth);
	    }
	    
	    if(HtmlConstants.OPERATE_ADSVIEW_BASIC.equals(operateType)){
	        //get the device height and width
	        String deviceHeight = HtmlCommonUtil.getString(httpRequest.getParameter("height"));
	        String deviceWidth = HtmlCommonUtil.getString(httpRequest.getParameter("width"));
	        if(deviceHeight==null||"".equals(deviceHeight)){
	            deviceHeight = "800";
	        }
	        if(deviceHeight==null||"".equals(deviceHeight)){
	            deviceWidth = "480";
	        }

	        request.setDeviceHeight(deviceHeight);
	        request.setDeviceWidth(deviceWidth);
	        
	        int logoHeight = (Integer.parseInt(deviceHeight)/10)/8*7;
	        
	        request.setLogoHeight(""+logoHeight);
	        request.setLogoWidth(""+logoHeight);

	    }
	    
		return request;
	}

}
