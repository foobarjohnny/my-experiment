/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.html.executor.GetRestaurantDetailRequest;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class GetRestaurantDetailRequestParser extends HtmlProtocolRequestParser {

	@Override
	public String getExecutorType() {
		return "getHtmlRestaurantDetail";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		GetRestaurantDetailRequest req = new GetRestaurantDetailRequest();

		req.setPartnerPoiId(Long.parseLong(httpRequest
				.getParameter("partnerId")));

		System.out.println(httpRequest.getParameter("partnerId"));

		return req;
	}
}
