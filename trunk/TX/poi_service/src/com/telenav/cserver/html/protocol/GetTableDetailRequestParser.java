/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.html.executor.GetTableDetailRequest;
import com.telenav.cserver.html.util.HtmlConstants;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class GetTableDetailRequestParser extends HtmlProtocolRequestParser {

	public String getExecutorType() {
		return "getHtmlTableDetail";
	}

	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
		GetTableDetailRequest req = new GetTableDetailRequest();

		JSONObject json = new JSONObject(httpRequest.getParameter("jsonStr"));

		req.setPartnerPoiId(json.getLong(HtmlConstants.RRKey.RESTAURANT_PARTNER_POI_ID));
		req.setPartySize(json.getInt(HtmlConstants.RRKey.PARTY_SIZE));
		req.setSearchTime((String) json.getString(HtmlConstants.RRKey.SEARCH_REQUEST_TIME));
		req.setSearchDate((String) json.getString(HtmlConstants.RRKey.SEARCH_REQUEST_DATE));

		return req;

	}
}
