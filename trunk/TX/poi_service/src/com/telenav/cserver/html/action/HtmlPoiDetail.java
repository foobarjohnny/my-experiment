/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;

/**
 * poidetail action
 * @author  panzhang@telenav.cn
 * @version 1.0 2011-3-3
 */
public class HtmlPoiDetail extends HtmlPoiBaseAction {
	
	/**
	 * return poidetail screen
	 */
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	String dummyData = HtmlCommonUtil.filterLastPara(request.getParameter("dummyData"));
    	request.setAttribute("dummyData", dummyData);
    	
        return mapping.findForward("success");
    }
}
