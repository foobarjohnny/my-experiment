/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import java.lang.reflect.Method;
import java.util.PropertyResourceBundle;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.cserver.framework.util.LiveStatsLogger;

/**
 * TestLiveStatInterceptor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LiveStatInterceptor.class, LiveStatsLogger.class})
@SuppressStaticInitializationFor("com.telenav.cserver.framework.util.LiveStatsLogger")
public class TestLiveStatInterceptor extends TestCase{
	private ExecutorRequest request;
	private ExecutorResponse response;
	LiveStatInterceptor liveStatInterceptor = new LiveStatInterceptor();
	LiveStatsLogger ls;
	
	@Override
	protected void setUp() throws Exception {
		request = PowerMock.createMock(ExecutorRequest.class);
		response = PowerMock.createMock(ExecutorResponse.class);
		ls = PowerMock.createMock(LiveStatsLogger.class);
	}
	public void testInterceptor()throws Exception{
		PowerMock.mockStatic(PropertyResourceBundle.class);
		PowerMock.mockStatic(LiveStatsLogger.class);
		//PowerMock.expectNew(LiveStatsLogger.class).andReturn(ls);
//		ls.setApp("a");
//		EasyMock.expect(ls.getDefaultCount()).andReturn("b");
//		ls.setSuccessCount("b");
//		ls.send();
		EasyMock.expect(response.getStatus()).andReturn(ExecutorResponse.STATUS_EXCEPTION);
		PowerMock.replayAll();
		//liveStatInterceptor.setApp("a");
		InterceptResult ret = liveStatInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.PROCEED,ret);
	}
	
	public void testInterceptorApp_routing_tripList()throws Exception{
		PowerMock.mockStatic(PropertyResourceBundle.class);
		PowerMock.mockStatic(LiveStatsLogger.class);
		//PowerMock.expectNew(LiveStatsLogger.class).andReturn(ls);
		//ls.setApp("routing");
		//EasyMock.expect(request.getExecutorType()).andReturn("Static_Route");
		//ls.setEx1("trip");
		//EasyMock.expect(ls.getDefaultCount()).andReturn("b");
		//ls.setSuccessCount("b");
		//ls.send();
		EasyMock.expect(response.getStatus()).andReturn(ExecutorResponse.STATUS_EXCEPTION);
		PowerMock.replayAll();
		//liveStatInterceptor.setApp("routing");
		InterceptResult ret = liveStatInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.PROCEED,ret);
	}
	
	public void testInterceptorApp_routing_devList()throws Exception{
		PowerMock.mockStatic(PropertyResourceBundle.class);
		PowerMock.mockStatic(LiveStatsLogger.class);
		//PowerMock.expectNew(LiveStatsLogger.class).andReturn(ls);
		//ls.setApp("routing");
		//EasyMock.expect(request.getExecutorType()).andReturn("Check_Deviation");
		//ls.setEx1("dev");
		//EasyMock.expect(ls.getDefaultCount()).andReturn("b");
		//ls.setSuccessCount("b");
		//ls.send();
		EasyMock.expect(response.getStatus()).andReturn(ExecutorResponse.STATUS_EXCEPTION);
		PowerMock.replayAll();
		//liveStatInterceptor.setApp("routing");
		InterceptResult ret = liveStatInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.PROCEED,ret);
	}
	public void testInterceptorApp_routing_else()throws Exception{
		PowerMock.mockStatic(PropertyResourceBundle.class);
		PowerMock.mockStatic(LiveStatsLogger.class);
		//PowerMock.expectNew(LiveStatsLogger.class).andReturn(ls);
		//ls.setApp("routing");
		//EasyMock.expect(request.getExecutorType()).andReturn("unknown");
		//ls.setEx1("more");
		//EasyMock.expect(ls.getDefaultCount()).andReturn("b");
		//ls.setSuccessCount("b");
		//ls.send();
		EasyMock.expect(response.getStatus()).andReturn(ExecutorResponse.STATUS_EXCEPTION);
		PowerMock.replayAll();
		//liveStatInterceptor.setApp("routing");
		InterceptResult ret = liveStatInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.PROCEED,ret);
	}
	public void testHasElement() throws Exception{
		
		String[] arr = new String[]{"aaa","bbb","ccc","ddd"};
		boolean result,result1;
		
		Method method = LiveStatInterceptor.class.getDeclaredMethod("hasElement", String[].class,String.class);
		method.setAccessible(true);
		result = Boolean.parseBoolean(method.invoke(liveStatInterceptor, arr,"aaa").toString());
		result1 = Boolean.parseBoolean(method.invoke(liveStatInterceptor, arr,"aaa3").toString());
		
		//if use Whitebox to invoke the method hasElement(), there will be a error
		//when run ant unittest
//		result = Whitebox.invokeMethod(liveStatInterceptor, "hasElement",arr,"aaa");
//		result1 = Whitebox.invokeMethod(liveStatInterceptor, "hasElement",arr,"aaa3");
		assertTrue(result);
		assertFalse(result1);
	}
	
	public void testSimple(){
		liveStatInterceptor.getApp();
	}

}
