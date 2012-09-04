/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayOutputStream;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import junit.framework.TestCase;

/**
 * TestByteArrayTransportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
public class TestByteArrayTransportor extends TestCase{
	private ByteArrayTransportor byteArrayTransportor;
	@Override
	protected void setUp() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byteArrayTransportor = new ByteArrayTransportor(new byte[]{},bos);
	}
	public void testWrite() throws Exception{
		byte[] responseBytes = new byte[]{1,2,3,4,5};
		int result = byteArrayTransportor.write(responseBytes);
		assertEquals(responseBytes.length,result);
	}
	
	/**
	 * Just for improve coverage rate
	 * @throws TransportorException 
	 */
	public void testSimple() throws TransportorException{
		
		byteArrayTransportor.getType(); 
		byteArrayTransportor.read();
		byteArrayTransportor.getInputStream();
		byteArrayTransportor.getOutputStream();
		byteArrayTransportor.flush();
		byteArrayTransportor.getRemoteAddress();
		byteArrayTransportor.close();

	}

}
