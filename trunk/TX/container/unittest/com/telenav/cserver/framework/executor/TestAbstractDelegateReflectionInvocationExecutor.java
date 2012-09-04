/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.telenav.cserver.class4test.InvocationExecutor;
import com.telenav.cserver.class4test.SampleBean;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestAbstractDelegateReflectionInvocationExecutor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class TestAbstractDelegateReflectionInvocationExecutor extends TestCase{
	private InvocationExecutor invExecutor = new InvocationExecutor();
	private SampleBean sampleBean = new SampleBean();
	
//	public void testRegisterHandledMethod() throws Exception{
//		//init
//		String methodName = "getValue";
//		Method method = SampleBean.class.getDeclaredMethod(methodName);
//		//invoke 
//		invExecutor.registerHandledMethod(method);
//		//assert
//		Map<String, Method> map = invExecutor.getHandledMethodsMap();
//		assertEquals(1,map.size());
//		assertTrue(map.containsKey(methodName));
//		assertEquals(method,map.get(methodName));
//	}
//	public void testRegisterHandledMethods(){
//		invExecutor.setDelegate(sampleBean);
//	}
	public void testSetDelegate(){
		invExecutor.setDelegate(sampleBean);
		//assert
		Map<String, Method> map = invExecutor.getHandledMethodsMap();
		assertEquals(10,map.size());
		assertTrue(map.containsKey("getValue"));
		assertTrue(map.containsKey("setValue"));
		assertTrue(map.containsKey("toString"));
	}
	
	public void testSetDelegate_this(){
		invExecutor.setDelegate(null);
		//assert
		Map<String, Method> map = invExecutor.getHandledMethodsMap();
		assertEquals(20,map.size());
		assertTrue(map.containsKey("setDelegate"));
		assertTrue(map.containsKey("setDesc"));
	}
	
	public void testDoExecute(){
		invExecutor.setDelegate(null);
		try{
			invExecutor.doExecute(null, null, null);
		}catch(ExecutorException e){
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testSimple(){
		invExecutor.getDesc();
		invExecutor.setDesc("String desc"); 
		try{
			invExecutor.handleUnCaughtExceptions(null, null, null, new Throwable());
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}
	

}
