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
 * forwart to a jsp
 * @author  panzhang@telenav.cn
 * @version 1.0 2011-3-3
 */
public class HtmlChooseJsp extends HtmlPoiBaseAction {
	
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
    	String value = HtmlCommonUtil.filterLastPara(request.getParameter("jsp"));

        return mapping.findForward(value);
    }
}
