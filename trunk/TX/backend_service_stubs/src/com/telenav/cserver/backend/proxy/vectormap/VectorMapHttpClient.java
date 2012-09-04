/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.vectormap;

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
public class VectorMapHttpClient extends HttpClientProxy 
{
	private static Logger logger = Logger.getLogger(VectorMapHttpClient.class);
	
	@Override
	public String getProxyConfType() 
	{
		return "VECTORMAP";
	}
	
    public String getVectorMapURL(String key)
    {
        String url = this.getServiceUrl() + key + ".json";
        VectorMapResponse response = this.getVectorMap(url);
        return response.getUrl();
    }
	
    protected VectorMapResponse getVectorMap(String url)
    {
        ProcessResult processMethod = new ProcessResult()
        {
            @Override
            public HttpClientResponse process(HttpMethod method) throws Exception
            {
            	VectorMapResponse response = new VectorMapResponse();
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
        
        VectorMapResponse response = (VectorMapResponse) this.send(url, processMethod);
        return response != null ? response : new VectorMapResponse();
    }

}
