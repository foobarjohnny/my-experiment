package com.telenav.cserver.backend.proxy.autosuggest;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.util.CSStringUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */

/**
 * AutoSuggestProxy.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 13, 2011
 *
 */
@BackendProxy
@ThrottlingConf("AutoSuggestProxy")
public class AutoSuggestProxy extends HttpClientProxy
{
    private static Logger logger = Logger.getLogger(AutoSuggestProxy.class);
    @Override
    public String getProxyConfType()
    {
        return "AUTOSUGGEST";
    }
    
    @ProxyDebugLog
    @Throttling
    public GetSuggestListResponse getSuggestList(GetSuggestListRequest request, TnContext tc)
    {
        CliTransaction cli =  CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getAutoSuggestList");
        cli.addData("request", request.toString());
        String url = createSuggestListUrl(request);
        if( logger.isDebugEnabled() )
        {
            logger.debug("suggestUrl: "+url);
        }
        cli.addData("suggestUrl", url);
        GetSuggestListResponse response = getSuggestList(url);
        cli.complete();
        return response;
    }
    
    protected String createSuggestListUrl(GetSuggestListRequest request)
    {
        String suggestListUrl = MessageFormat.format(this.getServiceUrl(), new Object[] {String.valueOf(request.getCount()), URLEncoder.encode(request.getQueryString()), 
                request.getLat(), request.getLon(), request.getPtn(),request.getTransactionId(),request.getUserId(), request.getMapDataSet()});
        if( request.getExtendParams() != null )
        {
            suggestListUrl += request.getExtendParams();
        }
        return suggestListUrl;
    }
    
    
    protected GetSuggestListResponse getSuggestList(String url)
    {
        ProcessResult processMethod = new ProcessResult()
        {
            @Override
            public HttpClientResponse process(HttpMethod method) throws Exception
            {
                GetSuggestListResponse response = new GetSuggestListResponse();
                response.setStatusCode(method.getStatusCode());
                byte[] bytes = method.getResponseBody();
                
                // Test
                String jsonStr = new String(bytes);
                if(logger.isDebugEnabled())
                {
                    logger.debug("Suggest String : " + jsonStr);
                }
                JSONObject object = new JSONObject(jsonStr);

                Object suggestObj = CSStringUtil.getJSONOBject(object, "suggestion");
                if (suggestObj != null && suggestObj instanceof JSONArray)
                {
                    JSONArray array = (JSONArray) suggestObj;
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject eachObj = array.getJSONObject(i);
                        SuggestItem suggestItem = new SuggestItem();
                        suggestItem.setShowterm(CSStringUtil.getJSONString(eachObj, "showterm").trim());
                        suggestItem.setSearchTerm(CSStringUtil.getJSONString(eachObj, "searchterm").trim());
                        suggestItem.setLatLon(CSStringUtil.getJSONString(eachObj, "ll").trim());
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(suggestItem.toString());
                        }
                        response.addSuggestItem(suggestItem);
                    }
                }
                return response;
            }
        };
        
        GetSuggestListResponse response = (GetSuggestListResponse)this.send(url, processMethod);
        return response != null ? response : new GetSuggestListResponse();
    }
}
