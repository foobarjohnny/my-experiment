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
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.common.resource.ptn.PtnProperties;

/**
 * TestGeneralPtnPropertyManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PtnProperties.class)
public class TestGeneralPtnPropertyManager extends TestCase{
	private GeneralPtnPropertyManager generalPtnPropertyManager = new GeneralPtnPropertyManager();
	private static final String PTN_RESOURCE_ROOT_PATH = "/device/ptn_resource/";
	
	public void testGetPtnResourcePath(){
		PowerMock.mockStatic(PtnProperties.class);
		EasyMock.expect(PtnProperties.get(new HashMap(),null)).andReturn("ptnFolder");
		PowerMock.replayAll();
		
		String result = generalPtnPropertyManager.getPtnResourcePath(null);
		PowerMock.verifyAll();
		
		assertEquals(PTN_RESOURCE_ROOT_PATH+"ptnFolder/",result);
	}
	
	public void testGetPtnResourcePath_returnNull(){
		PowerMock.mockStatic(PtnProperties.class);
		EasyMock.expect(PtnProperties.get(new HashMap(),null)).andReturn(null);
		PowerMock.replayAll();
		
		String result = generalPtnPropertyManager.getPtnResourcePath(null);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}

}
