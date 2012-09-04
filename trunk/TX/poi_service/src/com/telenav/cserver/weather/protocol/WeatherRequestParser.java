/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.JsonUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.weather.executor.WeatherRequest;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class WeatherRequestParser extends BrowserProtocolRequestParser{
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#getExecutorType
     * ()
     */
    @Override
    public String getExecutorType() {
        return "Weather";
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#
     * parseBrowserRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) 
        throws Exception {
        
        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        TxNode body = handler.getAJAXBody();
        String jsonStr = body.msgAt(0);
        JSONObject jo = new JSONObject(jsonStr);

        String addressString = JsonUtil.getString(jo, "addressString");
        JSONObject addressJO =  new JSONObject(addressString);
        Stop address = PoiUtil.convertAddress(addressJO);
        
        WeatherRequest request = new WeatherRequest();
        request.setLocation(address);
        request.setCanadianCarrier(TnUtil.isCanadianCarrier(handler));
        
        return request;
    }
}
