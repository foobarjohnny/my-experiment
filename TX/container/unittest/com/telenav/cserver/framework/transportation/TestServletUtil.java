/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * TestServletUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-5
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletUtil.class,ByteArrayOutputStream.class})
public class TestServletUtil extends TestCase{
	private HttpServletResponse response = PowerMock.createMock(HttpServletResponse.class);
	private ServletOutputStream servletOutputStream;
	private ByteArrayOutputStream byteArrayOutputStream;
	private ServletUtil servletUtil = new ServletUtil();
	
	public void testConvertNumberToBytes(){
		int num = 3;
		byte[] result;
		result = ServletUtil.convertNumberToBytes(num);
		assertEquals(3,result[0]);
		assertEquals(0,result[1]);
		assertEquals(0,result[2]);
		assertEquals(0,result[3]);
		
	}
	public void testConvertBytesToNumber(){
		byte[] bytes = new byte[]{3,0,0,0};
		int result;
		result = ServletUtil.convertBytesToNumber(bytes);
		assertEquals(3,result);
	}
	
	
	public void testSendResponse() throws Exception{
		servletOutputStream = PowerMock.createMock(ServletOutputStream.class);
		byteArrayOutputStream = PowerMock.createMockAndExpectNew(ByteArrayOutputStream.class);
		byte[] respBuff = new byte[]{3,0,0,0};
		response.setContentLength(respBuff.length);
		response.setHeader("Cache-Control", "no-cache,no-transform");
		EasyMock.expect(response.getOutputStream()).andReturn(servletOutputStream);
		byteArrayOutputStream.write(EasyMock.aryEq(respBuff));
		byteArrayOutputStream.writeTo(servletOutputStream);
		
		PowerMock.replayAll();
		ServletUtil.sendResponse(response, respBuff);
		
		PowerMock.verifyAll();
		
	}

}
