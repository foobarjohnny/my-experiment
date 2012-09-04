/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.navstar;

import org.apache.commons.lang.StringUtils;

import com.telenav.cserver.backend.datatypes.map.LatLonPoint;

/**
 * EtaProxyHelper
 * @author kwwang
 * @date 2011-6-28
 */
public class EtaProxyHelper
{
    public static final String COMMA=",";
    
    public static String createEtaStartEndParam(LatLonPoint origin,LatLonPoint dest,String routeStyle)
    {
        StringBuilder builder=new StringBuilder();
        builder.append(origin.getLat()).append(COMMA);
        builder.append(origin.getLon()).append(COMMA);
        builder.append(dest.getLat()).append(COMMA);
        builder.append(dest.getLon()).append(COMMA);
        if (StringUtils.isNotBlank(routeStyle))
            builder.append(routeStyle);
        return builder.toString();
    }
}
