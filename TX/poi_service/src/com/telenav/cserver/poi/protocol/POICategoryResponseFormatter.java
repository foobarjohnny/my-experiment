/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: PoiCategoryResponseFormatter.java
 * Package name: com.telenav.cserver.poi.protocol
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 2:48:06 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.POICategoryResponse;
import com.telenav.j2me.datatypes.TxNode;

/**
 * @author lwei (lwei@telenav.cn) 2:48:06 PM
 */
public class POICategoryResponseFormatter extends
        BrowserProtocolResponseFormatter {

    /*
     * (non-Javadoc)
     * 
     * @seecom.telenav.cserver.poi.protocol.BrowserProtocolResponseFormatter#
     * parseBrowserResponse(javax.servlet.http.HttpServletRequest,
     * com.telenav.cserver.framework.executor.ExecutorResponse)
     */
    @Override
    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse responses) {

        POICategoryResponse res = (POICategoryResponse) responses;
        
        TxNode node = new TxNode();
        String poiString = res.getCategoryList().toString();
		node.addMsg(poiString);

        httpRequest.setAttribute("node",node);
    }
}
