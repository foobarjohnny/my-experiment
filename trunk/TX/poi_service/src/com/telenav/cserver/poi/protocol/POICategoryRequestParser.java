/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: PoiCategoryRequestParser.java
 * Package name: com.telenav.cserver.poi.protocol
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 2:46:30 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.POICategoryRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 2:46:30 PM
 */
public class POICategoryRequestParser extends BrowserProtocolRequestParser {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#getExecutorType
     * ()
     */
    @Override
    public String getExecutorType() {
        return "POICategory";
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#
     * parseBrowserRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest request) {
    	POICategoryRequest pOICategoryRequest = new POICategoryRequest();
    	
    	DataHandler handler = (DataHandler) request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
    	pOICategoryRequest.setPoiFinderVersion(TnUtil.getPoiFinderVersion(PoiUtil.getUserProfile(handler)));
    	
    	return pOICategoryRequest;
    }

}
