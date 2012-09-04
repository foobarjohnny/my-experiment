/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.executor.GetTicketQuantityRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;

/**
 * GetTicketQuantityRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 21, 2010
 *
 */
public class GetTicketQuantityRequestParser extends HtmlProtocolRequestParser
{
    public String getExecutorType() {
        return "getTicketQuantity";
    }

    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception
    {
        String jsonStr = httpRequest.getParameter("jsonStr");
        JSONObject jo = new JSONObject(jsonStr);
        GetTicketQuantityRequest request = new GetTicketQuantityRequest();
        request.setMovieId(jo.getString(RRKey.GTQ_MOVIE_ID));
        request.setTicketTheaterId(jo.getString(RRKey.GTQ_THEATER_ID));
        request.setSearchDate(jo.getString(RRKey.GTQ_SHOW_DATE));
        request.setShowTime(jo.getString(RRKey.GTQ_SHOW_TIME));
        
        String ssoTokenStr = httpRequest.getParameter("ssoToken");
		long userId = -1;
		try{
		    userId = Long.parseLong(HtmlCommonUtil.getUserId(ssoTokenStr));
		}catch(NumberFormatException e)
        {
        }
		request.setUserId(userId);

        return request;
    }
}
