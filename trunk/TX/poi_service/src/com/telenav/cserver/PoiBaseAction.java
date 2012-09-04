/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2009-12-3
 * File name: PoiBaseAction.java
 * Package name: com.telenav.cserver
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 03:39:07 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver;

import com.telenav.cserver.browser.framework.struts.BaseAction;

/**
 * @author lwei (lwei@telenav.cn) 03:39:07 PM
 */
public abstract class PoiBaseAction extends BaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.browser.framework.struts.BaseAction#getCliApplicationID
     * ()
     */
    protected String getCliApplicationID() {
        return "bs_poi";
    }

}
