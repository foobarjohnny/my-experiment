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
 * choose poi page, this is a test page
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-12-27
 */
public class ChoosePOIAction extends HtmlPoiBaseAction{
	
	@Override
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
	protected ActionForward doAction(ActionMapping mapping,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String poi = request.getParameter("poi");
		String json = request.getParameter("json");
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String clientInfo = request.getParameter("clientInfo");
		
		String url = "/poidetail.do?dummyData=true&poi="+poi;
		if(width != null && !width.equals("")){
			url += "&width="+width;
		}
		if(height!=null && !height.equals("")){
			url += "&height="+height;
		}
		if(clientInfo != null && !clientInfo.equals("")){
			url += "&clientInfo="+clientInfo;
		}
		if(poi.equalsIgnoreCase("custom")){
			request.getSession().setAttribute("CUSTOMJSON", json);
		}
		return new ActionForward(url);
	}
}
