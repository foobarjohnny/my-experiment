package com.telenav.cserver.backend.proxy;

import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("DummyProxy")
public class DummyProxy extends AbstractProxy
{
    protected DummyProxy()
    {

    }

    @ProxyDebugLog
    @Throttling
    public void dummyOperationWithoutTnContext()
    {

    }

    @ProxyDebugLog
    @Throttling
    public void dummyOperationWithoutThrottlingExcp(TnContext tc)
    {

    }

    @ProxyDebugLog
    @Throttling
    public String dummyOperationWithGeneralException(TnContext tc) throws Exception
    {
        return "binGo";
    }

    @ProxyDebugLog
    @Throttling
    public void dummyOperationForThrottling(TnContext tc) throws ThrottlingException
    {
        try
        {
            //simulate the long operations
            Thread.sleep(1000);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public String getProxyConfType()
    {
        return "NoneType";
    }

}
