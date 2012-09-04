/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;

import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;

/**
 * TrafficImageProxy.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2011-8-23
 *
 */
@BackendProxy
@ThrottlingConf("TrafficImageProxy")
public class TrafficImageProxy extends ImageProxy
{
    protected TrafficImageProxy() {}

    @Override
    public String getProxyConfType()
    {
        return "TRAFFIC_IMAGE";
    }
    
    
}
