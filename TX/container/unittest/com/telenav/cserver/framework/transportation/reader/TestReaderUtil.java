/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.unittestutil.UnittestUtil;
/**
 * 
 * TestReaderUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ReaderFactory.class,ReaderUtil.class})
public class TestReaderUtil extends TestCase{
	private ReaderUtil readerUtil = new ReaderUtil();//for coverage rate
	public void testRead()throws Exception {
		//define variables
		byte[] inByte = new byte[]{2,0,0,0,2,0,0,0,10,20,30,40};
		InputStream is = new ByteArrayInputStream(inByte);
		
		//prepare and replay
		PowerMock.mockStatic(ReaderFactory.class);
		EasyMock.expect(ReaderFactory.getReader(2)).andReturn(new BasicReader());
		
		PowerMock.replayAll();
		
		//invoke and verify
		byte[] results = ReaderUtil.read(is);
		PowerMock.verifyAll();
		
		//assert
		assertEquals("length of results should be 2", 2, results.length);
		assertEquals(10, results[0]);
		assertEquals(20, results[1]);
	}
	
	public void testRead_exception() {
		try {
			ReaderUtil.read(null);
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testRead_returnNull() throws IOException {
		//define variables
		byte[] inByte = new byte[]{2};
		InputStream is = new ByteArrayInputStream(inByte);
		
		assertNull(ReaderUtil.read(is));
	}
	/**
	 * ReaderFactory.getReader(len) == null
	 * @throws IOException
	 */
	public void testRead_readerNull(){
		//define variables
		byte[] inByte = new byte[]{2,0,0,0};
		InputStream is = new ByteArrayInputStream(inByte);
		
		PowerMock.mockStatic(ReaderFactory.class);
		EasyMock.expect(ReaderFactory.getReader(2)).andReturn(null);
		try {
			ReaderUtil.read(is);
		} catch (IOException e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}

}
