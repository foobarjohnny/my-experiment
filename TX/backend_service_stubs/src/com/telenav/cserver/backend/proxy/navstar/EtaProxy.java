/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.navstar;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.PoiSearchServiceConfig;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractNewStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.impl.interceptor.TnContextInterceptor;
import com.telenav.cserver.resource.datatypes.RegionGroup;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.eta.EtaStub;
import com.telenav.services.eta.EtaStub.ETARequest;
import com.telenav.services.eta.EtaStub.ETAResponse;

/**
 * EtaProxy
 * 
 * @author kwwang
 * @date 2011-6-28
 */
@BackendProxy
@ThrottlingConf("EtaProxy")
public class EtaProxy extends AbstractNewStubProxy<EtaStub,String>
{
    public static final Logger logger = Logger.getLogger(EtaProxy.class);
    
    public static final String DATA_SET="dataset";
    
    public static final String UGT="UGT";
    
    public static final String INRIX="INRIX";

    protected EtaProxy()
    {

    }

    @ProxyDebugLog
    @Throttling
    public ETAResponse getEta(ETARequest request, UserProfile userProfile, TnContext tc)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getEta");

        cli.addData("ETARequest", ReflectionToStringBuilder.toString(request));
        ETAResponse resp = new ETAResponse();
        try
        {
        	resp = createStub(getWebServiceConfigInterface(), createKey(userProfile, tc)).getETA(request);
            cli.addData("ETAResponse", ReflectionToStringBuilder.toString(resp));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
            logger.fatal("getEta failed,", e);
        }
        finally
        {
            cli.complete();
        }
        return resp;
    }

    @Override
    protected EtaStub createStub(WebServiceConfigInterface ws,String key) throws Exception
    {
    	PoiSearchServiceConfig conf=(PoiSearchServiceConfig)ws;
    	Map<String, String> urlMap = conf.getServiceUrlMapping();
    	EtaStub stub = new EtaStub(createContext(ws), getUrl(urlMap, key));
        stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        return stub;
    }
           
    public String createKey(UserProfile userProfile, TnContext tc){
    	StringBuilder key = new StringBuilder();;
    	if(userProfile != null && userProfile.getRegion() != null){
    		key.append(RegionGroup.getRegionGroup(userProfile.getRegion()));
    	}

    	if(tc != null && tc.getProperty(DATA_SET) != null){
    		key.append(SEPARATOR + tc.getProperty(DATA_SET));
    	}

    	String dsmRule = tc.getProperty(TnContextInterceptor.NAV_FLOW_DATA_SRC_KEY);
    	if(StringUtils.isNotEmpty(dsmRule) && dsmRule.contains(UGT)){
    		key.append(SEPARATOR + UGT);
    	}else if(StringUtils.isNotEmpty(dsmRule) && dsmRule.contains(INRIX)){
    		key.append(SEPARATOR + INRIX);
    	}

    	if(logger.isDebugEnabled()){
    		logger.debug("get eta service key:" + key);
    	}

    	return key.toString();
    }

    @Override
    public String getProxyConfType()
    {
        return "ETA_SERVICE";
    }
}
