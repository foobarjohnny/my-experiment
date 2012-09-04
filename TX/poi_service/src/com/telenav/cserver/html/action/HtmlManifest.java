/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;

/**
 * return manifest screen
 * @author  panzhang@telenav.cn
 * @version 1.0 2011-3-3
 */
public class HtmlManifest extends HtmlPoiBaseAction {
	private static final Logger logger = Logger.getLogger(HtmlManifest.class);
	
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	//generate attribute here and put to share folder
    	String forwardName = HtmlCommonUtil.getString((String) request.getParameter("module"));
    	if("".equals(forwardName))
    	{
    		forwardName = "success";
    	}
    	
    	logger.info("forwardName:" + forwardName);
        return mapping.findForward(forwardName);
    }
}
