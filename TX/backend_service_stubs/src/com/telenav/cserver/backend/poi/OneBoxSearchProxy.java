/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poi;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.search.onebox.v10.OneboxSearchRequest;
import com.telenav.services.search.onebox.v10.OneboxSearchResponse;
import com.telenav.services.search.onebox.v10.OneboxSearchServiceStub;

/**
 * OneBoxSearchProxy
 * 
 * @author kwwang
 * 
 */
public class OneBoxSearchProxy
{
    private static final Logger logger = Logger.getLogger(OneBoxSearchProxy.class);

    private static final OneBoxSearchProxy instance = new OneBoxSearchProxy();

    private OneboxSearchServiceStub stub;

    public final static String SERVICE_POI = "SERVICE_ONEBOXSEARCH";

    private OneBoxSearchProxy()
    {
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(SERVICE_POI);
            stub = new OneboxSearchServiceStub(ws.getServiceUrl());
        }
        catch (AxisFault e)
        {
            logger.error("new OneboxSearchServiceStub failed,", e);
        }
    }

    public static OneBoxSearchProxy getInstance()
    {
        return instance;
    }

    public OneboxSearchResponse oneBoxSearch(OneboxSearchRequest obReq, TnContext tc)
    {
        boolean startAPICall = false;
        OneboxSearchResponse obResp = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_POI, tc);
            if (!startAPICall)
            {
                // can't call this API anymore, throws Exception
                throw new ThrottlingException();
            }
            obResp = stub.oneboxSearch(obReq);
        }
        catch (Exception e)
        {
            logger.error("oneBoxSearch failed,", e);
        }
        finally
        {
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_POI, tc);
            }
        }

        return obResp;
    }
}
