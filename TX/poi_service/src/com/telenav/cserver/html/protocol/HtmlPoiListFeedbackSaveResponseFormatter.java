/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author pzhang
 * @version 1.0, 2009-5-25
 */
public class HtmlPoiListFeedbackSaveResponseFormatter extends
        HtmlProtocolResponseFormatter {
    private static Logger log = Logger
            .getLogger(HtmlWeatherResponseFormatter.class);

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {

		JSONObject ajaxResponse = new JSONObject();
		httpRequest.setAttribute("ajaxResponse", ajaxResponse.toString());
    }
}
