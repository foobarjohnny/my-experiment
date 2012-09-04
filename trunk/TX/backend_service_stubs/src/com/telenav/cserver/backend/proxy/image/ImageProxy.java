package com.telenav.cserver.backend.proxy.image;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */

/**
 * ImageProxy.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 12, 2011
 *
 */
@BackendProxy
@ThrottlingConf("ImageProxy")
public class ImageProxy extends HttpClientProxy
{
    private static final Logger logger = Logger.getLogger(ImageProxy.class);
    
    private static final String HEAD_EXP_DATE = "Expires";
    private static final String EXP_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ssZ";
    
    @Override
    public String getProxyConfType()
    {
        return "IMAGE";
    }
    
    @ProxyDebugLog
    @Throttling
    public ImageResponse getMapTile(ImageRequest request, TnContext tc)
    {
        String url = createImageUrl(request, true);
        if(logger.isDebugEnabled())
        {
                logger.debug("mapURL>>"+url);
        }
        return getMapTile(url);
    }
    
//    protected String createImageUrl(ImageRequest request)
//    {
//        String mapUrl =  MessageFormat.format(getServiceUrl(), 
//            new Object[]{request.getLayers(), request.getXCoordinate(), request.getYCoordinate(),request.getZoom(),request.getVersion(), request.getStyle()});
//        if( request.getExtendParams() != null )
//            mapUrl += request.getExtendParams();
//        return mapUrl;
//    }
    
    
    protected String createImageUrl(ImageRequest request, boolean flag)
    {
        String url = ImageUrlDaemon.createImageUrl(request);
//        if (StringUtils.isEmpty(url))
//        {
//        	url = createImageUrl(request);
//        }
        return url;
    }    
    
    protected ImageResponse getMapTile(String url)
    {
        ProcessResult processMethod = new ProcessResult() 
        {
            public HttpClientResponse process(HttpMethod method) throws IOException
            {   
                ImageResponse resonse = new ImageResponse();
                resonse.setBinData(method.getResponseBody());
                resonse.setStatusCode(method.getStatusCode());
                if(method.getResponseHeader(HEAD_EXP_DATE) != null)
                {
                    try
                    {
                        SimpleDateFormat dataFormat = new SimpleDateFormat(EXP_DATE_FORMAT);
                        long expireTime = ((Date)dataFormat.parse(method.getResponseHeader(HEAD_EXP_DATE).getValue())).getTime();
                        resonse.setExpirationData(expireTime);
                    }
                    catch(Exception ex)
                    {
                        resonse.setExpirationData(-1);
                    }
                }
                else
                {
                    resonse.setExpirationData(-1);
                }
                return resonse;
            }
        };
        
        ImageResponse response = (ImageResponse)send(url, processMethod);
        return response != null ? response : new ImageResponse();
        
    }
 }
