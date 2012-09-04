/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * TestClassUtils.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ClassUtils.class})
public class TestClassUtils extends TestCase{
	private ClassUtils classUtils = new ClassUtils();//for coverage rate
		
	public void testGetUrl(){
		URL url = ClassUtils.getUrl("resource/html.xml");
		URL url_notExists = ClassUtils.getUrl("resource/html_notExists.xml");
		assertNotNull(url);
		assertTrue(url.getPath().indexOf("/resource/html.xml") > -1);
		assertNull(url_notExists);
	}
	public void testIsExist() {
		assertTrue(ClassUtils.isExist("resource/html.xml"));
		assertFalse(ClassUtils.isExist("resource/html_notExists.xml"));
	}
	
	public void testGetPrimitiveClass(){
		assertEquals(ClassUtils.getPrimitiveClass("int"),int.class);
		assertEquals(ClassUtils.getPrimitiveClass("byte"),byte.class);
		assertEquals(ClassUtils.getPrimitiveClass("char"), char.class);
		assertEquals(ClassUtils.getPrimitiveClass("short"), short.class);
		assertEquals(ClassUtils.getPrimitiveClass("long"), long.class);
		assertEquals(ClassUtils.getPrimitiveClass("double"), double.class);
		assertEquals(ClassUtils.getPrimitiveClass("float"), float.class);
		assertEquals(ClassUtils.getPrimitiveClass("boolean"), boolean.class);
        assertNull(ClassUtils.getPrimitiveClass("null"));
	}

	
	public void testNewObject(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", "Jim");
		map.put("gender", "male");
		map.put("age", 20);
		
		TestClassUtils tcu = ClassUtils.newObject(this.getClass(), map);
		assertNotNull(tcu);
	}
	
}
