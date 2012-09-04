/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

import com.telenav.cserver.common.cache.AtomicCounter;
import com.telenav.cserver.common.cache.SizeOfObjectFactory;
import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.common.resource.message.MessagesHolder;

/**
 * TestResourceCacheManagement.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
public class TestResourceCacheManagement extends TestCase{
	private ResourceCacheManagement resourceCacheManagement = ResourceCacheManagement.getInstance();
	private MessagesHolder messagesHolder = new MessagesHolder();
	private BillingConfHolder billingConfHolder1 = new BillingConfHolder();
	private BillingConfHolder billingConfHolder2 = new BillingConfHolder();
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
		billingConfHolder1.put("1","");
		billingConfHolder1.put("2","");
		billingConfHolder1.setName("billingConfHolder1");
		
		billingConfHolder2.put("1","");
		billingConfHolder2.put("2","");
		billingConfHolder2.put("3","");
		billingConfHolder2.setName("billingConfHolder2");
		
		resourceCacheManagement.addHolder(billingConfHolder1);
		resourceCacheManagement.addHolder(billingConfHolder2);
		
		
		messagesHolder.setName("messagesHolder");
		resourceCacheManagement.addHolder(messagesHolder);
	}
	
	public void testGetCountOfCacheObject(){
		assertEquals(5,resourceCacheManagement.getCountOfCacheObject());
	}
	public void testGetCountOfHolderType(){
		assertEquals(2,resourceCacheManagement.getCountOfHolderType());
	}
	
	public void testGetTotalCacheSize(){
		assertEquals(0,resourceCacheManagement.getTotalCacheSize());
	}
	
	public void testClear(){
		resourceCacheManagement.clear();
		assertEquals(0,resourceCacheManagement.getCountOfCacheObject());
	}
	
	public void testGetSimpleClassName(){
		String allName = this.getClass().getName();
		assertEquals(this.getClass().getSimpleName(),resourceCacheManagement.getSimpleClassName(allName));
		assertNull(resourceCacheManagement.getSimpleClassName(null));
		assertEquals("telenav",resourceCacheManagement.getSimpleClassName("telenav"));
	}
	
	public void testGetCounterOfHolder(){
		MessagesHolder messagesHolder1 = new MessagesHolder();
		messagesHolder1.setName("test");
		MessagesHolder messagesHolder2 = new MessagesHolder();
		messagesHolder2.setName("test");
		resourceCacheManagement.addHolder(messagesHolder1);
		resourceCacheManagement.addHolder(messagesHolder2);
		assertEquals(2,resourceCacheManagement.getCounterOfHolder("test"));
	}
	
	public void testGetHolder(){
		MessagesHolder holder = new MessagesHolder();
		holder.setName("testHolder");
		resourceCacheManagement.addHolder(holder);
		ResourceHolder result = resourceCacheManagement.getHolder("testHolder");
		assertEquals(holder,result);
		assertNull(resourceCacheManagement.getHolder("_123"));
	}
	
	public void testGetCacheSize(){
		assertEquals(144,resourceCacheManagement.getCacheSize(billingConfHolder1));
	}
	
	public void testSetAndGet() throws Exception{
		resourceCacheManagement.setCacheSize(new AtomicCounter(0));
		resourceCacheManagement.setDisplayFormat(new ServletHTMLDisplayFormat(ResourceCacheManagement.getInstance()));
		resourceCacheManagement.contents("billingConfHolder1", "1");
		resourceCacheManagement.statistic();
		resourceCacheManagement.details("");
		resourceCacheManagement.details();
		resourceCacheManagement.toString();
		Whitebox.invokeMethod(resourceCacheManagement, "setSizeOfObject", SizeOfObjectFactory.getInstance());
	}

}
