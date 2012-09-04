/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestNativeResourecBundleLoader.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({NativeResourecBundleLoader.class})
public class TestNativeResourecBundleLoader extends TestCase {
	private NativeResourecBundleLoader loader = new NativeResourecBundleLoader();
	private URL url = PowerMock.createMock(URL.class);
	private HttpURLConnection httpURLConnection = PowerMock.createMock(HttpURLConnection.class);
	private InputStream in;

	public void testLoadResource() {
		String path = "resource/test";// test.properties
		List list = (List) loader.loadResource(path, null);
		assertNotNull(list);
		assertEquals("db=true", list.get(0));
		assertEquals("sample=true", list.get(1));
		assertEquals("billing=true", list.get(2));
		assertEquals("startup=true", list.get(3));
		assertEquals("iconRetry=true", list.get(4));
	}
	
	
	public void testLoadResource_getByHTTP() throws Exception{
		//init
		String path = "http";
		String text = "a=1\nb=2\nc=3\nd=4";
		in = new ByteArrayInputStream(text.getBytes());
		//expect
		PowerMock.expectNew(URL.class,path+".properties").andReturn(url);
		EasyMock.expect(url.openConnection()).andReturn(httpURLConnection);
		EasyMock.expect(httpURLConnection.getInputStream()).andReturn(in);
		//
		PowerMock.replayAll();
		Object obj = loader.loadResource(path, null);
		//
		PowerMock.verifyAll();
		//assert
		List result = (List)obj;
		assertNotNull(obj);
		assertEquals(4,result.size());
		assertEquals("a=1",result.get(0));
		assertEquals("b=2",result.get(1));
		assertEquals("c=3",result.get(2));
		assertEquals("d=4",result.get(3));
	}

}
