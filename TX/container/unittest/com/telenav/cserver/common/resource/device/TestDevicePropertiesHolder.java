/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.device;

import java.util.HashMap;
import java.util.Map;

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

/**
 * TestDevicePropertiesHolder.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceFactory.class,DevicePropertiesHolder.class})
public class TestDevicePropertiesHolder extends TestCase {
	private DevicePropertiesHolder devicePropertiesHolder = new DevicePropertiesHolder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private DeviceProperties deviceProperties = PowerMock.createMock(DeviceProperties.class);

	public void testCreateObject() throws Exception {
		PowerMock.mockStatic(ResourceFactory.class);
		Map<String,String> map = new HashMap<String,String>();
		EasyMock.expect(ResourceFactory.createResource(devicePropertiesHolder, userProfile, null)).andReturn(map);
		PowerMock.expectNew(DeviceProperties.class, map).andReturn(deviceProperties);
		PowerMock.replayAll();
		ResourceContent result = devicePropertiesHolder.createObject(null, userProfile, null);
		PowerMock.verifyAll();
		assertNotNull(result.getProperty("device"));
	}
}
