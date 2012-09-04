package com.telenav.browser.movie.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.browser.MovieAjaxAction;
import com.telenav.browser.movie.executor.MovieSearchResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.browser.movie.Constant;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;

public class MovieListAction extends MovieAjaxAction {
	private static Logger logger = Logger.getLogger(MovieListAction.class);
	public static final int PAGE_SIZE = 10;

    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    	DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

    	// get values for current location lat and lon, and also distUnit
    	TxNode node = (TxNode) handler.getParameter("anchorLat");
		long anchorLat = node!=null? node.valueAt(0) : -1;
	    
		node = (TxNode) handler.getParameter("anchorLon");
		long anchorLon = node!=null? node.valueAt(0) : -1;
	    
		node = (TxNode) handler.getParameter("distUnit");
		long distUnit = node!=null? node.valueAt(0) : 1;

		logger.debug("Lat:" + anchorLat + " Lon:" + anchorLon + "Dist Unit:" + distUnit);

		// convert distUnit to mi or km
		String scale = "mi";
		if (distUnit != Constant.DUNIT_MILES) {
	    	scale = "km";
		}

		// set attributes for anchor lat, lon, distUnit and scale
		request.setAttribute("anchorLat", anchorLat);
		request.setAttribute("anchorLon", anchorLon);
		request.setAttribute("distUnit",  distUnit);
		request.setAttribute("scale",     scale);

        try {
        	logger.debug("MovieListAction... before");
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            MovieSearchResponse executorResponse = new MovieSearchResponse();

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
            logger.debug("MovieListAction... after");
            return mapping.findForward("success");
        } catch (Exception e) {
            ActionMessages msgs = new ActionMessages();
            msgs.add("loginfailed", new ActionMessage("errors.movie.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }
    }

}
