/******************************************************************************
 * Copyright (c) 2010 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2010-12-10
 * File name: HtmlMovieBaseAction.java
 * Package name: com.telenav.cserver.movie.html
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: panzhang(panzhang@telenav.cn) 03:39:07 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.movie.html.action;

import com.telenav.cserver.framework.html.struts.HtmlBaseAction;

/**
 * @author anzhang(panzhang@telenav.cn) 03:39:07 PM
 */
public abstract class HtmlMovieBaseAction extends HtmlBaseAction {



    /* (non-Javadoc)
     * @see com.telenav.cserver.browser.framework.struts.BaseAction#getCliApplicationID()
     */
    @Override
    protected String getCliApplicationID() {
        return "bs_movie";
    }

    protected String getModuleName() {
        return "movie";
    }
}
