/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cli.client.CliTransaction;

/**
 * TestCliTransactionFactory.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CliThreadLocalUtil.class,CliClientConfig.class})
public class TestCliTransactionFactory extends TestCase{
	
	private CliTransactionFactory ctf = new CliTransactionFactory();
	
	public void testGetInstance(){
		//assert
		assertTrue(getInsctance(true) instanceof CliTransaction);
		assertTrue(getInsctance(false) instanceof CliFakeTransaction);
	}
	
	private Object getInsctance(boolean b){
		//prepare and replay
		PowerMock.mockStatic(CliClientConfig.class);
		PowerMock.mockStatic(CliThreadLocalUtil.class);
		EasyMock.expect(CliThreadLocalUtil.getUserProfile()).andReturn(null);
		EasyMock.expect(CliThreadLocalUtil.getExecutorType()).andReturn("");
		EasyMock.expect(CliClientConfig.getCliClientConfig(null, "")).andReturn(b);
		PowerMock.replayAll();
		
		//invoke and verify
		Object obj = CliTransactionFactory.getInstance("abc");
		PowerMock.verifyAll();
		
		return obj;
	}

}
