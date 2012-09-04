/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.ac.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.poi.struts.util.PoiUtil;

/**
 * This is for different instance before browser core provide "#" to solve this issue. 
 * 
 * @author pzhang
 * @version 1.0, 2009-5-12
 */
public class SelectAddress extends PoiBaseAction{
    
    public ActionForward doAction(ActionMapping mapping,
            HttpServletRequest httpRequest, HttpServletResponse response)
            throws Exception {
        try
        {
            /**
             * from:
             * Common
             * DriveTo
             * MyFavorite
             * EditRoute
             * ShareAddress
             * Map
             * POI
             * 
             * 
             */
            String page = PoiUtil.filterLastPara(httpRequest.getParameter("jsp"));
            String from = PoiUtil.filterLastPara(httpRequest.getParameter("from"));
            httpRequest.setAttribute("from", from);

            String path = page;

            //httpRequest.setAttribute("DataHandler", handler);
            return mapping.findForward(path);
        }
        catch(Exception e)
        {
            return mapping.findForward("Globe_Exception");
        } 

    }
}
