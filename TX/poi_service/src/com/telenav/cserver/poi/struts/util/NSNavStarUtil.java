package com.telenav.cserver.poi.struts.util;

import com.telenav.navstar.proxy.NavResult;
import com.telenav.navstar.proxy.facade.NavRoutePath;

/**
 * search along to get routePath
 * @author chbzhang
 *
 */
public class NSNavStarUtil {
    public static NavRoutePath fetchActiveRoutePath(NavResult res, int routeId)
    {
        return res.getActiveRoutePath(0, routeId);
    }
}
