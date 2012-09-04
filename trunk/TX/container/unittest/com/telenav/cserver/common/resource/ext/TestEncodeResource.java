/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.InputStream;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;
import org.springframework.core.io.Resource;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestEncodeResource.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestEncodeResource extends TestCase {
	String path = "resource/test.properties";// test.properties
	private EncodeResource er = new EncodeResource(path);

	public void testGetInputStream() throws Exception {
		
		InputStream input = er.getInputStream();
		assertNotNull(input);
		assertEquals('#', (char)input.read());
		input.skip(23);
		assertEquals('s', (char)input.read());
	}
	
	public void testGetInputStream_fail() {
		EncodeResource er_null = new EncodeResource("path.acd");
		try{
			InputStream input = er_null.getInputStream();
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testSimple() throws Exception{
		Whitebox.invokeMethod(er, "getEncoding", "");
		
		Resource obj;
		obj = (Resource)Whitebox.invokeMethod(er, "createRelative", path);
		assertNotNull(obj);
		assertTrue(obj instanceof EncodeResource);
	}


}
