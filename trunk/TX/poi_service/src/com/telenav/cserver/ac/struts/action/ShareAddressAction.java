package com.telenav.cserver.ac.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class ShareAddressAction extends PoiAjaxAction {

    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);

            ShareAddressResponse executorResponse = new ShareAddressResponse();

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
            return mapping.findForward("Globe_Exception");
        }
    }
}
