/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

import junit.framework.TestCase;

import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;

/**
 * TestAbstractCacheModel.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
public class TestAbstractCacheModel extends TestCase{
	//Cannot instantiate the type AbstractCacheModel, so we find a class BillingConfHolder extends AbstractCacheModel for testing
	private TestableClass tc = new TestableClass();
	
	public void testGet(){
		String result,result_put;
		tc.put("1","one");
		tc.put("2","two");
		tc.put("3","three");
		
		result = tc.get("1","first").toString();
		result_put = tc.get("4","four").toString();
		
		tc.getMap();
		tc.clear();
		
		assertEquals("one",result);
		assertEquals("four",result_put);
	}
	
	private class TestableClass extends AbstractCacheModel{
		@Override
		public Object createObject(Object key, Object argument) {
			return argument;
		}
	}

}
