/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.weather.executor.WeatherResponse;

/**
 * @author pzhang
 * This is the controller class for Weather function
 * It will get the weather information base on the location user choosed
 * 
 * @version 1.0, 2009-3-2
 * @version 2.0, 2009-5-11
 */
public class WeatherAction extends PoiAjaxAction{
    /**
     * 
     */
    protected ActionForward doAction(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            WeatherResponse executorResponse = new WeatherResponse();

            if (executorRequests != null && executorRequests.length > 0) {
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }

            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            return mapping.findForward("success");
        } catch (Exception e) {
            return mapping.findForward("Globe_Exception");
        } 
    }
}
