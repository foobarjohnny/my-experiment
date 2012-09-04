/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.device;

import java.util.HashMap;
import java.util.Map;

import org.powermock.api.easymock.PowerMock;

import junit.framework.TestCase;

/**
 * TestDeviceProperties.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestDeviceProperties extends TestCase{
	private Map<String,Object> map = new HashMap<String,Object>();
	DeviceProperties dp;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		map.put("0","0");
		map.put("t","true");
		map.put("1",1);
		map.put("f","false");
		map.put("string","i am a string");
		map.put("stringGroup","first");
		map.put("stringGroup.","second");
		map.put("stringGroup","third");
		map.put("0.0","0");
		map.put("0.en_US","1");
		map.put("0.en","2");
		map.put("0.es","3");
		dp = new DeviceProperties(map);
	}
	
	public void testGetInt(){
		assertEquals(0,dp.getInt("0", -1));
		assertEquals(-1,dp.getInt("-1", -1));
		assertEquals(-1,dp.getInt("t", -1));
	}
	
	public void testGetString(){
		assertEquals("0",dp.getString("0", "no found"));
		assertEquals("no found",dp.getString("-1", "no found"));
		assertEquals("true",dp.getString("t", "no found"));
		assertEquals("i am a string",dp.getString("string", "no found"));
	}
	
	public void testGetBoolean(){
		assertTrue(dp.getBoolean("t",false));
		assertFalse(dp.getBoolean("f",true));
		assertFalse(dp.getBoolean("string",true));
	}
	
	public void testGetStringGroup(){
		Map<String,String> gmap = dp.getStringGroup("stringGroup");
		assertEquals(2,gmap.size());
		assertEquals("third",gmap.get("stringGroup"));
	}
	
	public void testGetGroupString(){
		String result = dp.getGroupString("0","0","1");
		assertEquals("0",result);
	}
	public void testGetGroupString_returnGetKey(){
		String result = dp.getGroupString("stringGroup","0","1");
		assertEquals("third",result);
	}
	public void testGetGroupString_returnDEF(){
		String result = dp.getGroupString("9","0","1");
		assertEquals("1",result);
	}
	public void testGetGroupString_returnLocale(){
        String result = dp.getGroupString("0","en_US");
        assertEquals("1",result);
    }
	public void testGetGroupString_returnLanguage(){
	    String result = dp.getGroupString("0","en_GB");
	    assertEquals("2",result);
	}
	public void testGetGroupString_returnLanguage2(){
        String result = dp.getGroupString("0","es_US");
        assertEquals("3",result);
    }
	public void testToString(){
		String result = dp.toString();
		assertTrue(result.indexOf("0=0")>-1);
		assertTrue(result.indexOf("t=true")>-1);
		assertTrue(result.indexOf("1=1")>-1);
		assertTrue(result.indexOf("f=false")>-1);
		assertTrue(result.indexOf("string=i am a string")>-1);
		assertTrue(result.indexOf("stringGroup=third")>-1);
		assertTrue(result.indexOf("stringGroup.=second")>-1);
		assertTrue(result.indexOf("0.0=0")>-1);
	}
	
	public void testSimple(){
		dp.getInt("1");
		dp.getBooleanDefTrue("");
		dp.getBooleanDefFalse("");
		dp.getBoolean("");
		dp.getString("");
		dp.getGroupString("","");
	}
}
