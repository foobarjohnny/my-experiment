package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONObject;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.tnbrowser.util.DataHandler;

public class StartUpAction extends PoiBaseAction {
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		DataHandler handler = (DataHandler) request
				.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

		if (PoiUtil.needsBrandListWhenStartUp(handler)) {
			PoiUtil.loadHotBrand(request);
		}

		//ACTemplates is not used anymore, but since StartUp.jsp still has this variable, so we can't remove them 
        JSONObject templates = new JSONObject();
        JSONObject template = new JSONObject();
        template.put("dummy", "dummydata");
        templates.put("US", template);
        request.setAttribute("ACTemplates", templates);

		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
		String product = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
		String versionNo = handler.getClientInfo(DataHandler.KEY_VERSION);
		String forwardUrl = "success";
		if (TnUtil.isBoostRIM6001User(handler))
		{
			forwardUrl = "successTNBoostRIM";
		}
		else if (TnUtil.isSprintUser(carrier) || TnUtil.isTMOAndroidUser(handler) || TnUtil.isBoostNotTN60(carrier, versionNo)  || TnUtil.isVNAndroidUser(handler)) {
			forwardUrl = "successSN";
			if(device.equals("8530")){
				forwardUrl = "successSNBadge";
			}
		}
		else if (TnUtil.isVNRIM62(handler) || TnUtil.isRIM64(handler)) {
		    forwardUrl = "successSN";
		}
		else if(TnUtil.isATT(product)){
			if (TnUtil.isATTRIM63(handler)) {
                forwardUrl = "successRIM63";
            }else if(device.equals("9000")){
                forwardUrl = "successBadge";
            }  
        }else if(TnUtil.isTMORIM62(handler))
        {
        	forwardUrl = "successTMORIM62";
        }

        return mapping.findForward(forwardUrl);
    }
}
