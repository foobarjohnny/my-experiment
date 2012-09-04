package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.tnbrowser.util.DataHandler;

public class ToolsMainAction extends PoiBaseAction {
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        String product = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);

		System.out.println("device="+ device);
		String forwardUrl = "success";
		if(TnUtil.isSprintUser(carrier)){
			if(device.equals("8530")){
				forwardUrl = "successBadge";
			}
		}
		else if(TnUtil.isATT(product)){
			if(device.equals("9000")){
				forwardUrl = "successBadge";
			}       	
        }
        
        return mapping.findForward(forwardUrl);
    }
}
