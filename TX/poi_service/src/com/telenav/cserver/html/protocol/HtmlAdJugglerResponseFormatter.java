/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.html.executor.HtmlAdJugglerResponse;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class HtmlAdJugglerResponseFormatter extends
		HtmlProtocolResponseFormatter {

	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		HtmlAdJugglerResponse adJugglerResponse = (HtmlAdJugglerResponse) executorResponse;
		String result = adJugglerResponse.getResult();
		httpRequest.setAttribute("ajaxResponse", result);
	}
}
