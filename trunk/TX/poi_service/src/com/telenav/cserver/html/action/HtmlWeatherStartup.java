/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * About screen action class
 * @author   zhhyan@telenav.cn
 * @version 1.0 2012-02-21
 */
public class HtmlWeatherStartup extends HtmlPoiBaseAction {
	
	@Override
	/**
	 * return to weather screen
	 */
	public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        return mapping.findForward("success");
    }
}
