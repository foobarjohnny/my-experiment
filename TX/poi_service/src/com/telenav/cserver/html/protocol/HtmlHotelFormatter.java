/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.html.datatypes.JSONDataBuilder;
import com.telenav.cserver.html.executor.HtmlHotelResponse;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class HtmlHotelFormatter extends HtmlProtocolResponseFormatter {

	@Override
	public void parseBrowserResponse(HttpServletRequest httpServletRequest,
			ExecutorResponse response) throws Exception {
		HtmlHotelResponse hotelResponse = (HtmlHotelResponse) response;
		httpServletRequest.setAttribute("ajaxResponse", JSONDataBuilder
				.HotelToJSON(hotelResponse.getHotel()).toString());
	}
}
