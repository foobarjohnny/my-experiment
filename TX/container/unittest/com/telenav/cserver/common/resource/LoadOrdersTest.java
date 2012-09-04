/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * LoadOrdersTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-7
 *
 */
public class LoadOrdersTest extends TestCase
{
    
    @Override
    protected void setUp() throws Exception
    {
        LoadOrderManager.register(new LoadOrder1());
        LoadOrderManager.register(new LoadOrder2());
    }

    public void testAddOrderStringForSuccessful()
    {
        LoadOrders loadOrders = new LoadOrders();
        loadOrders.addOrderString("loadorder1,loadorder2");
        assertEquals(2,loadOrders.getOrders().size());
    }
    
    public void testAddOrderStringForFail()
    {
        LoadOrders loadOrders = new LoadOrders();
        loadOrders.addOrderString("");
        assertEquals(0,loadOrders.getOrders().size());
        loadOrders.addOrderString("noexist");
        assertEquals(0,loadOrders.getOrders().size());
        loadOrders.addOrderString("noexist,noexist1,noexist2,");
        assertEquals(0,loadOrders.getOrders().size());
    }
    
    static class LoadOrder1 extends LoadOrder
    {
        public LoadOrder1()
        {
            setType("loadorder1");
        }
        @Override
        public String getAttributeValue(UserProfile profile, TnContext tnContext)
        {
            return null;
        }
        
    }

    static class LoadOrder2 extends LoadOrder
    {
        public LoadOrder2()
        {
            setType("loadorder2");
        }
        @Override
        public String getAttributeValue(UserProfile profile, TnContext tnContext)
        {
            return null;
        }
        
    }


}

