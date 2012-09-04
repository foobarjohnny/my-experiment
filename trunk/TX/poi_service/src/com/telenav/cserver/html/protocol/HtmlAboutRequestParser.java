/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.executor.HtmlAboutRequest;
import com.telenav.cserver.util.TnUtil;

/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class HtmlAboutRequestParser extends HtmlProtocolRequestParser{
	
	private static Logger logger = Logger.getLogger(HtmlAboutRequestParser.class);

	@Override
	public String getExecutorType() {
		return "HtmlAbout";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest request)
			throws Exception {
			HtmlAboutRequest req = new HtmlAboutRequest();
			
			HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	    	String ssoToken = TnUtil.filterLastPara(request.getParameter("ssoToken"));
	    	
	    	logger.info("ssoToken: ["+ssoToken+"], clientInfo: ["+clientInfo.toString()+"]");
	    	req.setClientInfo(clientInfo);
	    	req.setSsoToken(ssoToken);
	    	return req;
	}

}