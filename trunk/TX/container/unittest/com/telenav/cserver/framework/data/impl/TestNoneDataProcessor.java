/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.data.impl;

import junit.framework.TestCase;

/**
 * TestNoneDataProcessor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
public class TestNoneDataProcessor extends TestCase{
	NoneDataProcessor ndp = new NoneDataProcessor();
	
	public void testProcess(){
		byte[] originalData = new byte[]{49,50,51};
		ndp.getType();
		assertEquals(originalData, ndp.process(originalData));
	}
}
