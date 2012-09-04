/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.class4test;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * TestExecutor.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-26
 */
public class SampleExecutor extends AbstractExecutor {
	public SampleExecutor() {
		// TODO Auto-generated constructor stub
	}
	public SampleExecutor(String type) {
		// TODO Auto-generated constructor stub
		this.setExecutorType(type);
	}
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException {
		System.out.println("I am a test Executor");
	}
	String desc;

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
