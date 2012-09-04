/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.framework.util.CSStringUtil;

/**
 * @author huishen
 *
 */
@BackendProxy
public class ImageUrlHttpClient extends HttpClientProxy 
{
	private static Logger logger = Logger.getLogger(ImageUrlHttpClient.class);
	
	@Override
	public String getProxyConfType() 
	{
		return "IMAGEURL";
	}

    public String getImageURL(String key)
    {
        String url = this.getServiceUrl() + key + ".json";
        ImageUrlResponse response = this.requestImageURL(url);
        return response.getUrl();
    }
	
    protected ImageUrlResponse requestImageURL(String url)
    {
        ProcessResult processMethod = new ProcessResult()
        {
            @Override
            public HttpClientResponse process(HttpMethod method) throws Exception
            {
            	ImageUrlResponse response = new ImageUrlResponse();
                response.setStatusCode(method.getStatusCode());
                
                if (response.isSuccess())
                {
                	JSONObject object = new JSONObject(method.getResponseBodyAsString());
                    response.setUrl(CSStringUtil.getJSONString(object, "URL"));
                }
                else 
                {
					logger.fatal(getProxyConfType() + " : " + response.getStatusCode());
				}
                
                return response;
            }
        };
        
        ImageUrlResponse response = (ImageUrlResponse) this.send(url, processMethod);
        return response != null ? response : new ImageUrlResponse();
    }

}
