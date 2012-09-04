package com.telenav.cserver.onebox.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.PoiAjaxAction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
/**
 * @author weiw
 * @date Mar 25, 2010
 *
 */

public class OneBoxSearchAction extends PoiAjaxAction {
	private Logger logger = Logger.getLogger(OneBoxSearchAction.class);

    @Override
    protected final ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse arg2)
            throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            OneBoxResponse executorResponse = new OneBoxResponse();
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
        	logger.error("OneBoxSearchAction is failed!!! mapping will forward to failure");
        	e.printStackTrace();
            ActionMessages msgs = new ActionMessages();
            msgs.add("oneboxfailed", new ActionMessage("errors.oneBox.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }

    }

}
