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
 * TestDeviceLoadOrder.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-28
 */
public class TestDeviceLoadOrder extends TestCase {
	private DeviceLoadOrder deviceLoadOrder = new DeviceLoadOrder();

	public void testGetAttributeValue() {
		UserProfile userProfile = UnittestUtil.createUserProfile();
		String result = deviceLoadOrder.getAttributeValue(userProfile, null);

		assertEquals(UTConstant.USERPROFILE_DEVICE, result);
	}

}
