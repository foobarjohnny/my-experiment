/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.executor.BookTicketResponse;

/**
 * BookTicketAction.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 Mar 9, 2011
 * 
 */
public class BookTicketAction extends HtmlMovieBaseAction {

	private static Logger logger = Logger.getLogger(BookTicketAction.class);

	@Override
	protected ActionForward doAction(ActionMapping mapping,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {
		try {
			ExecutorRequest[] executorRequests = this.requestParser
					.parse(httpRequest);
			ExecutorResponse executorResponse = new BookTicketResponse();
			if (executorRequests != null && executorRequests.length > 0) {
				ExecutorDispatcher.getInstance().execute(executorRequests[0],
						executorResponse, new ExecutorContext());
			}

			if (executorResponse.getStatus() == ExecutorResponse.STATUS_FAIL) {
				httpRequest.setAttribute("errorCode", new Long(-1));
				httpRequest.setAttribute("errorMsg", executorResponse
						.getErrorMessage());
				return mapping.findForward("failure");
			}

			this.responseFormatter.format(httpRequest,
					new ExecutorResponse[] { executorResponse });
			return mapping.findForward("success");
		} catch (Exception e) {
			logger.error("BookTicketAction#doAction", e);
			ActionMessages msgs = new ActionMessages();
			msgs.add("loginfailed", new ActionMessage("errors.movie.failed"));
			addErrors(httpRequest, msgs);
			return mapping.findForward("failure");
		}
	}

}
