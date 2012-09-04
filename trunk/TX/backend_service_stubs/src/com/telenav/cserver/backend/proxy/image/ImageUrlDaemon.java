/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.proxy.BackendProxyManager;



/**
 * ImageProxy.java
 *
 * jzhu@telenav.cn
 * @version 1.0 Jun 6, 2012
 *
 */
public class ImageUrlDaemon implements Runnable
{
    private static final Logger logger = Logger.getLogger(ImageUrlDaemon.class);
    private static HashMap<String, String> urlMap = new HashMap<String, String>();
    
    public void run()
    {
		try 
		{
			Set<Entry<String, String>> entries = urlMap.entrySet();
			String url;
			String key;
			for (Entry<String, String> entry : entries)
			{
				key = entry.getKey();
				url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
						ImageUrlHttpClient.class).getImageURL(key);
				if (!StringUtils.isEmpty(url))
				{
					logger.debug("Image map url from backend: [" + key + "] " + url);
					urlMap.put(key, url);
				}
			}
		} 
		catch (Exception e) 
		{
			logger.error(e.getStackTrace());
		}
    }
    
    
    public static String createImageUrl(ImageRequest request)
    {
    	String url = getImageUrlInfo(request);
    	if (StringUtils.isEmpty(url))
    	{
    		return url;
    	}
    	
        url = url.replaceFirst("@x", request.getXCoordinate())
                        .replaceFirst("@y", request.getYCoordinate())
                        .replaceFirst("@zoom", request.getZoom())
                        .replaceFirst("@style", "ts="+request.getStyle());
        
        
        
        if( request.getExtendParams() != null )
            url += request.getExtendParams();
        
        logger.debug(url);
        return url;

    }

    private static String getImageUrlInfo(ImageRequest request)
    {
    	String layer = request.getLayers();
        String urlInfo;
        if (!urlMap.containsKey(layer))
        {
            synchronized(urlMap)
            {
//                urlInfo = urlMap.get(request.getLayers());
                if (!urlMap.containsKey(layer))
                {
                	urlInfo = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
							ImageUrlHttpClient.class).getImageURL(layer);

            		urlMap.put(layer, urlInfo);
					logger.debug("Image map url from backend: [" + layer + "] " + urlInfo);
                }
                else 
                {
					urlInfo = urlMap.get(layer);
				}
            }
        }
        else 
        {
			urlInfo = urlMap.get(layer);
		}
        
		logger.debug("Image map url: [" + layer + "] " + urlInfo);
        return urlInfo == null ? "" : urlInfo;
    } 
    
    
//    private static String getImageUrlInfo(ImageRequest request)
//    {
//    	String layer = request.getLayers();
//        String url = urlMap.get(request.getLayers());
//        if (StringUtils.isEmpty(url))
//        {
//            synchronized(urlMap)
//            {
//                url = urlMap.get(request.getLayers());
//                if (StringUtils.isEmpty(url))
//                {
//                	url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
//							ImageUrlHttpClient.class).getImageURL(layer);
//                	if (!StringUtils.isEmpty(url))
//                	{
//                		urlMap.put(layer, url);
//    					logger.debug("Image map url from backend: [" + layer + "] " + url);
//                	}
//                }
//            }
//        }
//        
//		logger.debug("Image map url: [" + layer + "] " + url);
//        return url == null ? "" : url;
//    } 
 }
