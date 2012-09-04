/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.executor.HtmlAdJugglerResponse;

/**
 * This class is not used any more
 * @author  chbzhang@telenav.cn
 * @version 1.0 2012-02-21
 */

public class HtmlAdJugglerAction extends HtmlPoiBaseAction {
	
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
	protected final ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse arg2)
			throws Exception {
//		try {
//			ExecutorRequest[] executorRequests = requestParser.parse(request);
//			HtmlAdJugglerResponse executorResponse = new HtmlAdJugglerResponse();
//			if (executorRequests != null && executorRequests.length > 0) {
//				ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
//				ac.execute(executorRequests[0], executorResponse,
//						new ExecutorContext());
//			}
//
//			if (executorResponse.getStatus() == ExecutorResponse.STATUS_FAIL) {
//				request.setAttribute("errorMsg", executorResponse
//						.getErrorMessage());
//				return mapping.findForward("failure");
//			}
//
//			responseFormatter.format(request,
//					new ExecutorResponse[] { executorResponse });
//			return mapping.findForward("success");
//		} catch (Exception e) {
//			e.printStackTrace();
//			ActionMessages msgs = new ActionMessages();
//			msgs.add("adjugglerFailed", new ActionMessage("errors.adjuggler.failed"));
//			addErrors(request, msgs);
//			return mapping.findForward("failure");
//		}
		return null;
	}
}
