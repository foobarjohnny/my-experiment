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
 * TestCarrierLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestCarrierLoadOrder  extends TestCase {
	private CarrierLoadOrder carrierLoadOrder = new CarrierLoadOrder();
	public void testGetAttributeValue(){
		UserProfile userProfile = UnittestUtil.createUserProfile();
		String result = carrierLoadOrder.getAttributeValue(userProfile, null);
		
		assertEquals(UTConstant.USERPROFILE_CARRIER,result);
	}

}
