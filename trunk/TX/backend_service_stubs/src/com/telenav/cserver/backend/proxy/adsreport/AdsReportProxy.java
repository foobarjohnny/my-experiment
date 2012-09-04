package com.telenav.cserver.backend.proxy.adsreport;

import java.io.IOException;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("AdsReportProxy")
public class AdsReportProxy extends HttpClientProxy 
{	
	private static final Logger logger = Logger.getLogger(AdsReportProxy.class);
    @Override
    public String getProxyConfType()
    {
        return "ADSREPORT";
    }
    
    @ProxyDebugLog
    @Throttling
    public AdsReportResponse reportAds(AdsReportRequest request, TnContext tc)
    {
    	String url = getServiceUrl();
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("adsReportUrl>>"+url);
    	}
    	return storeAdsReportData(url, request, tc);
    }
    
    protected AdsReportResponse storeAdsReportData(String url, AdsReportRequest request, TnContext tc)
    {
    	ProcessResult processMethod = new ProcessResult()
    	{
    		public HttpClientResponse process(HttpMethod method) throws IOException
    		{
    			AdsReportResponse response = new AdsReportResponse();
    			response.setStatusCode(method.getStatusCode());
    			return response;
    		}
    	};
    	TxNode reqNode = new TxNode();
    	reqNode.addChild(request.toTxNode());
    	AdsReportResponse response = (AdsReportResponse)send(url, processMethod, TxNode.toByteArray(reqNode));
    	return response != null ? response : new AdsReportResponse();
    }

}
