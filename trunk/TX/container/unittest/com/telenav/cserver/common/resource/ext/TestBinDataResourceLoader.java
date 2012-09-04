/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestBinDataResourceLoader.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BinDataResourceLoader.class})
public class TestBinDataResourceLoader extends TestCase{
	private BinDataResourceLoader binDataResourceLoader = new BinDataResourceLoader();
	private URL url = PowerMock.createMock(URL.class);
	private HttpURLConnection httpURLConnection = PowerMock.createMock(HttpURLConnection.class);
	private InputStream in = new ByteArrayInputStream(new byte[]{0,10,20,30,40});
	
	public void testLoadResource(){
		String path = "resource/test.bar";//test.bar
		Object obj = binDataResourceLoader.loadResource(path, null);
		assertNotNull(obj);
	}
	
	public void testencodeFileName() throws Exception{
		String url = "",url1=null,url2="http://www.google.com/test.xls";
		String result,result1,result2;
		result = BinDataResourceLoader.encodeFileName(url);
		result1 = BinDataResourceLoader.encodeFileName(url1);
		result2 = BinDataResourceLoader.encodeFileName(url2);
		
		assertEquals("",result);
		assertNull(result1);
		assertEquals(url2,result2);
	}
	
	public void testLoadResource_getByHTTP() throws Exception{
		String path = "http.bar";
		//expect
		PowerMock.expectNew(URL.class,path).andReturn(url);
		EasyMock.expect(url.openConnection()).andReturn(httpURLConnection);
		EasyMock.expect(httpURLConnection.getInputStream()).andReturn(in);
		//
		PowerMock.replayAll();
		Object obj = binDataResourceLoader.loadResource(path, null);
		//
		PowerMock.verifyAll();
		//assert
		byte[] result = (byte[])obj;
		assertNotNull(obj);
		assertEquals(0,result[0]);
		assertEquals(10,result[1]);
		assertEquals(20,result[2]);
		assertEquals(30,result[3]);
		assertEquals(40,result[4]);
	}
	
	
	public void testLoadResource_urlIsNull(){
		String path = "testbar";//test.bar
		Object obj = binDataResourceLoader.loadResource(path, null);
		assertNull(obj);
	}

}
