/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: POICategoryAction.java
 * Package name: com.telenav.cserver.poi.struts.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 3:03:30 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.POICategoryResponse;

/**
 * @author lwei (lwei@telenav.cn) 3:03:30 PM
 */
public class POICategoryAction extends PoiBaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.poi.struts.action.BaseAction#doAction(org.apache.
     * struts.action.ActionMapping, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
       
        ExecutorRequest[] executorRequests = requestParser.parse(request);
        POICategoryResponse executorResponse = new POICategoryResponse();

        ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
        ac
                .execute(executorRequests[0], executorResponse,
                        new ExecutorContext());

        responseFormatter.format(request,
                new ExecutorResponse[] { executorResponse });
        
        return mapping.findForward("success");
    }

}
