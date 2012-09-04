/*
 * Copyright (c) 2011 TeleNav, Inc
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
import com.telenav.cserver.weather.executor.I18NWeatherResponse;

/**
 * @author Derek Liu
 *
 * @version 1.0, 2011-3-1
 */
public class I18NWeatherAction extends PoiAjaxAction{
    /**
     * 
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

            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            return mapping.findForward("success");
        } catch (Exception e) {
            return mapping.findForward("Globe_Exception");
        } 
    }
}
