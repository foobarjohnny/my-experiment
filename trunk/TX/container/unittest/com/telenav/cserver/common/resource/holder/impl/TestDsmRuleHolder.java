/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.holder.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestDsmRuleHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestDsmRuleHolder extends TestCase{
	private TestableDsmRuleHolder dsmRuleHolder = new TestableDsmRuleHolder(); 
	HashMap<String,String> map = new HashMap<String,String>();
	String key = "key";
	
	@Override
	protected void setUp() throws Exception {
		map.put(key, "I am a key.");
	}
	public void testGetDsmResponseBy(){
		String result = dsmRuleHolder.getDsmResponseBy(null, key, null);
		assertEquals("I am a key.",result);
	}
	
	public void testGetDsmResponseBy_null(){
		map = null;
		String result = dsmRuleHolder.getDsmResponseBy(null, key, null);
		assertNull(result);
	}
	
	public void testSimple(){
		dsmRuleHolder.getDsmResponses(null,null);
	}
	
	private class TestableDsmRuleHolder extends DsmRuleHolder
	{
		@Override
		protected HashMap getMatchedMap(UserProfile profile, TnContext tc) {
			return map;
		}
	}

}
