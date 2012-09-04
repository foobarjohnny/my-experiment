/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 11, 2009
 * File name: ValidateAddressAction.java
 * Package name: com.telenav.cserver.ac.struts.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 3:44:19 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @author dysong (dysong@telenav.cn) 3:44:19 PM, May 11, 2009
 */
public class ValidateAddressAction extends PoiAjaxAction {

    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            ValidateAddressResponseACEWS executorResponse = new ValidateAddressResponseACEWS();

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
            ActionMessages msgs = new ActionMessages();
            msgs.add("AC failed", new ActionMessage("errors.AC.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }
    }

}
