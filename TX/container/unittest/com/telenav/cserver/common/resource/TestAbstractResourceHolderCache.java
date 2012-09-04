/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import static junit.framework.Assert.*;


import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestAbstractResourceHolder for cache issue
 * @author kwwang
 *
 */
public class TestAbstractResourceHolderCache {
	
	private UserProfile userProfile;
	
	@Before
	public void init()
	{
		ResourceFactory.getInstance();
		userProfile = new UserProfile();
		//set properties
		userProfile.setProgramCode("ATTNAVPROG");
		userProfile.setPlatform("IPHONE");
		userProfile.setVersion("7.0.01");
		userProfile.setDevice("sunDevice");
		userProfile.setProduct("ATT_NAV");
	}
	
	@Test
	public void get_multipleDeviceShareSameCache()
	{
		MockTestHolder holder=UnittestUtil.createMockTestHolder();
		
		ResourceContent rc=(ResourceContent)holder.getResourceContent(userProfile, null);
		verifyTheCacheContent(rc);
		String refLoadingPath=holder.getLoadingPath(userProfile, null);
		
		//change to another device
		userProfile.setDevice("sonyDevice");
		rc=(ResourceContent)holder.getResourceContent(userProfile, null);
		verifyTheCacheContent(rc);
		
		String newLoadingPath=holder.getLoadingPath(userProfile, null);
		assertEquals(refLoadingPath, newLoadingPath);
		assertEquals("device//ATTNAVPROG/IPHONE/7_0_01//ATT_NAV/default//test", refLoadingPath);
		assertEquals(1, holder.getMap().size());
		
		//load another test.properties
		userProfile.setDevice("9800");
		rc=(ResourceContent)holder.getResourceContent(userProfile, null);
		assertNotNull(rc);
		assertNotNull(rc.getObject());
		Map map=(Map)rc.getObject();
		assertEquals(2, map.size());
		assertEquals("value1", map.get("key1"));
		assertEquals("value2", map.get("key2"));
		
		
		//two cache now
		assertEquals(2, holder.getMap().size());
	}
	
	private void verifyTheCacheContent(ResourceContent rc)
	{
		assertNotNull(rc);
		assertNotNull(rc.getObject());
		Map map=(Map)rc.getObject();
		assertEquals(3, map.size());
		assertEquals("value1", map.get("key1"));
		assertEquals("value2", map.get("key2"));
		assertEquals("value3", map.get("key3")); 
	}
	
	
	@Test
	public void get_noFileLoadAndGetCached()
	{
		MockTestHolder holder=UnittestUtil.createMockTestHolder();
		userProfile.setPlatform("NonexistentPlatForm");
		ResourceContent rc=(ResourceContent)holder.getResourceContent(userProfile, null);
		assertNotNull(rc);
		assertNull(rc.getObject());
		
		String refLoadingPath=holder.getLoadingPath(userProfile, null);
		assertNull(refLoadingPath);
		assertEquals(0, holder.getMap().size());
	}
}
