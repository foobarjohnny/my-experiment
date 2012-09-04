package com.telenav.cserver.exception;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 	mmli@telenavsoftware.com, 2011/12/25, telenav
 * 	unit test for TnException 
 */
public class TnExceptionTest {
	public static final String parameterOfInt = "int";               // never use hard code in your program
	public static final String parameterOfString = "string";
	public static final String parameterOfThrowable = "throwalbe";
	public static final String unknowError = "unknown reason from %s";
	
	TnException instanceForTest = new TnException();
	HashMap<String, Object> dataCenter = new HashMap();
	
	@Before
	public void setUp() throws Exception {
		// setup data before testing
		dataCenter.put(TnExceptionTest.parameterOfInt, Integer.valueOf(0));
		dataCenter.put(TnExceptionTest.parameterOfString, String.valueOf("just for test"));
		dataCenter.put(TnExceptionTest.parameterOfThrowable, new Throwable("ignore it"));
		
	}

	@After
	public void tearDown() throws Exception {
		// clear work after each test
		dataCenter.clear();
	}

	@Test
	public void testTnException() {
		try{
			Class<?>[] parameter = {};
			Constructor<?> defaultConstructor = instanceForTest.getClass().getConstructor(parameter);
			defaultConstructor.newInstance();
		}catch(NoSuchMethodException e){
			fail("the default constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionString() {
		try{
			Class<?>[] parameter = {String.class};
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfString)};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionThrowable() {
		try{
			Class<?>[] parameter = {Throwable.class};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfThrowable)};			
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionInt() {
		try{
			Class<?>[] parameter = {int.class};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfInt)};			
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionStringIntThrowable() {
		try{
			Class<?>[] parameter = {String.class, int.class, Throwable.class};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfString),
									 dataCenter.get(TnExceptionTest.parameterOfInt),
									 dataCenter.get(TnExceptionTest.parameterOfThrowable),
									};			
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionStringInt() {
		try{
			Class<?>[] parameter = {String.class, int.class};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfString),
					 				 dataCenter.get(TnExceptionTest.parameterOfInt),
									};			
			constructor.newInstance(parameterObj);			
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testTnExceptionIntThrowable() {
		try{
			Class<?>[] parameter = {int.class, Throwable.class};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			Object[] parameterObj = {dataCenter.get(TnExceptionTest.parameterOfInt),
	 				 				 dataCenter.get(TnExceptionTest.parameterOfThrowable),
									};			
			constructor.newInstance(parameterObj);	
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testGetCode() {
		String methodName = "getCode";
		try{
			Class<?>[] parameter = {};
			Object[] parameterObj = {};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			m.invoke(instanceForTest, parameterObj);
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : TnExceptionTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

}
