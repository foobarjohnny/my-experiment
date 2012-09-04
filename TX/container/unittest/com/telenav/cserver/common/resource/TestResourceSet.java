/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * TestResourceSet.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-11
 */
public class TestResourceSet extends TestCase{
	private ResourceSet resourceSet = new ResourceSet();
	private Map map = new HashMap();
	@Override
	protected void setUp() throws Exception {
		map.put("1","1");
		map.put("2","1");
		map.put("3","1");
	}
	public void testGetMapping(){
		assertEquals(0,resourceSet.getMapping().size());
		resourceSet.setMapping(map);
		assertEquals(3,resourceSet.getMapping().size());
	}
	
	public void testAddConfig(){
		resourceSet.addConfig("4","4");
		resourceSet.addConfig("3","1");
		assertEquals(2,resourceSet.getMapping().size());
	}
	
	public void testGetValue(){
		resourceSet.addConfig("a","A");
		assertEquals("A",resourceSet.getValue("a"));
		
		resourceSet.setDir("dir");
		assertEquals("dir.A",resourceSet.getValue("a"));
	}

}
