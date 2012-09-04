/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.TestCase;

import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.common.resource.message.MessagesHolder;

/**
 * TestResourceHolderManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-11
 */
public class TestResourceHolderManager extends TestCase{
	private MessagesHolder messageHolder = new MessagesHolder();
	private BillingConfHolder billingHolder = new BillingConfHolder();
	@Override
	protected void setUp() throws Exception {
		messageHolder.setName("messageHolder");
		billingHolder.setName("billingHolder");
		
		billingHolder.put("1","111");
		billingHolder.put("2","2222");
	}
	public void testRefresh(){
		ResourceHolderManager.register(messageHolder);
		ResourceHolderManager.register(billingHolder);
		
		assertEquals(2,billingHolder.getMap().size());
		
		ResourceHolderManager.refresh();
		assertEquals(0,billingHolder.getMap().size());
	}

}
