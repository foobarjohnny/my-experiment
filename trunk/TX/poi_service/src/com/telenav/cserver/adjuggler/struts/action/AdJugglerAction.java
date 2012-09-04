package com.telenav.cserver.adjuggler.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.adjuggler.executor.AdJugglerResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * 
 * @author chbzhang
 * 
 */

public class AdJugglerAction extends PoiAjaxAction {
	
	private Logger logger = Logger.getLogger(AdJugglerAction.class);
	
	protected final ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse arg2)
			throws Exception {
		try {
			ExecutorRequest[] executorRequests = requestParser.parse(request);
			AdJugglerResponse executorResponse = new AdJugglerResponse();
			if (executorRequests != null && executorRequests.length > 0) {
				ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
				ac.execute(executorRequests[0], executorResponse,
						new ExecutorContext());
			}

			if (executorResponse.getStatus() == ExecutorResponse.STATUS_FAIL) {
				request.setAttribute("errorMsg", executorResponse
						.getErrorMessage());
				return mapping.findForward("failure");
			}

			responseFormatter.format(request,
					new ExecutorResponse[] { executorResponse });
			return mapping.findForward("success");
		} catch (Exception e) {
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
			e.printStackTrace();
			ActionMessages msgs = new ActionMessages();
			msgs.add("adjugglerFailed", new ActionMessage("errors.adjuggler.failed"));
			addErrors(request, msgs);
			return mapping.findForward("failure");
		}

	}
}
