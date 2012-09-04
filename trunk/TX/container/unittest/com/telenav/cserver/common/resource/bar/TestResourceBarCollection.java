/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.bar;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.util.CSStringUtil;

/**
 * TestResourceBarCollection.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSStringUtil.class})
public class TestResourceBarCollection extends TestCase{
	private ResourceBarCollection resourceBarCollection = new ResourceBarCollection();
	private List<ResourceBar> list = new ArrayList<ResourceBar>();
	private String testType = "testType";
	private ResourceBar resourceBar1 = new ResourceBar();
	private ResourceBar resourceBar2 = new ResourceBar();
	private ResourceBar resourceBar3 = new ResourceBar();
	@Override
	protected void setUp() throws Exception {
		
		
		resourceBar1.setType(testType);
		resourceBar2.setType(testType);
		resourceBar3.setType(testType);
		
		list.add(resourceBar1);
		list.add(resourceBar2);
		list.add(resourceBar3);
		
		resourceBarCollection.setBarList(list);
	}
	public void testGetResourceBar(){
		//init
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(testType)).andReturn(true);
		EasyMock.expect(CSStringUtil.removeTheTail(testType, ResourceBarCollection.VERSION)).andReturn(testType);
		
		PowerMock.replayAll();
		//invoke
		ResourceBar result = resourceBarCollection.getResourceBar(testType);
		
		PowerMock.verifyAll();
		//assert
		assertEquals(resourceBar1,result);
	}
	
	public void testGetResourceBar_null(){
		//init
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(testType)).andReturn(false).times(3);
		
		PowerMock.replayAll();
		//invoke
		ResourceBar result = resourceBarCollection.getResourceBar("");
		
		PowerMock.verifyAll();
		//assert
		assertNull(result);
	}

}
