/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.movie.html.executor.LoadImageRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

/**
 * LoadImageRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 28, 2010
 *
 */
public class LoadImageRequestParser extends HtmlProtocolRequestParser
{
    private static Logger logger = Logger.getLogger(LoadImageRequestParser.class);
    @Override
    public String getExecutorType()
    {
        return "loadImage";
    }

    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception
    {
        String jsonStr = httpRequest.getParameter("jsonStr");
        JSONObject jo = new JSONObject(jsonStr);
        LoadImageRequest request = new LoadImageRequest();
        String idsString = jo.getString(RRKey.LM_MOVIE_ID);
        if( logger.isDebugEnabled() )
        {
            logger.debug("movieIds = "+idsString);
        }
        if( idsString != null )
        {
            String[] ids = StringUtils.split(idsString, ',');
            request.setMovieIds(ids);
        }
        
        request.setHeight(120);
        request.setWidth(80);
        
        return request;
    }
    

}
