/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * ThrottlingManagerTest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-2
 * 
 */
public class ThrottlingManagerTest extends TestCase
{

    private static String SERVICE_TEST = "TEST";

    private TnContext tnContext = new TnContext();

    private Service testService = getService(SERVICE_TEST);

    public void testStartAPICallForSuccesss()
    {
        boolean result = ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
        assertTrue(result);
        assertEquals(1, testService.getOnlineNumber());
        ThrottlingManager.endAPICall(SERVICE_TEST, tnContext);
        assertEquals(0, testService.getOnlineNumber());
        resetService();

    }

    public void testStartAPICallForFail()
    {
        // Service Type is not exist, but can get service
        boolean result = ThrottlingManager.startAPICall("NOEXIST", tnContext);
        assertTrue(result);

        // max allowed online number, so can't get service
        for (int i = 0; i < testService.getMaxAllowedOnlineNumber(); i++)
        {
            result = ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
            assertTrue(result);
        }
        result = ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
        assertFalse(result);
        for (int i = 0; i < testService.getMaxAllowedOnlineNumber(); i++)
        {
            ThrottlingManager.endAPICall(SERVICE_TEST, tnContext);
        }
        assertEquals(0, testService.getOnlineNumber());
        resetService();
    }

    public void testThrottlingManagerWhenSingleThread()
    {
        for (int i = 0; i < 1000; i++)
        {
            ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
            ThrottlingManager.endAPICall(SERVICE_TEST, tnContext);
        }
        assertEquals(0, testService.getOnlineNumber());
        resetService();
    }

    public void testThrottlingManagerWhenMultiThread()
    {
        int clientCount = 1000;
        // create test threads

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(clientCount);
        /*
         * create client runnables
         */
        TestRunnable[] testRunnables = new TestRunnable[clientCount];
        for (int i = 0; i < clientCount; i++)
        {
            testRunnables[i] = new TestRunnable(startSignal, doneSignal);
        }
        /*
         * create threads
         */
        for (int i = 0; i < clientCount; i++)
        {
            new Thread(testRunnables[i]).start();
        }
        // start threads
        startSignal.countDown();
        try
        {
            // wait all thread to end
            doneSignal.await();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        assertEquals(0, testService.getOnlineNumber());
        resetService();
    }

    private Service getService(String serviceType)
    {
        List serviceList = ThrottlingManager.getServiceList();
        for (int i = 0, size = serviceList.size(); i < size; i++)
        {
            Service service = (Service) serviceList.get(i);
            if (service.isInService(serviceType))
            {
                return service;
            }
        }
        return null;
    }

    private void resetService()
    {
        testService.setOnlineNumber(0);
    }

    class TestRunnable implements Runnable
    {
        final CountDownLatch startSignal;

        final CountDownLatch doneSignal;

        TestRunnable(CountDownLatch startSignal, CountDownLatch doneSignal)
        {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run()
        {
            try
            {
                startSignal.await();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            boolean result = ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
            assertTrue(result);
            ThrottlingManager.endAPICall(SERVICE_TEST, tnContext);
            doneSignal.countDown();
        }
    }

}
