/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.class4test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.framework.executor.AbstractDelegateReflectionInvocationExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.unittestutil.UTConstant;

/**
 * InvocationExecutor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class InvocationExecutor extends AbstractDelegateReflectionInvocationExecutor{
	@Override
	public List<String> beforeInvocation(ExecutorRequest req,
			ExecutorResponse resp, ExecutorContext context) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("not_exists_method");
		list.add("getName");
		list.add("setName");
		return list;
	}
	@Override
	public Object[] getInvocationParams(ExecutorRequest req,
			ExecutorResponse resp, ExecutorContext context) {
		Object[] obj = new Object[]{"telenav"};
		return obj;
	}
	@Override
	public void registerHandledMethod(Method m) {
		handledMethods.put(m.getName(),m);
	}
	
	public Map<String, Method> getHandledMethodsMap(){
		return handledMethods;
	}
	
	public String getName(String arg){
		System.out.println("arg is : " + arg);
		return "hello " + arg;
	}
	public String setName(String arg) throws Exception{
		System.out.println("arg is : " + arg);
		throw new Exception(UTConstant.DO_NOT_WORRY_EXCEPTION);
	}
	
}
