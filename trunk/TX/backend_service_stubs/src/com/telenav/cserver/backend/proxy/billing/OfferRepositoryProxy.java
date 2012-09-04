/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.offerrepository.OfferRepositoryStub;
import com.telenav.billing2.offerrepository.dataTypes.GetPurchaseOptionRequest;
import com.telenav.billing2.offerrepository.dataTypes.GetPurchaseOptionResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceManager;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("OfferRepositoryProxy")
public class OfferRepositoryProxy extends AbstractStubProxy<OfferRepositoryStub>
{
    private Logger logger = Logger.getLogger(OfferRepositoryProxy.class);

    protected OfferRepositoryProxy()
    {
        
    }
    
    @ProxyDebugLog
    @Throttling
    public GetPurchaseOptionResponse getPurchaseOption(GetPurchaseOptionRequest request,TnContext tc) throws ThrottlingException
    {
        GetPurchaseOptionResponse response = null;
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getPurchaseOption");
        cli.addData("GetPurchaseOptionRequest", ReflectionToStringBuilder.toString(request));
        OfferRepositoryStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response=stub.getPurchaseOption(request);
            cli.addData("GetPurchaseOptionResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
            logger.fatal("getPurchaseOption failed.",e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    @Override
    protected OfferRepositoryStub createStub(WebServiceConfigInterface ws) throws Exception
    {
        OfferRepositoryStub stub = null;
        try
        {
            stub = new OfferRepositoryStub(createContext(ws), ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (Exception e)
        {
            logger.fatal("create OfferRepositoryStub stub failed", e);
        }

        return stub;
    }

    @Override
    public String getProxyConfType()
    {
        return "WS_OFFER_REPOSITORY";
    }

}
