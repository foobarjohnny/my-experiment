package com.telenav.cserver.poi.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;

public class MainPageAction extends PoiBaseAction{
	
	public ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("success");
	}
}
