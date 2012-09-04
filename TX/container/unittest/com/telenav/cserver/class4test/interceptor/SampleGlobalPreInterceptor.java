/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.class4test.interceptor;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;

/**
 * SamplePreInterceptor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-9
 */
public class SampleGlobalPreInterceptor implements Interceptor{
	@Override
	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context) {
		System.out.println("I am a [global Pre] Interceptor: "+this.getClass());
		return InterceptResult.PROCEED;
	}

}
