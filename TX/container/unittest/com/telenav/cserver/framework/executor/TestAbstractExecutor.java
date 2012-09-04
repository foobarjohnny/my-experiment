/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

import junit.framework.TestCase;

/**
 * TestAbstractExecutor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
public class TestAbstractExecutor extends TestCase{
	private  TestableClass tc = new TestableClass();
	public void testExecute(){
		try {
			tc.execute(null, null, null);
		} catch (ExecutorException e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	//for improving test coverage
	public void testSimple(){
		tc.getExecutorType();
		tc.setExecutorType("");
	}
	
	private class TestableClass extends AbstractExecutor{
		@Override
		public void doExecute(ExecutorRequest request,
				ExecutorResponse response, ExecutorContext context)
				throws ExecutorException {
			throw new ExecutorException(UTConstant.DO_NOT_WORRY_EXCEPTION);
		}
	}
}
