/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;
import static org.easymock.EasyMock.expect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.powermock.api.easymock.PowerMock;
/**
 * 
 * TestBasicReader.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-26
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(BasicReader.class)
public class TestBasicReader extends TestCase{
	private BasicReader basicReader = new BasicReader();
	
	public void testRead()throws Exception {
		byte[] inByte = new byte[]{2,0,0,0,10,20};
		InputStream is = new ByteArrayInputStream(inByte);
		
		
		byte[] results = basicReader.read(is);
		
		assertEquals("length of results should be 2", 2, results.length);
		assertEquals(10, results[0]);
		assertEquals(20, results[1]);
	}
	
	public void testRead_reutnNull() throws IOException {
		//define variables
		byte[] inByte = new byte[]{2};
		InputStream is = new ByteArrayInputStream(inByte);
		
		byte[] results = basicReader.read(is);

		//assert
		assertNull(results);
	}

}
