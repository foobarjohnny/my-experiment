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
 * TestRegionLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestRegionLoadOrder extends TestCase {
	private RegionLoadOrder regionLoadOrder = new RegionLoadOrder();
	public void testGetAttributeValue(){
		UserProfile userProfile = UnittestUtil.createUserProfile();
		String result = regionLoadOrder.getAttributeValue(userProfile, null);
		
		assertEquals(UTConstant.USERPROFILE_REGION,result);
	}


}
