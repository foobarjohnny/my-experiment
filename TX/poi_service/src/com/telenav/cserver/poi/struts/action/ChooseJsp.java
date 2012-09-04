package com.telenav.cserver.poi.struts.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.cserver.poi.holder.PoiCategoryCollection;
import com.telenav.cserver.poi.holder.PoiCategoryItem;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;

/**
 * Go to corresponding jsp
 * 
 * @author chbzhang
 * 
 */
public class ChooseJsp extends PoiBaseAction {
	public ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String value = request.getParameter("jsp").split(";")[0];
		
		DataHandler handler = (DataHandler) request
				.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

		if ("SearchPoi".equalsIgnoreCase(value)) 
		{   if(!PoiUtil.needsBrandListWhenStartUp(handler)){
			   PoiUtil.loadHotBrand(request);
		    }
			else
			{
				//get hot poi category data
				PoiCategoryCollection poiCollection = new PoiCategoryCollection();
				List<PoiCategoryItem> hotList;
				UserProfile userProfile = TnUtil.getUserProfile(handler);
				hotList = poiCollection.getPoiHotList(userProfile);
//				if(TnUtil.isMMICarrier(handler))
//				{
//					hotList = poiCollection.getPoiHotListForMMI();
//				}
//				else
//				{
//					hotList = poiCollection.getPoiHotList();
//				}
				
				/*
				List<PoiCategoryItem> hotList = PoiCategoryManager.getInstance().getPoiHotList(tncontext, handler);*/
				request.setAttribute("hotCategoryData", hotList);
			}
		}
		
		// do some hardcode logic here
		if ("PoiList".equalsIgnoreCase(value)) {
			if (PoiUtil.isAndroid61(handler)) {
				value = "PoiListAndroid";
			}
			if(PoiUtil.isRimTouch(handler))
        	{
        		value = "PoiListAndroid";
        	}
		}
		else if("ShowDetail".equalsIgnoreCase(value)){
        	if(PoiUtil.isAndroid61(handler))
        	{
        		value = "ShowDetailAndroid";
        	}
        	if(PoiUtil.isRimTouch(handler))
        	{
        		value = "ShowDetailAndroid";
        	}
		}
		else if ("AboutSupport".equalsIgnoreCase(value)){
			if(TnUtil.isBell_VMC(handler)){
				value = "AboutSupportForBell_VMC";
			}
			else if(TnUtil.isTelcelRIM64(handler))
			{
				value = "AboutSupportForTelcel";
			}
			
		}
		else if ("ToolsMain".equalsIgnoreCase(value)) {
		    if(TnUtil.isSprintRim62(handler)||TnUtil.isVNRIM62(handler)) {
		        value = "NewToolsMain";
		    }
		}
		
		return mapping.findForward(value);
	}
}
