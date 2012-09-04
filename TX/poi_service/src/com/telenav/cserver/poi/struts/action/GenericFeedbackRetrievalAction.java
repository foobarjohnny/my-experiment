package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalRequest;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalResponse;

public class GenericFeedbackRetrievalAction extends PoiAjaxAction{

	private static Logger logger = Logger.getLogger(GenericFeedbackRetrievalAction.class);
	
	@Override
	protected ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
            logger.info("POIFeedbackRetrieval... before");
            
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            GenericFeedbackRetrievalResponse executorResponse = new GenericFeedbackRetrievalResponse();

            String success = null;
            
            if (executorRequests != null && executorRequests.length > 0) {
            	GenericFeedbackRetrievalRequest retrievalRequest = (GenericFeedbackRetrievalRequest)executorRequests[0];
                String pageName = retrievalRequest.getPageName();
                
                success = "successFor" + pageName;
                
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }

            if (executorResponse.getStatus() == ExecutorResponse.STATUS_FAIL) {
                request.setAttribute("errorCode", new Long(-1));
                request.setAttribute("errorMsg", executorResponse.getErrorMessage());
                return mapping.findForward("failure");
            }

            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            logger.info("POIFeedbackRetrieval... after");
            if(null != success){
            	logger.info("Forward to page " + success);
            	return mapping.findForward(success);
            }else{
            	logger.info("Forward to failure page");
            	return mapping.findForward("failure");
            }
        } catch (Exception e) {
            ActionMessages msgs = new ActionMessages();
            msgs.add("loginfailed", new ActionMessage("errors.poi.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }
	}

}
