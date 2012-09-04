/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * TestAbstractSetResourceHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-18
 */
public class TestAbstractSetResourceHolder extends TestCase{
	private TestableClass tc = new TestableClass();
	
	public void testGetKey(){
		String result;
		result = tc.getKey(null, null);
		assertEquals("null_abc",result);
	}
	
	private class TestableClass extends AbstractSetResourceHolder{
		@Override
		public void clear() {
			super.clear();
		}
		@Override
		public ResourceContent createObject(String key, UserProfile profile,TnContext tnContext) {
			return null;
		}
		@Override
		protected String getSetIndex(UserProfile profile, TnContext tc) {
			return "abc";
		}
	}

}
