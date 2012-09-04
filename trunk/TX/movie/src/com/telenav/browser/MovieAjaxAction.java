/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2009-12-3
 * File name: ContentAjaxAction.java
 * Package name: com.telenav.cserver
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 03:39:07 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser;

import com.telenav.cserver.browser.framework.struts.AjaxAction;


/**
 * @author lwei (lwei@telenav.cn) 03:39:07 PM
 */
public abstract class MovieAjaxAction extends AjaxAction {



    /* (non-Javadoc)
     * @see com.telenav.cserver.browser.framework.struts.BaseAction#getCliApplicationID()
     */
    @Override
    protected String getCliApplicationID() {
        return "bs_movie";
    }

}
