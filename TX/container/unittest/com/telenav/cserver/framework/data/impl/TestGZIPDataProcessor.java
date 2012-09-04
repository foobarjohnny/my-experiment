/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.data.impl;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.transportation.ServletUtil;
import com.telenav.cserver.framework.transportation.TransportationFlag;

import junit.framework.TestCase;

/**
 * TestGZIPDataProcessor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletUtil.class,GZIPDataProcessor.class})
public class TestGZIPDataProcessor extends TestCase{
	
	private GZIPDataProcessor gzip = new GZIPDataProcessor();
	private int flag = TransportationFlag.GZIP_FLAG;
	/*
	public void testProcess() throws Exception{
		//define variable
		byte[] flagBytes = new byte[]{65,66,67};
		byte[] originalData = new byte[]{49,50,51};
		byte[] lenBytes = new byte[]{97,98,99};
		ByteArrayOutputStream baos = PowerMock.createMock(ByteArrayOutputStream.class);
		ByteArrayOutputStream resultBytes = PowerMock.createMock(ByteArrayOutputStream.class);
		GZIPOutputStream gos = PowerMock.createMock(GZIPOutputStream.class);
		
		//prepare and replay
		PowerMock.mockStatic(ServletUtil.class);
		EasyMock.expect(ServletUtil.convertNumberToBytes(flag)).andReturn(flagBytes);
		PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(baos);
		PowerMock.expectNew(GZIPOutputStream.class, baos).andReturn(gos);
		EasyMock.expect(baos.toByteArray()).andReturn(originalData);
		
		gos.write(originalData, 0, originalData.length);
    	gos.finish();
    	gos.close();
    	
    	EasyMock.expect(ServletUtil.convertNumberToBytes(3)).andReturn(lenBytes);
    	PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(resultBytes);
    	
    	resultBytes.write(flagBytes);
    	resultBytes.write(lenBytes);
    	resultBytes.write(originalData);
    	EasyMock.expect(resultBytes.toByteArray()).andReturn(originalData);
    	
    	PowerMock.replayAll();
    	
    	//invoke and verify
    	byte[] result = gzip.process(originalData);
    	PowerMock.verifyAll();
    	
    	//assert
    	assertEquals(originalData.length,result.length);
	}
	*/
	public void testProcess() throws Exception{
		byte[] originalData = new byte[]{49,50,51};
    	byte[] result = gzip.process(originalData);
    	//assert
    	assertEquals(31,result.length);
	}
	public void testSimple(){
		assertNull(gzip.process(null));
		gzip.getType();
	}
}
