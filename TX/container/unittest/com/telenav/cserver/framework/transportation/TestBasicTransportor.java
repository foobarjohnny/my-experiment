/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestBasicTransportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-5
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletUtil.class})
public class TestBasicTransportor extends TestCase{
	private BasicTransportor basicTransportor;
	private Transportor transportor;
	@Override
	protected void setUp() throws Exception {
		transportor = PowerMock.createMock(Transportor.class);
		basicTransportor = new BasicTransportor(transportor);
	}
	public void testWrite() throws Exception{
		byte[] responseBytes = new byte[]{1,2,3,4,5};
		int ret = 0;
		byte[] flagBytes = new byte[]{6,7};
		byte[] lenBytes = new byte[]{8,9,10};
		PowerMock.mockStatic(ServletUtil.class);
		EasyMock.expect(ServletUtil.convertNumberToBytes(TransportationFlag.BASIC_FLAG)).andReturn(flagBytes);
		EasyMock.expect(ServletUtil.convertNumberToBytes(responseBytes.length)).andReturn(lenBytes);
		
		EasyMock.expect(transportor.write(flagBytes)).andReturn(0);		
		EasyMock.expect(transportor.write(lenBytes)).andReturn(0);		
		EasyMock.expect(transportor.write(responseBytes)).andReturn(0);		
		
		PowerMock.replayAll();
		ret = basicTransportor.write(responseBytes);
		PowerMock.verifyAll();
		assertEquals(10,ret);
	}
	
	/**
	 * Just for improve coverage rate
	 * @throws TransportorException 
	 */
	public void testSimple() throws TransportorException{
		
		EasyMock.expect(transportor.read()).andReturn(new byte[]{1,2});
		EasyMock.expect(transportor.getInputStream()).andReturn(null);
		EasyMock.expect(transportor.getOutputStream()).andReturn(null);
		EasyMock.expect(transportor.getRemoteAddress()).andReturn("");
		transportor.flush();
		transportor.close();
		PowerMock.replayAll();
		
		basicTransportor.getType();
		basicTransportor.read();
		basicTransportor.getInputStream();
		basicTransportor.getOutputStream();
		basicTransportor.getRemoteAddress();
		basicTransportor.flush();
		basicTransportor.close();
		
		PowerMock.verifyAll();
	}

}
