/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import com.telenav.cserver.framework.html.struts.HtmlBaseAction;

/**
 * poi base action
 * @author  panzhang@telenav.cn
 * @version 1.0 2011-3-3
 */
public abstract class HtmlPoiBaseAction extends HtmlBaseAction {

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
    
    protected String getModuleName()
    {
    	return "poi_service";
    }
}
