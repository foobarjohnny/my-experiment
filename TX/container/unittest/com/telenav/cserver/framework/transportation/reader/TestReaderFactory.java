/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import com.telenav.cserver.framework.transportation.TransportationFlag;

import junit.framework.TestCase;

/**
 * TestReaderFactory.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-12
 */
public class TestReaderFactory extends TestCase{
	public void testGetReader(){
		Object resultBasicReader,resultGZipReader;
		
		resultBasicReader = ReaderFactory.getReader(TransportationFlag.BASIC_FLAG);
		resultGZipReader =  ReaderFactory.getReader(TransportationFlag.GZIP_FLAG);
		assertNull(ReaderFactory.getReader(-1));

		assertTrue(resultBasicReader instanceof BasicReader);
		assertTrue(resultGZipReader instanceof GZipReader);
	}

}
