/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;


import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.matcher.MatchBox;

import junit.framework.TestCase;

/**
 * TestCompressedTransportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-5
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletUtil.class})
public class TestCompressedTransportor extends TestCase{
	private CompressedTransportor compressedTransportor;
	private Transportor transportor;
	@Override
	protected void setUp() throws Exception {
		transportor = PowerMock.createMock(Transportor.class);
		compressedTransportor = new CompressedTransportor(transportor);
	}
	public void testWrite()throws Exception{
		byte[] responseBytes = new byte[]{1,2,3,4,5};
		int ret = 0;
		
		byte[] flagBytes = new byte[]{6,7};
		byte[] lenBytes = new byte[]{8,9,10};
		byte[] bytesToWrite = getBytesToWrite(responseBytes);
		int len = bytesToWrite.length;
		PowerMock.mockStatic(ServletUtil.class);
		EasyMock.expect(ServletUtil.convertNumberToBytes(TransportationFlag.GZIP_FLAG)).andReturn(flagBytes);
		EasyMock.expect(ServletUtil.convertNumberToBytes(len)).andReturn(lenBytes);
		
		EasyMock.expect(transportor.write(flagBytes)).andReturn(1);		
		EasyMock.expect(transportor.write(lenBytes)).andReturn(2);		
		EasyMock.expect(transportor.write(MatchBox.byteArrEquals(bytesToWrite))).andReturn(10);		
		PowerMock.replayAll();
		ret = compressedTransportor.write(responseBytes);
		PowerMock.verifyAll();
		assertEquals(13,ret);
	}
	
	private byte[] getBytesToWrite(byte[] responseBytes)throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
	
		GZIPOutputStream gos = new GZIPOutputStream(baos);
    	gos.write(responseBytes, 0, responseBytes.length);
    	gos.finish();
    	gos.close();
    	byte[] bytesToWrite = baos.toByteArray();
    	return bytesToWrite;
	}
	
	
	/**
	 * Just for improve coverage rate
	 * @throws TransportorException 
	 */
	public void testSimple() throws TransportorException{
		
		EasyMock.expect(transportor.read()).andReturn(new byte[]{1,2});
		EasyMock.expect(transportor.getInputStream()).andReturn(null);
		EasyMock.expect(transportor.getRemoteAddress()).andReturn("");
		transportor.flush();
		transportor.close();
		PowerMock.replayAll();
		
		compressedTransportor.getType();
		compressedTransportor.read();
		compressedTransportor.getInputStream();
		compressedTransportor.getOutputStream();
		compressedTransportor.getRemoteAddress();
		compressedTransportor.flush();
		compressedTransportor.close();
		
		PowerMock.verifyAll();
	}

}
