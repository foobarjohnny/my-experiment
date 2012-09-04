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
 * TestProgramCodeLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
public class TestProgramCodeLoadOrder extends TestCase{
	
	private ProgramCodeLoadOrder programCodeLoadOrder = new ProgramCodeLoadOrder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	public void testGetAttributeValue(){
		String result;
		result = programCodeLoadOrder.getAttributeValue(userProfile, null);
		
		assertEquals(UTConstant.USERPROFILE_PROGRAMCODE,result);
	}

}
