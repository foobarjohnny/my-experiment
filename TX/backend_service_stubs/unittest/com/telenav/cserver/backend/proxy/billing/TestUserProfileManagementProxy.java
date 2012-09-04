/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.userprofile2.userprofile.dataTypes.GetUserProfilePreferencesResponse;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * UserProfileManagementProxyTest
 * @author kwwang
 *
 */
public class TestUserProfileManagementProxy {
	private UserProfileManagementProxy proxy;
	private TnContext tc;

	@Before
	public void init() {
		proxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(UserProfileManagementProxy.class);
		tc = new TnContext();
	}

	@Test
	public void testGetUserProfilePreferences_Success() {
		GetUserProfilePreferencesResponse resp = proxy
				.getUserProfilePreferences(UserProfileManagementHelper
						.createGetUserProfilePreferencesRequest("29c7534c48bfcaf47e7b30c6044420f656e3f34f"), tc);
		Assert.assertNotNull(resp);
		Assert.assertNotNull(resp.getUserProfilePreferences());
		Assert.assertEquals(false,resp.getUserProfilePreferences()[0].getIsAccountSyncEnabled());
	}
	
	@Test
	public void testGetUserProfilePreferences_DeviceNotFound() {
		GetUserProfilePreferencesResponse resp = proxy
				.getUserProfilePreferences(UserProfileManagementHelper
						.createGetUserProfilePreferencesRequest("29c7534c48bfcaf47e7b30c6044420f111111"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("3301",resp.getResponseCode());
	}
	
	@Test
	public void testUpdateUserProfilePreferences_Success() {
		UpdateUserProfilePreferencesResponse resp = proxy
				.updateUserProfilePreferences(UserProfileManagementHelper
						.createUpdateUserProfilePreferencesRequest("10228845", "Jack", "Zhou"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("0000", resp.getResponseCode());
	}
	
	@Test
	public void testUpdateUserProfilePreferences_AccountNotFound() {
		UpdateUserProfilePreferencesResponse resp = proxy
				.updateUserProfilePreferences(UserProfileManagementHelper
						.createUpdateUserProfilePreferencesRequest("234999", "Jack", "Zhou"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("3301", resp.getResponseCode());
	}
}
