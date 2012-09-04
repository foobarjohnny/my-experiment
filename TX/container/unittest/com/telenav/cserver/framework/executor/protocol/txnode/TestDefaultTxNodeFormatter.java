/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import junit.framework.TestCase;
import static org.easymock.EasyMock.expect;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;
/**
 * 
 * TestDefaultTxNodeFormatter.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-26
 */
public class TestDefaultTxNodeFormatter extends TestCase{
	private DefaultTxNodeFormatter defaultTxNodeFormatter;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void testFormat() throws Exception{
		ExecutorResponse[] responses =new ExecutorResponse[1];
		ExecutorResponse response = PowerMock.createMock(ExecutorResponse.class);
		responses[0] = response;
		TxNode node = new TxNode();
		
		expect(response.getExecutorType()).andReturn("==ExecutorType==");
		expect(response.getErrorMessage()).andReturn("==ErrorMessage==");
		expect(response.getStatus()).andReturn(100);
		
		PowerMock.replay(response);
		
		DefaultTxNodeFormatter defaultTxNodeFormatter = new DefaultTxNodeFormatter();
		
		defaultTxNodeFormatter.format(node, responses);
				
		PowerMock.verify(response);
		
		assertEquals("==ExecutorType==",node.msgAt(0));
		assertEquals("==ErrorMessage==",node.msgAt(1));
		assertEquals(100,node.valueAt(1));
	}

}
