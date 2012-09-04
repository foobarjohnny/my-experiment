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
import com.telenav.cserver.poi.executor.EditPOIReponse;

public class EditPOIStrutsAction extends PoiAjaxAction {

	public EditPOIStrutsAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

        try {
        	
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            System.out.println("Got the message before response");
            EditPOIReponse executorResponse = new EditPOIReponse();
            System.out.println("Got the message after response");

            if (executorRequests != null && executorRequests.length > 0) {
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }
            System.out.println("Got the message afater extreafaf response");


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
