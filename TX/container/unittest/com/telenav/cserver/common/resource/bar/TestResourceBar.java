/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.bar;

import junit.framework.TestCase;

/**
 * TestResourceBar.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-11
 */
public class TestResourceBar extends TestCase{
	private ResourceBar resourceBar = new ResourceBar();
	
	public void testToString(){
		byte[] data = new byte[]{1,2,3,4,5};
		resourceBar.setData(data);
		resourceBar.setType(this.getClass().toString());
		resourceBar.setVersion("1.0.2");
		
		String result = resourceBar.toString();
		assertEquals("type:"+getClass().toString()+",version:1.0.2,binary size:"+data.length,result);
	}
	
	public void testToString_dataNull(){
		byte[] data = new byte[]{1,2,3,4,5};
		resourceBar.setData(data);
		resourceBar.setType(this.getClass().toString());
		resourceBar.setVersion("1.0.2");
		
		String result = resourceBar.toString();
		assertEquals("type:"+getClass().toString()+",version:1.0.2,binary size:"+data.length,result);
	}
	
	public void testSimple(){
		resourceBar.getData() ;
		resourceBar.getVersion();
		resourceBar.toString();
	}

}
