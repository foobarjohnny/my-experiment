/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ptn;

import java.util.Map;

import junit.framework.TestCase;

/**
 * TestPtnProperties.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-29
 */
public class TestPtnProperties extends TestCase {
	Map<String, String> map = new java.util.HashMap<String, String>();
	private PtnProperties prop = new PtnProperties(map);

	@Override
	protected void setUp() throws Exception {
		map.put("4083063896", "att_beta");
		map.put("409.......", "sprint_beta");
		map.put("...567....", "nextel_beta");
	}

	public void testGet() {
		assertEquals("att_beta", prop.get("4083063896"));
		assertEquals("sprint_beta", prop.get("4093063896"));
		assertEquals("nextel_beta", prop.get("4075673896"));
		assertNull(prop.get("4063063896"));
	}

	public void testSimple() {
		prop.put("1", "one");
		prop.get(map,null);
		prop.toString();
		
		PtnProperties prop = new PtnProperties(null);
	}
}
