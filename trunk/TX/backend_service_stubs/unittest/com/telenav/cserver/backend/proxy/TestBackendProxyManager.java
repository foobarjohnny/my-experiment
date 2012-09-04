/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestBackendProxyManager
 * 
 * @author kwwang
 * @date 2011-7-5
 */
public class TestBackendProxyManager
{
    @Test(expected = IllegalArgumentException.class)
    public void testWithoutTncontextInParams()
    {
        DummyProxy dummyProxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(DummyProxy.class);
        dummyProxy.dummyOperationWithoutTnContext();
    }

    
    
    /**
     * dont run this test currently, this part will be integrated in next phrase.
     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testWithoutThrowingThrottlingException()
//    {
//        DummyProxy dummyProxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(DummyProxy.class);
//        dummyProxy.dummyOperationWithoutThrottlingExcp(new TnContext());
//    }

    @Test
    public void testWithGeneralException() throws Exception
    {
        DummyProxy dummyProxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(DummyProxy.class);
        Assert.assertEquals("binGo", dummyProxy.dummyOperationWithGeneralException(new TnContext()));
    }

    @Test
    public void testThrottling() throws Exception
    {
        final DummyProxy dummyProxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(DummyProxy.class);

        final CountDownLatch latch = new CountDownLatch(5);

        final AtomicInteger throttlingCount = new AtomicInteger(0);

        for (int i = 0; i < 5; i++)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        latch.await();
                        dummyProxy.dummyOperationForThrottling(new TnContext());
                    }
                    catch (ThrottlingException e)
                    {
                        throttlingCount.getAndIncrement();
                    }
                    catch (Exception e)
                    {

                    }
                }
            }).start();
            
            latch.countDown();
        }

        Thread.sleep(5000);
        Assert.assertEquals(3, throttlingCount.get());
    }
    
    @Test
    public void testWithoutThrottlingConf()
    {
        DummyProxy4NonexistThrottlingConf proxy=BackendProxyManager.getBackendProxyFactory().getBackendProxy(DummyProxy4NonexistThrottlingConf.class);
        Assert.assertEquals("binGo", proxy.dummyOperation(new TnContext()));
    }

}
