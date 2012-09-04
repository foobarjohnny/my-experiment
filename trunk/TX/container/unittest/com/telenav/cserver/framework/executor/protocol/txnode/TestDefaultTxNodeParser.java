/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;

import junit.framework.TestCase;

/**
 * TestDefaultTxNodeParser.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class TestDefaultTxNodeParser extends TestCase{
	private DefaultTxNodeParser defaultTxNodeParser = new DefaultTxNodeParser();
	public void testParse() throws ExecutorException{
		ExecutorRequest[] requests = defaultTxNodeParser.parse(null);
		assertEquals(1,requests.length);
		assertTrue(requests[0] instanceof ExecutorRequest);
	}

}
