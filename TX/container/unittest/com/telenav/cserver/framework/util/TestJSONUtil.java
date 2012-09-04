/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestJSONUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JSONUtil.class})
public class TestJSONUtil extends TestCase{
	
	private JSONUtil jSONUtil = new JSONUtil();//for coverate rate
	
	public void testGetInt(){
		assertEquals(1,JSONUtil.getInt(null, 1));
		assertEquals(1,JSONUtil.getInt("", 1));
		assertEquals(2,JSONUtil.getInt("2 ", 1));
	}
	
	public void testDouble(){
		assertEquals(1.0,JSONUtil.getDouble(null, 1.0));
		assertEquals(1.0,JSONUtil.getDouble("", 1.0));
		assertEquals(2.0,JSONUtil.getDouble("2.0", 1.0));
	}
	
	public void testGetFlagBoolean(){
		assertFalse(JSONUtil.getFlagBoolean(null, false));
		assertFalse(JSONUtil.getFlagBoolean("2", false));
		assertTrue(JSONUtil.getFlagBoolean("1", false));
	}
	
	public void testGetBoolean(){
		assertFalse(JSONUtil.getBoolean(null, false));
		assertFalse(JSONUtil.getBoolean("tr", false));
		
		String aa = "a_string"; 
		System.setProperty(aa, "true"); 
		assertTrue(JSONUtil.getBoolean(aa, false));
	}
	
	public void testGetJSONOBject() throws JSONException{
		assertNull(JSONUtil.getJSONOBject(null, ""));
		
		JSONObject obj = UnittestUtil.getSampleJSONObject();
		assertEquals("Jim",JSONUtil.getJSONOBject(obj, "name"));
	}
	
	public void testGetJSONString() throws JSONException{
		assertEquals("",JSONUtil.getJSONString(null, ""));
		
		JSONObject obj = UnittestUtil.getSampleJSONObject();
		assertEquals("Jim",JSONUtil.getJSONString(obj, "name"));
	}
	/**
	 * getJSONOBject(jsonObj, key) == null
	 */
	public void testGetJSONArray_exception(){
		//define variables
		PowerMock.mockStaticPartial(JSONUtil.class, "getJSONOBject");
		
		//prepare and replay
		EasyMock.expect(JSONUtil.getJSONOBject(null, "")).andReturn(null);
		PowerMock.replayAll();

		//invoke and verify
		try {
			JSONUtil.getJSONArray(null, "");
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
		PowerMock.verifyAll();
	}
	/**
	 * obj instanceof JSONArray == false
	 */
	public void testGetJSONArray_exception1(){
		//define variables
		PowerMock.mockStaticPartial(JSONUtil.class, "getJSONOBject");
		
		//prepare and replay
		EasyMock.expect(JSONUtil.getJSONOBject(null, "")).andReturn("1234");
		PowerMock.replayAll();

		//invoke and verify
		try {
			JSONUtil.getJSONArray(null, "");
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
		PowerMock.verifyAll();
	}
	/**
	 * Failed to parse JSONString ....
	 */
	public void testGetJSONArray_exception2(){
		//define variables
		String str = UnittestUtil.getJSONRequestJsonArray();
		try{
			JSONObject obj = new JSONObject(str);
			JSONObject[] result = JSONUtil.getJSONArray(obj, "gps");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}
	public void testGetJSONArray() throws Exception {
		//define variables
		String str = UnittestUtil.getJSONRequestJsonArray();
		JSONObject obj = new JSONObject(str);
		JSONObject[] result = JSONUtil.getJSONArray(obj, "JSONRequest");

		//assert
		assertEquals(1,result.length);
	}

}
