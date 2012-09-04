/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.cserver.class4test.SampleBean;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestSizeOfObjectImpl.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
public class TestSizeOfObjectImpl extends TestCase{
	
	private SizeOfObjectImpl sizeOfObjectImpl = new SizeOfObjectImpl();
	private Object nullObj = null;
	
	public void testHumanReadable_byte(){
        String result = sizeOfObjectImpl.humanReadable(52);
        assertEquals("52Byte",result);
    }
	
	public void testHumanReadable_Kbyte(){
		String result = sizeOfObjectImpl.humanReadable(5000);
		assertEquals("4.88KByte",result);
	}
	
	public void testHumanReadable_Mbyte(){
        String result = sizeOfObjectImpl.humanReadable(4*1024*1024);
        assertEquals("4MByte",result);
    }
	
	public void testHumanReadable_Gbyte(){
        String result = sizeOfObjectImpl.humanReadable(1024*1024*1024);
        assertEquals("1GByte",result);
    }
	
	public void testIndent() throws Exception{
		String result;
		result = Whitebox.invokeMethod(SizeOfObjectImpl.class,"indent",3);
		
		assertEquals("      ",result);
	}
	public void testDeepSizeOf() throws Exception{
		//[1] prepare for test
		long result,result1,result2;
		SizeOfObjectImpl.turnOnDebug();
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("1","1");
		//[2] invoke method
		result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "deepSizeOf", nullObj,null,3).toString());
		//doneObj.contains first argument
		result1 = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "deepSizeOf", "1",map,3).toString());
		
		result2 = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "deepSizeOf", new String[]{"hello","world"},map,3).toString());
		//[3] assert
		assertEquals(0,result);
		assertEquals(0,result1);
		assertEquals(168,result2);
	}
	public void testSizeOf() throws Exception{
		long result;
		result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "sizeOf", nullObj).toString());
		assertEquals(0,result);
		
		//compute a instance of SampleBean and set SKIP_STATIC_FIELD = true
		SampleBean sampleBean = new SampleBean();
		SizeOfObjectImpl.skipStaticField(true);
		result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "sizeOf", sampleBean).toString());
		assertEquals(16,result);
		
		//compute a instance of SampleBean and set SKIP_FINAL_FIELD = true
		SizeOfObjectImpl.skipStaticField(false);
		SizeOfObjectImpl.skipFinalField(true);
		result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "sizeOf", sampleBean).toString());
		assertEquals(16,result);
		
	}
	
	public void testGetSizeOfPrimitiveType() {
		long result = -1;
		try {
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Boolean.TYPE).toString());
			assertEquals(1,result);
			
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Character.TYPE).toString());
			assertEquals(2,result);
			
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Byte.TYPE).toString());
			assertEquals(1,result);

			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Short.TYPE).toString());
			assertEquals(2,result);
			
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Integer.TYPE).toString());
			assertEquals(4,result);
			
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Long.TYPE).toString());
			assertEquals(8,result);

			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Float.TYPE).toString());
			assertEquals(4,result);
			
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Double.TYPE).toString());
			assertEquals(8,result);

			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", Void.TYPE).toString());
			assertEquals(1,result);
	        
			result = Long.parseLong(Whitebox.invokeMethod(sizeOfObjectImpl, "getSizeOfPrimitiveType", this.getClass()).toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			UnittestUtil.printExceptionMsg(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testIsComputable() throws Exception{
		//prepare for test
		SizeOfObjectImpl.skipStaticField(true);
		SizeOfObjectImpl.skipFinalField(true);
		Field field_static = SampleBean.class.getField("userProfile");
		Field field_final = SampleBean.class.getField("tnContext");
		
		assertFalse(Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isComputable", field_static).toString()));
		assertFalse(Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isComputable", field_final).toString()));
	}
	public void testIsSharedFlyweight() throws Exception{
		Object obj = null;
		boolean result;
		//obj instanceof Comparable == False
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertFalse(result);
		
		//obj instanceof Enum
		obj = InterceptResult.ERROR;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof String
		obj = "123";
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Boolean
		obj = true;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		obj = false;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Integer
		obj = 1;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Short
		obj = (short) 2;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Byte
		obj = (byte) 2;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Long
		obj = (long) 2;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
		//obj instanceof Character
		obj = (char) 97;
		result = Boolean.valueOf(Whitebox.invokeMethod(sizeOfObjectImpl, "isSharedFlyweight", obj).toString());
		assertTrue(result);
		
	}
	/**
	 * for coverage rate
	 */
	public void testSimple(){
		SizeOfObjectImpl.setMinSizeToLog(0);
		SizeOfObjectImpl.skipFlyweightObject(false);
		try {
			Whitebox.invokeMethod(sizeOfObjectImpl, "print", "strings");
			Whitebox.invokeMethod(sizeOfObjectImpl, "print", nullObj);
			
			Whitebox.invokeMethod(sizeOfObjectImpl, "print", "strings",this.getClass());
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
		
		
		try{
			SizeOfObjectImpl.setLogOutputStream(null);
		}catch(IllegalArgumentException e){
			UnittestUtil.printExceptionMsg(e);
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		SizeOfObjectImpl.setLogOutputStream(System.out);
		
		SizeOfObjectImpl.turnOffDebug();
		assertFalse(Boolean.valueOf(Whitebox.getInternalState(SizeOfObjectImpl.class, "debug").toString()));
	}
	
	
	
}
