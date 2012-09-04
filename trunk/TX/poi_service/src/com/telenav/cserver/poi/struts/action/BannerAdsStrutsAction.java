package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.BannerAdsReponse;
/**
 * @author weiw
 * @date Mar 25, 2010
 *
 */

public class BannerAdsStrutsAction extends PoiAjaxAction {

    @Override
    protected final ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse arg2)
            throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            BannerAdsReponse executorResponse = new BannerAdsReponse();
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
            msgs.add("loginfailed", new ActionMessage("errors.poi.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }

    }

}
