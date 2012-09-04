/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;


import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.executor.LoadImageResponse;

/**
 * LoadImageResponseFormatter.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 28, 2010
 *
 */
public class LoadImageResponseFormatter extends HtmlProtocolResponseFormatter
{

    @Override
    public void parseBrowserResponse(HttpServletRequest httpRequest, ExecutorResponse executorResponse) throws Exception
    {
        LoadImageResponse response = (LoadImageResponse)executorResponse;
        Collection<String> imagesCollection = response.getImages().values();
        Map<String,String> map = response.getImages();
        String[] images = new String[imagesCollection.size()+1];
        
        imagesCollection.toArray(images);
        JSONObject json = new JSONObject();
       
        Iterator<Entry<String,String>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Entry<String,String> entry = it.next();
            json.put(entry.getKey(), entry.getValue());
        }
        
        httpRequest.setAttribute("ajaxResponse", json.toString()); 
        
    }

}
