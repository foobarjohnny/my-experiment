/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;


import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestMajorVersionLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestMajorVersionLoadOrder extends TestCase{
	
	private MajorVersionLoadOrder majorVersionLoadOrder = new MajorVersionLoadOrder();
	private String result;
	public void testGetAttributeValue() {
		UserProfile userProfile = UnittestUtil.createUserProfile();
		
		result = majorVersionLoadOrder.getAttributeValue(userProfile, null);
		assertEquals(UTConstant.USERPROFILE_VERSION, result);
		
		userProfile.setVersion("6.0.01");
		result = majorVersionLoadOrder.getAttributeValue(userProfile, null);
		assertEquals("6_0", result);
		
		userProfile.setVersion("6.0");
		result = majorVersionLoadOrder.getAttributeValue(userProfile, null);
		assertEquals("6_0", result);
		
		userProfile.setVersion(null);
		result = majorVersionLoadOrder.getAttributeValue(userProfile, null);
		assertNull( result);
		
		userProfile.setVersion("");
		result = majorVersionLoadOrder.getAttributeValue(userProfile, null);
		assertEquals("", result);
	}

}
