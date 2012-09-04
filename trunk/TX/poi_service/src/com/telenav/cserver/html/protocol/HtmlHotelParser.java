/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.html.executor.HtmlHotelRequest;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class HtmlHotelParser extends HtmlProtocolRequestParser {

	@Override
	public String getExecutorType() {
		// TODO Auto-generated method stub
		return "getHotelDetailData";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		HtmlHotelRequest request = new HtmlHotelRequest();

		String jsonString = httpRequest.getParameter("jsonStr");
		JSONObject json = new JSONObject(jsonString);
		request.setPoiId(Long.parseLong(json.getString("poiId")));
		request.setDummy(Boolean.parseBoolean(json.getString("isDummy")));
		return request;
	}

}
