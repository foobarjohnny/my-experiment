/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 8, 2009
 * File name: ValidateAddressResponseFormatter.java
 * Package name: com.telenav.cserver.ac.protocol
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:06:04 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;

/**
 * @author dysong (dysong@telenav.cn) 5:06:04 PM, May 8, 2009
 */
public class ShareAddressResponseFormatter extends
        BrowserProtocolResponseFormatter {
	
	private Logger logger = Logger.getLogger(ShareAddressResponseFormatter.class);
	
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

    @Override
    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) {
        ShareAddressResponse response = (ShareAddressResponse) executorResponse;
        int status = response.getStatus();
        boolean isRGC = response.isRGC();
        String resultText = "N";
        if(status == ExecutorResponse.STATUS_OK)
        {
            resultText = "Y";
        }
        TxNode node = new TxNode();
        node.addMsg(resultText);
        if(isRGC)
        {
            Stop stop = response.getAddress();
            JSONObject stopJo = new JSONObject();
            try {
                stopJo.put("firstLine", stop.firstLine);
                stopJo.put("city", stop.city);
                stopJo.put("state", stop.state);
                stopJo.put("country", stop.country);
                stopJo.put("lon", stop.lon);
                stopJo.put("zip", stop.zip);
                stopJo.put("lat", stop.lat);
                stopJo.put("lon", stop.lon);
                stopJo.put("label", stop.label);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
            	logger.error("JSONException occured when we want to put a string into it");
            	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
            }
            
            node.addMsg(stopJo.toString());
        }
        httpRequest.setAttribute("node", node);

        // TODO save audio
    }
}
