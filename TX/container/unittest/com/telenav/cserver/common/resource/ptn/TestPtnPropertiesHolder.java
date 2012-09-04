/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ptn;

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

/**
 * TestPtnPropertiesHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceFactory.class,PtnProperties.class})
public class TestPtnPropertiesHolder extends TestCase{
	private PtnPropertiesHolder ptnPropertiesHolder = new PtnPropertiesHolder();
	
	public void testCreateObject() throws Exception{
		
		//1. prepare
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(ptnPropertiesHolder,null, null)).andReturn(new HashMap());
		PowerMock.replayAll();
		
		//2. run the test method
		ResourceContent rc = ptnPropertiesHolder.createObject(null,null, null);
		
		//3. verify
		PowerMock.verifyAll();
		assertNotNull(rc.getProperty("ptn"));
	}
	

}
