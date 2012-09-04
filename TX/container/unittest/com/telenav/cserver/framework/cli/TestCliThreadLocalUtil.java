/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * TestCliThreadLocalUtil.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-19
 */
public class TestCliThreadLocalUtil extends TestCase {

	private CliThreadLocalUtil ctu = new CliThreadLocalUtil();

	public void testSetCliThreadLocal() {
		UserProfile userProfile = new UserProfile();
		userProfile.setCarrier("ATT");

		ExecutorRequest[] requests = new ExecutorRequest[2];
		requests[0] = new ExecutorRequest();
		requests[0].setUserProfile(userProfile);
		requests[0].setExecutorType("Map");

		requests[1] = new ExecutorRequest();
		requests[1].setUserProfile(userProfile);
		requests[1].setExecutorType("Dynamical_Route");

		CliThreadLocalUtil.setCliThreadLocal(requests);
		CliThreadLocalUtil.setSingleExecutorType("Map");

		assertEquals("ATT", CliThreadLocalUtil.getUserProfile().getCarrier());
		assertEquals("Map", CliThreadLocalUtil.getExecutorType());
		assertEquals("Map",CliThreadLocalUtil.getExecutorType());
	}
}
