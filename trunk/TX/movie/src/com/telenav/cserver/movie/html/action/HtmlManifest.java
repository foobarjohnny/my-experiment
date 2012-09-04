package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.action.HtmlMovieBaseAction;

public class HtmlManifest extends HtmlMovieBaseAction {
	private static final Logger logger = Logger.getLogger(HtmlManifest.class);
	
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
