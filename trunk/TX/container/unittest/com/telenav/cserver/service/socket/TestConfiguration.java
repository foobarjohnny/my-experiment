/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.service.socket;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * TestConfiguration.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Configuration.class })
public class TestConfiguration {
	private HashMap configs = new HashMap();
	Configuration configuration;

	/* 
	 *
	 */
	@Before
	public void setUp() throws Exception {
		Whitebox.setInternalState(Configuration.class, "configs", configs);

		PowerMock.suppress(PowerMock.constructor(Configuration.class));
		configuration = Whitebox.newInstance(Configuration.class);
	}

	@Test
	public void testGetInstance_get() {
		// define variables
		Configuration configuration = PowerMock.createMock(Configuration.class);
		configs.put("1", configuration);

		// prepare and replay

		PowerMock.replayAll();

		// invoke and verify

		Configuration result = Configuration.getInstance("1");
		PowerMock.verifyAll();

		// assert
		Assert.assertEquals("The two objects should be same.", configuration, result);
		configs.clear();
	}

	@Test
	public void testGetInstance_new() throws Exception {
		// define variables
		Configuration configuration = PowerMock.createMock(Configuration.class);
		String resourceBundle = "1";
		// prepare and replay
		PowerMock.expectNew(Configuration.class, resourceBundle).andReturn(configuration);
		PowerMock.replayAll();

		// invoke and verify

		Configuration result = Configuration.getInstance("1");
		PowerMock.verifyAll();

		// assert
		Assert.assertEquals("The two objects should be same.", configuration, result);
		configs.clear();
	}

	@Test
	public void testSimple() {
		Assert.assertEquals("", configuration.getProperty("no_exists"));
		Assert.assertEquals("no_exists", configuration.getProperty("no_exists", "no_exists"));

		Assert.assertNotNull(Configuration.getDbType());
		Assert.assertNotNull(Configuration.setKeystore("telenav", "password"));
		Assert.assertNotNull(Configuration.getJarFileNames());
		Assert.assertFalse(Configuration.existConfigurationFile("config"));
		Assert.assertNotNull(Configuration.getClassesDir());
	}

}
