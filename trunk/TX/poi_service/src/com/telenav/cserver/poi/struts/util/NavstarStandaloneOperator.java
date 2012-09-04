package com.telenav.cserver.poi.struts.util;


import com.telenav.cserver.framework.throttling.AbstractOperator;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.NavFactory;
import com.telenav.navstar.proxy.NavResult;
import com.telenav.navstar.proxy.NavStarProxy;

/**
 * 
 * @author chbzhang
 *
 */
public class NavstarStandaloneOperator extends AbstractOperator
{   
    public final static String SERVICE_NAVSTAR = "NAVSTAR";
   
    public static NavResult retrieveLastResult(TnContext tc, int routeId, int routePathId) throws ThrottlingException
    {
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
            if(!startAPICall)
            {
                //can't call this API anymore, throws Exception
                throw new ThrottlingException();
            }
            NavStarProxy proxy = NavFactory.createNavStarProxy();
            NavResult res = proxy.retrieveLastResult(tc, routeId, routePathId);
            return res;
        }
        finally
        {
            if(startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
            }
        }
    }
}