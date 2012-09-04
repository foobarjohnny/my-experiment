/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestResourceSetManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceFactory.class)
@SuppressStaticInitializationFor("com.telenav.cserver.common.resource.ResourceFactory")
public class TestResourceSetManager extends TestCase {
	private ResourceSetManager resourceSetManager = new ResourceSetManager();
	public void testAddResourceSet(){
		
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource("1.1",ResourceFactory.TYPE_RESOURCE_BUNDLE)).andReturn(new HashMap());
		PowerMock.replayAll();
		resourceSetManager.addResourceSet("key", "1.1");
		PowerMock.verifyAll();
		assertNotNull(resourceSetManager.getResourceSet("key"));
	}

}
