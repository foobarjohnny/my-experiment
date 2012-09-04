/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestGZipReader.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GZipReader.class,ReaderFactory.class})
public class TestGZipReader extends TestCase {

	private GZipReader gZipReader = new GZipReader();
	private GZIPInputStream zis = PowerMock.createMock(GZIPInputStream.class); 
	private ByteArrayInputStream byteArrayInputStream = PowerMock.createMock(ByteArrayInputStream.class);//new ByteArrayInputStream(new byte[]{10,20});

	public void testRead() throws Exception {
		//define variables
		byte[] inByte = new byte[] {2,0, 0, 0,10,20};
		byte[] args4newByteArrayInputStream = new byte[]{10,20};
		InputStream is = new ByteArrayInputStream(inByte);
		
		//prepare and replay
		PowerMock.expectNew(ByteArrayInputStream.class, EasyMock.aryEq(args4newByteArrayInputStream)).andReturn(byteArrayInputStream);
		PowerMock.expectNew(GZIPInputStream.class, byteArrayInputStream).andReturn(zis);
		EasyMock.expect(zis.read()).andReturn(97);
		EasyMock.expect(zis.read()).andReturn(98);
		EasyMock.expect(zis.read()).andReturn(99);
		EasyMock.expect(zis.read()).andReturn(-1);
		
		PowerMock.replayAll();
		
		//invoke and verify
		byte[] results = gZipReader.read(is);
		
		PowerMock.verifyAll();
		
		//assert
		assertEquals("length of results should be 3", 3, results.length);
		assertEquals('a', results[0]);
		assertEquals('b', results[1]);
		assertEquals('c', results[2]);
	}
	
	public void testRead_returnNull() throws IOException {
		byte[] inByte = new byte[] {2};
		InputStream is = new ByteArrayInputStream(inByte);
		byte[] results = gZipReader.read(is);
		
		assertNull(results);
	}
	
	
	public void testRead_readerNotNull() throws Exception {
		//define variables
		byte[] inByte = new byte[] {2,0, 0, 0,10,20};
		byte[] args4newByteArrayInputStream = new byte[]{10,20};
		InputStream is = new ByteArrayInputStream(inByte);
		Reader reader = PowerMock.createMock(Reader.class);
		//prepare and replay
		PowerMock.mockStatic(ReaderFactory.class);
		EasyMock.expect(ReaderFactory.getReader(2)).andReturn(reader);
		EasyMock.expect(reader.read(is)).andReturn(args4newByteArrayInputStream);
		
		
		PowerMock.expectNew(ByteArrayInputStream.class, EasyMock.aryEq(args4newByteArrayInputStream)).andReturn(byteArrayInputStream);
		PowerMock.expectNew(GZIPInputStream.class, byteArrayInputStream).andReturn(zis);
		EasyMock.expect(zis.read()).andReturn(97);
		EasyMock.expect(zis.read()).andReturn(98);
		EasyMock.expect(zis.read()).andReturn(99);
		EasyMock.expect(zis.read()).andReturn(-1);
		
		PowerMock.replayAll();
		
		//invoke and verify
		byte[] results = gZipReader.read(is);
		
		PowerMock.verifyAll();
		
		//assert
		assertEquals("length of results should be 3", 3, results.length);
		assertEquals('a', results[0]);
		assertEquals('b', results[1]);
		assertEquals('c', results[2]);
	}

}
