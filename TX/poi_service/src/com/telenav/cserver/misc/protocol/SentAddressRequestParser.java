/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.misc.executor.SentAddressRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class SentAddressRequestParser extends BrowserProtocolRequestParser{
    private static Logger log = Logger.getLogger(SentAddressRequestParser.class);
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#getExecutorType
     * ()
     */
    @Override
    public String getExecutorType() {
        return "SentAddress";
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#
     * parseBrowserRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) {
        
        DataHandler handler = new DataHandler(httpRequest);
        SentAddressRequest request = new SentAddressRequest();
        request.setUserId(PoiUtil.getUserId(handler));
        
        String action = TnUtil.filterLastPara(httpRequest.getParameter("action"));
        request.setAction(action);
        log.info("SentAddressRequestParser.parseBrowserRequest(). action is:" + action);
        //delete action
        if("delete".equals(action))
        {
            //get address id
            String id = TnUtil.filterLastPara((String)httpRequest.getParameter("id"));
            log.info("SentAddressRequestParser.delete(). id is:" + id);
            //invokde delete service to delete
            request.setId(id);
        }
        
        return request;
    }
}
