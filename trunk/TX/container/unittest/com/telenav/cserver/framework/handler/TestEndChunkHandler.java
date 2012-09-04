/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.handler;

import junit.framework.TestCase;

import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * TestEndChunkHandler.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-23
 */
public class TestEndChunkHandler extends TestCase{
	private EndChunkHandler endChunkHandler = new EndChunkHandler();
	
	public void testDoParse(){
		boolean b = endChunkHandler.doParse(null, new ExecutorResponse(), null);
		assertFalse(b);
	}
}
