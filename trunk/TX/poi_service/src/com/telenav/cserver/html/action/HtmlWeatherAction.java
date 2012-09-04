/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;

/**
 * This is the controller class for Weather function
 * It will get the weather information base on the location user choosed
 * 
 * @author panzhang@telenav.cn
 * @version 1.0, 2009-3-2
 * @version 2.0, 2009-5-11
 */
public class HtmlWeatherAction extends HtmlPoiBaseAction{
	
	@Override
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
    protected ActionForward doAction(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            I18NWeatherResponse executorResponse = new I18NWeatherResponse();

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
            return mapping.findForward("Globe_Exception");
        } 
    }
}
