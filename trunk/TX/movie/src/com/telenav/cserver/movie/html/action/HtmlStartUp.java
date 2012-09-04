package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;

public class HtmlStartUp extends HtmlMovieBaseAction {
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("success");
    }
}
