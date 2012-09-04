/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.holder.impl;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestBillingConfHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BillingConfHolder.class, ResourceFactory.class})
public class TestBillingConfHolder extends TestCase{
	private BillingConfHolder billingConfHolder = new BillingConfHolder();
	public void testCreateObject() throws Exception{
		UserProfile userProfile = UnittestUtil.createUserProfile();
		TnContext tc = UnittestUtil.createTnContext();
		
		PowerMock.mockStatic(ResourceFactory.class);
		Object result = new Object();
		EasyMock.expect(ResourceFactory.createResource(billingConfHolder, userProfile, tc)).andReturn(result);

		PowerMock.replayAll();
		
		ResourceContent rc = billingConfHolder.createObject(null,userProfile , tc);
		
		PowerMock.verifyAll();
		Assert.assertTrue("the two object should be the same.",result==rc.getObject());
	}

}
