/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.struts.action.v20;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.exception.TnException;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.misc.executor.v20.SentAddressResponse;
/**
 * @author pzhang
 * This is the controller class for Sent Address function
 * @version 1.0, 2009-3-12
 * @version 2.0, 2009-5-11
 */
public class SentAddressAction extends PoiBaseAction{
	
    /**
     * 
     */
    protected ActionForward doAction(ActionMapping mapping,HttpServletRequest request,
            HttpServletResponse response) throws TnException {

        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            SentAddressResponse executorResponse = new SentAddressResponse();

            if (executorRequests != null && executorRequests.length > 0) {
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }

            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            return mapping.findForward(executorResponse.getPath());
        } catch (Exception e) {
            return mapping.findForward("Globe_Exception");
        } 
    }
}
