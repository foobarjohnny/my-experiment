/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.class4test;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.handler.DataHandler;

/**
 * SampleDataHandler.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
public class SampleDataHandler implements DataHandler{
	@Override
	public boolean parse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, boolean isContinue, CliTransaction cli){
		return true;
	}
	public String getHandlerType(){
		return "executorType";
	}
}
