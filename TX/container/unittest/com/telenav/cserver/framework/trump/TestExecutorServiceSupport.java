/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import junit.framework.TestCase;

/**
 * TestExecutorServiceSupport.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
public class TestExecutorServiceSupport extends TestCase{
	
	private ExecutorServiceSupport executorServiceSupport = new ExecutorServiceSupport ();
	private TestableClass run = new TestableClass();
	
	public void testMethod(){
		ExecutorServiceSupport.sumbit(run);
		ExecutorServiceSupport.sumbitWithSecondPeriod(run,60);
		ExecutorServiceSupport.sumbitWithMinutePeriod(run,2);
		ExecutorServiceSupport.sumbitWithHourPeriod(run,1);
		ExecutorServiceSupport.sumbitWithHourPeriod(run,1,1);
		ExecutorServiceSupport.stop();
		ExecutorServiceSupport.stopNow();
	}
	
	private class TestableClass implements Runnable{
		@Override
		public void run() {
			
		}
	}

}
