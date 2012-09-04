/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAccountTypeLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestAccountTypeLoadOrder extends TestCase {
	private AccountTypeLoadOrder accountTypeLoadOrder = new AccountTypeLoadOrder();
	public void testGetAttributeValue(){
		UserProfile userProfile = UnittestUtil.createUserProfile();
		TnContext tnContext = UnittestUtil.createTnContext();
		String result = accountTypeLoadOrder.getAttributeValue(userProfile, tnContext);
		assertEquals(UserProfile.DEFAULT_ACCOUNT_TYPE,result);
		
		userProfile.setAttribute("ACCOUNT_TYPE", UTConstant.USERPROFILE_ATTRBUTE_ACCOUNT_TYPE);
		String result1 = accountTypeLoadOrder.getAttributeValue(userProfile, tnContext);
		assertEquals(UTConstant.USERPROFILE_ATTRBUTE_ACCOUNT_TYPE,result1);
	}

}
