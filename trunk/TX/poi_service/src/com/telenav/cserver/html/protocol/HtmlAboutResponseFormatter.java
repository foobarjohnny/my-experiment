/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.html.executor.HtmlAboutResponse;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class HtmlAboutResponseFormatter extends HtmlProtocolResponseFormatter {
	private static Logger logger = Logger.getLogger(HtmlAboutResponseFormatter.class);
	
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		HtmlAboutResponse aboutResponse = (HtmlAboutResponse) executorResponse;
		
		String dataSet = aboutResponse.getDataSet();
		
		logger.info("dataSet: ["+dataSet+"]");
		httpRequest.setAttribute("ajaxResponse", dataSet);
	}
}
