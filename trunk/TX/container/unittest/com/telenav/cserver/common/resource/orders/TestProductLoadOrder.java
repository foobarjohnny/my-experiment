/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestProductLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
public class TestProductLoadOrder extends TestCase{
	private ProductLoadOrder productLoadOrder = new ProductLoadOrder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	public void testGetAttributeValue(){
		String result = productLoadOrder.getAttributeValue(userProfile, null);
		assertEquals(UTConstant.USERPROFILE_PRODUCT,result);
	}
	public void testGetAttributeValueList(){
		List<String> list = productLoadOrder.getAttributeValueList(userProfile, null);
		assertNotNull(list);
		assertEquals(1,list.size());
		assertEquals(UTConstant.USERPROFILE_PRODUCT,list.get(0));
	}

}
