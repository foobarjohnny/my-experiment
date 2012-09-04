/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlGetLogImageResponse;

/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 */
public class HtmlGetLogImageFormatter extends HtmlProtocolResponseFormatter
{

    @Override
    public void parseBrowserResponse(HttpServletRequest httRequest, ExecutorResponse executorResonse) throws Exception
    {
        HtmlGetLogImageResponse response = (HtmlGetLogImageResponse)executorResonse;
        JSONObject json = new JSONObject();
        json.put("image", response.getImage());
        json.put("imageName", HtmlCommonUtil.getString(response.getImageName()));
        
        httRequest.setAttribute("ajaxResponse", json.toString()); 
    }

}
