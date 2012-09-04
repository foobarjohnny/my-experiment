package com.telenav.cserver.backend.proxy;

import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("DummyProxy4NonexistThrottlingConf")
public class DummyProxy4NonexistThrottlingConf extends AbstractProxy
{
    @ProxyDebugLog
    @Throttling
    public String dummyOperation(TnContext tc)
    {
        return "binGo";
    }

    @Override
    public String getProxyConfType()
    {
        return "";
    }

}
