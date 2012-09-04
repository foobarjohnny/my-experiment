package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalRequest;
import com.telenav.cserver.util.TnUtil;
import com.telenav.tnbrowser.util.DataHandler;

public class GenericFeedbackRetrievalRequestParser extends BrowserProtocolRequestParser{

	private static Logger logger = Logger.getLogger(GenericFeedbackRetrievalRequestParser.class);
	
	@Override
	public String getExecutorType() {
		return "GenericFeedbackRetrieval";
	}
	
	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest request)
			throws Exception {
		
		DataHandler handler = (DataHandler) request
        	.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		
		GenericFeedbackRetrievalRequest req = new GenericFeedbackRetrievalRequest();
    	
		logger.info("Parse request for POIFeedbackRetrieval");
    	
    	String feedbackTopic = TnUtil.filterLastPara(request.getParameter("feedBackTopic"));
    	String pageName = TnUtil.filterLastPara(request.getParameter("pageName"));
        String pageSizeStr = TnUtil.filterLastPara(request.getParameter("pageSize"));
        String pageNumberStr = TnUtil.filterLastPara(request.getParameter("pageNumber"));
    	
        int pageSize = 1;
        try{
        	pageSize = Integer.valueOf(pageSizeStr).intValue();
        }catch(NumberFormatException e){
        	pageSize = 1;
        }
        
        int pageNumber = 1;
        try{
        	pageNumber = Integer.valueOf(pageNumberStr).intValue();
        }catch(NumberFormatException e){
        	pageNumber = 1;
        }
    	logger.info("feedbackTopic: [" + feedbackTopic + "], pageName:[" + pageName + "]," + "pageNumber:[" + pageNumber + "], pageSize:[" + pageSize + "]");
    	
    	req.setFeedbackTopic(feedbackTopic);
    	req.setPageName(pageName);
    	req.setPageNumber(pageNumber);
    	req.setPageSize(pageSize);
    	req.setLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
    	return req;
	}

	
}
