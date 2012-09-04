package com.telenav.cserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.telepersonalize.TelepersonalizationFacade;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

public class UpdateUserInfoAction extends PoiAjaxAction {
    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DataHandler handler = (DataHandler) request
                    .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
            Long userId = PoiUtil.getUserId(handler);
            TnContext tc = PoiUtil.getTnContext(handler);
            TxNode body = handler.getAJAXBody();
            String joString = body.msgAt(0);
            JSONObject jo = new JSONObject(joString);
            String firstName = jo.getString("firstName");
            String lastName = jo.getString("lastName");
            String email = jo.getString("email");
            if(null != email){
            	email = email.toLowerCase();
            }

            TxNode node = new TxNode();
            node.addMsg(firstName);
            node.addMsg(lastName);
            node.addMsg(email);

            if (email.equals("")) {
                email = null;
            }

            
            TelepersonalizationFacade.updateUserProfile(userId.toString(), firstName, lastName, email,
                    -1, -1, true, tc);
            
            /*
            TelePersonalize telePersonalize = new TelePersonalize();
            telePersonalize.updateUserInfo(userId.toString(), firstName, lastName, email,
                    -1, -1, false, tc.toContextString());*/

            request.setAttribute("node", node);
            return mapping.findForward("success");
        } catch (Exception e) {
            ActionMessages msgs = new ActionMessages();
            msgs.add("UpdateUserInfofailed", new ActionMessage(
                    "errors.UpdateUserInfo.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }
    }
}
