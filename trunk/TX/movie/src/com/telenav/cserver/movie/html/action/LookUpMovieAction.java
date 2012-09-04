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
import com.telenav.cserver.movie.html.executor.MovieListResponse;

public class LookUpMovieAction extends HtmlMovieBaseAction {
    private static final Logger logger = Logger.getLogger(MovieListAction.class);
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            MovieListResponse executorResponse = new MovieListResponse();

            if (executorRequests != null && executorRequests.length > 0) {
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
            return mapping.findForward("success");
        } catch (Exception e) {
            ActionMessages msgs = new ActionMessages();
            msgs.add("loginfailed", new ActionMessage("errors.movie.failed"));
            addErrors(request, msgs);
            logger.error("MovieListAction#doAction",e);
            return mapping.findForward("failure");
        }
    }

}
