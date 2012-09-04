/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;

/**
 * TestLoggingInterceptor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class TestLoggingInterceptor extends TestCase{
	private LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
	private ExecutorRequest request = PowerMock.createMock(ExecutorRequest.class);
	private ExecutorResponse response = PowerMock.createMock(ExecutorResponse.class);
	
	@Override
	protected void setUp() throws Exception {
		Whitebox.setInternalState(loggingInterceptor, "log", Logger.getLogger(LoggingInterceptor.class));
	}
	public void testInterceptArgsNotNull(){
		PowerMock.replayAll();
		InterceptResult result;
		result = loggingInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.PROCEED,result);
	}
	
	public void testInterceptArgsAreNull(){
		InterceptResult result;
		result = loggingInterceptor.intercept(null, null, null);
		assertEquals(InterceptResult.PROCEED,result);
	}

}
