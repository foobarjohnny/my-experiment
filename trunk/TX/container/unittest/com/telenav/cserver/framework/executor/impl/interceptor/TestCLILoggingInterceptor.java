/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;

import junit.framework.TestCase;

/**
 * TestCLILoggingInterceptor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class TestCLILoggingInterceptor extends TestCase{
	private CLILoggingInterceptor cLILoggingInterceptor = new CLILoggingInterceptor();
	private ExecutorRequest request = PowerMock.createMock(ExecutorRequest.class);
	private ExecutorResponse response = PowerMock.createMock(ExecutorResponse.class);
	public void testIntercept(){
		PowerMock.replayAll();
		InterceptResult result;
		result = cLILoggingInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertNull(result);
	}

}
