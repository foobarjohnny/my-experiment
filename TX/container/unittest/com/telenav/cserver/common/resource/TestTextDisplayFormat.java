/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.unittestutil.UnittestUtil;

import junit.framework.TestCase;

/**
 * TestTextDisplayFormat.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-13
 */
public class TestTextDisplayFormat extends TestCase{
	private ResourceCacheManagement cacheManagement = ResourceCacheManagement.getInstance();
	private TextDisplayFormat textDisplayFormat = new TextDisplayFormat(cacheManagement);
	
	private final static String holderName = "BillingConfHolder";
	private final static String holderType = "Bill";
	private final static String holderKey = "key_bill";
	
	private BillingConfHolder bholder = new BillingConfHolder();
	@Override
	protected void setUp() throws Exception {
		cacheManagement.clear();
		bholder.setName(holderName);
		bholder.setType(holderType);
		cacheManagement.addHolder(bholder);
	}
	public void testStatistic() {
		String result = textDisplayFormat.statistic();
		assertEquals("-----------CacheManagement------------\n"+
				"Statistic\n"+
				"Holder Type: = 1\n"+
				"Holders: = 1\n"+
				"Memory: = 0Byte\n\n"+
				"Holder\n"+
				"BillingConfHolder[0] = 0Byte\n"+
				"-----------------end------------------\n"
					,result);
	}
	
	public void testDetails(){
		String result = textDisplayFormat.details();
		assertEquals("-----------CacheManagement------------\n"+
				"Statistic\n"+
				"Holder Type: = 1\n"+
				"Holders: = 1\n"+
				"Memory: = 0Byte\n\n"+
				"Holder\n"+
				"--------------------------------------\n"+
				"-----------------end------------------\n"
					,result);
	}
	
	public void testDetails_holderMapNotNull(){
		bholder.put(holderKey, "value of "+holderKey);
		String result = textDisplayFormat.details();
		System.out.println(result);
		assertEquals("-----------CacheManagement------------\n"+
				"Statistic\n"+
				"Holder Type: = 1\n"+
				"Holders: = 1\n"+
				"Memory: = 0Byte\n\n"+
				"Holder\n"+
				"key_bill = 112Byte--------------------------------------\n"+
				"-----------------end------------------\n"
					,result);
	}
	
	public void testSimple(){
		try{
			textDisplayFormat.details("");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		
		try{
			textDisplayFormat.contents("", "");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}

}
