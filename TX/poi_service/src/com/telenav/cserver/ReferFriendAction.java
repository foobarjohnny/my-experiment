package com.telenav.cserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.backend.xnav.ReferToFriendRequest;
import com.telenav.cserver.backend.xnav.ReferToFriendResponse;
import com.telenav.cserver.backend.xnav.XnavServiceProxy;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * ReferFriendAction class Refer a Friend to server
 * 
 * @author chbzhang
 */
public class ReferFriendAction extends PoiAjaxAction {

    protected ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DataHandler handler = (DataHandler) request
                    .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
            TnContext tnContext = PoiUtil.getTnContext(handler);
            long userId = PoiUtil.getUserId(handler);
           
            TxNode body = handler.getAJAXBody();

//            TxNode groupsNode = body.childAt(1);
            TxNode contactsNode = body.childAt(0);
            String[] ptns = new String[contactsNode.childrenSize()];
            //IContact[] contacts = new IContact[contactsNode.childrenSize()];
            for (int i = 0; i < contactsNode.childrenSize(); i++) {
                TxNode node = contactsNode.childAt(i);
                ptns[i] = node.msgAt(1);
            }
            ReferToFriendRequest referToFriendRequest = new ReferToFriendRequest();
            referToFriendRequest.setUserId(userId);
            referToFriendRequest.setPtns(ptns);
            referToFriendRequest.setContextString(tnContext.toContextString());
        	
        	ReferToFriendResponse referToFriendResponse = XnavServiceProxy.getInstance().refer2Friends(referToFriendRequest,tnContext);
        	String statusCode = referToFriendResponse.getStatusCode();
        	
            TxNode node = new TxNode();
            request.setAttribute("node", node);
            return mapping.findForward("success");
        } catch (Exception e) {
            ActionMessages msgs = new ActionMessages();
            msgs.add("referFriendfailed", new ActionMessage(
                    "errors.referFriend.failed"));
            addErrors(request, msgs);
            return mapping.findForward("failure");
        }
    }
}
