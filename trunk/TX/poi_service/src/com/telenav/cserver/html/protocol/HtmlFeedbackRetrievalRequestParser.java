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
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalRequest;
import com.telenav.cserver.util.TnUtil;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */

public class HtmlFeedbackRetrievalRequestParser extends HtmlProtocolRequestParser{
	
	private static Logger logger = Logger.getLogger(HtmlFeedbackRetrievalRequestParser.class);

	@Override
	public String getExecutorType() {
		return "GenericFeedbackRetrieval";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest request)
			throws Exception {
			GenericFeedbackRetrievalRequest req = new GenericFeedbackRetrievalRequest();
			HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	    	String pageName = TnUtil.filterLastPara(request.getParameter("jsp"));
	    	if("".equals(pageName))
	    	{
	    		pageName = "generic";
	    	}
	    	String feedbackTopic = getTopic(pageName);
	        int pageSize = 20;
	        int pageNumber = 0;
	    	logger.info("feedbackTopic: [" + feedbackTopic + "], pageName:[" + pageName + "]");
	    	
	    	req.setFeedbackTopic(feedbackTopic);
	    	req.setPageName(pageName);
	    	req.setPageNumber(pageNumber);
	    	req.setPageSize(pageSize);
	    	req.setLocale(clientInfo.getLocale());
	    	return req;
	}
	
	/**
	 * get the FeedBack topic
	 * @param pageName
	 * @return
	 */
	private String getTopic(String pageName)
	{
		String topic = "";
		if("poilist".equals(pageName))
		{
			//poi list feedback
			topic = "SEARCHFEEDBACKV2";
		}
		else if("poidetail".equals(pageName))
		{
			//poi detail feedback
			topic = "POIFEEDBACKV2";
		}
		else
		{
			//generic feedback
			topic = "GENERALFEEDBACKV2";
		}
		return topic;
	}
	
}