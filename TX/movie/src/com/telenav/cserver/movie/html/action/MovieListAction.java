package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.executor.MovieListResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;

public class MovieListAction extends HtmlMovieBaseAction {
    private static final Logger logger = Logger.getLogger(MovieListAction.class);
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	String forwardName = "success";
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            MovieListResponse executorResponse = new MovieListResponse();
            if (executorRequests != null && executorRequests.length > 0) {
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }

            if(HtmlConstants.PAGE_TYPE_SIMPLE.equalsIgnoreCase(executorResponse.getPageType()))
            {
            	forwardName = "simpleList";
            }
            else if(HtmlConstants.PAGE_TYPE_SUB.equalsIgnoreCase(executorResponse.getPageType()))
            {
            	forwardName = "subList";
            }
            else if(HtmlConstants.PAGE_TYPE_AJAXSIMPE.equalsIgnoreCase(executorResponse.getPageType()))
            {
            	forwardName = "ajaxSimpleList";
            }
            

            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            return mapping.findForward(forwardName);
        } catch (Exception e) {
            return mapping.findForward(forwardName);
        }
    }

}
