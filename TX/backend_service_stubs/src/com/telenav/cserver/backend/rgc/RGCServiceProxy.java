/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.rgc;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.service.ace.AddressStatus;
import com.telenav.service.ace.ValidateAddressFromService;

/**
 * RGCServiceProxy
 * @author kwwang
 *
 */
public class RGCServiceProxy
{
    private static final Logger logger = Logger.getLogger(RGCServiceProxy.class);

    private static final RGCServiceProxy instance = new RGCServiceProxy();

    public static final String SERVICE_RGC = "SERVICE_RGC";

    private RGCServiceProxy()
    {
    }

    public static RGCServiceProxy getInstance()
    {
        return instance;
    }

    public AddressStatus reverseGeocode(double lat, double lon, double heading, double speed, TnContext tc)
    {
        boolean startAPICall = false;
        AddressStatus status = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_RGC, tc);
            if (!startAPICall)
            {
                // can't call this API anymore, throws Exception
                throw new ThrottlingException();
            }
            
            status=ValidateAddressFromService.getInstance(tc.toContextString()).reverseGeocode(lat, lon, -1, -1);
            
        }
        catch (Exception e)
        {
            logger.error("oneBoxSearch failed,", e);
        }
        finally
        {
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_RGC, tc);
            }
        }
        return status;
    }
}
