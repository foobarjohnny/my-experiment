/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 3, 2008
 * File name: Startup.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 7:46:10 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 7:46:10 PM, Nov 3, 2008
 */
public class Startup extends BrowserBaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.j2me.browser.action.BrowserBaseAction#doAction(org.apache
     * .struts.action.ActionMapping, com.telenav.tnbrowser.util.DataHandler,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) {
        // Clear Search.
        handler.removeShareData(Constants.MOVIES_DATE);
        handler.removeShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NAME);
        handler.removeShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NEWOPENING);
        handler.removeShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_RATING);
        handler.removeShareData(Constants.SEARCH_CIRCLE);
        handler.removeShareData(Constants.THEATER_ID_ARRAY);
        handler.removeShareData(Constants.SEARCH_LOCATION);

        // Initialize date.
        String dateStr = Constants.DATE_FORMAT.format(new Date());
        request.setAttribute("date", dateStr);

        return mapping.findForward("success");
    }

}
