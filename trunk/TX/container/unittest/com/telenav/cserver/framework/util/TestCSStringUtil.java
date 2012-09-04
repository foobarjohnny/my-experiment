/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import junit.framework.TestCase;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestCSStringUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
public class TestCSStringUtil extends TestCase{
	private CSStringUtil csStringUtil = new CSStringUtil();//for coverage
	public void testIsNotNil(){
		assertFalse(CSStringUtil.isNotNil(null));
		assertFalse(CSStringUtil.isNotNil(""));
		assertFalse(CSStringUtil.isNotNil("  "));
		assertFalse(CSStringUtil.isNotNil("null"));
		assertTrue(CSStringUtil.isNotNil("telenav"));
	}
	
	public void testIsNil(){
		assertTrue(CSStringUtil.isNil(null));
		assertTrue(CSStringUtil.isNil(""));
		assertTrue(CSStringUtil.isNil("  "));
		assertTrue(CSStringUtil.isNil("null"));
		assertFalse(CSStringUtil.isNil("telenav"));
	}
	
	public void testRemoveTheTail(){
		
		assertNull(CSStringUtil.removeTheTail("","telenav"));
		assertNull(CSStringUtil.removeTheTail("telenav",""));
		assertNull(CSStringUtil.removeTheTail("telenav","abc"));
		assertEquals("telenav",CSStringUtil.removeTheTail("telenav.com",".com"));
		
	}
	
	public void testIsEqual(){
		assertFalse(CSStringUtil.isEqual("", "telenav"));
		assertFalse(CSStringUtil.isEqual("telenav", ""));
		assertFalse(CSStringUtil.isEqual("telenav", "t  "));
		assertTrue(CSStringUtil.isEqual("telenav", "telenav"));
	}
	
	public void testParse(){
		char[] in = new char[]{ 'x',
								'\\','t',
								'\\','r',
								'\\','n',
								'\\','f',
								
							   '\\','u','0','1','a','B',
							   '\\','u','a','0','1','2',
							   '\\','u','B','0','1','2'
							  };
		char[] convtBuf = in;
		CSStringUtil.parse(in, 0, in.length, convtBuf);
		
		//no exception is ok.
	}
	
	public void testParse_convtBuf_length_lessThan_len(){
		char[] in = new char[]{ 'x',
				'\\','t',
				'\\','r',
				'\\','n',
				'\\','f',
				'\\','c',
				
			   '\\','u','0','1','a','B',
			   '\\','u','a','0','1','2',
			   '\\','u','B','0','1','2'
			  };
		char[] convtBuf = new char[1];
		CSStringUtil.parse(in, 0, in.length, convtBuf);
		CSStringUtil.parse(in, 0, -1, convtBuf);
	}
	public void testParse_exception(){
		char[] in = new char[]{'\\','u','H','0','1','2'};
		char[] convtBuf = new char[1];
		try{
			CSStringUtil.parse(in, 0, in.length, convtBuf);
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testGetJSONOBject() throws JSONException{
		JSONObject obj = new JSONObject(UnittestUtil.getSampleJsonArray());
		Object result = CSStringUtil.getJSONOBject(obj, "programmers");
		assertNotNull(result);
		assertTrue(result instanceof JSONArray);
	}
	
	public void testGetJSONString() throws JSONException{
		JSONObject obj = new JSONObject(UnittestUtil.getSampleJsonString());
		String name = CSStringUtil.getJSONString(obj, "name");
		String height = CSStringUtil.getJSONString(obj, "height");
		
		assertEquals("Jim", name);
		assertEquals("175(cm)", height);
	}

}
