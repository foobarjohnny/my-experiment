/*
 *  @file BaseAction.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.cserver.browser.framework.struts;

import javax.servlet.http.HttpServletRequest;

import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author zywang
 * @version 1.0 2009-4-14
 */

public abstract class AjaxAction extends BaseAction {
    @Override
    protected DataHandler getHandler(HttpServletRequest request) {
        return new DataHandler(request, true);
    }
}
