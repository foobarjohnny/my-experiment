/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.http;


import static org.easymock.EasyMock.expect;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.transportation.TransportorException;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
/**
 * 
 * TestServletTransportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-25
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServletTransportor.class)
public class TestServletTransportor extends TestCase {

	private ServletTransportor servletTransportor;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	

	@Override
	protected void setUp() throws Exception {
		
		servletRequest = PowerMock.createMock(HttpServletRequest.class);
		servletResponse = PowerMock.createMock(HttpServletResponse.class);
		
		PowerMock.suppress(PowerMock.constructor(ServletTransportor.class));
		
		servletTransportor = Whitebox.newInstance(ServletTransportor.class);
		servletTransportor.setRequest(servletRequest);
		servletTransportor.setResponse(servletResponse);
		
	}

	public void testRead() throws Exception {

		ServletInputStream inputStream = PowerMock.createMock(ServletInputStream.class);
		
		expect(servletRequest.getContentLength()).andReturn(3);
		expect(servletRequest.getInputStream()).andReturn(inputStream);
		
		BufferedInputStream bufferedInputStream = PowerMock.createMock(BufferedInputStream.class);
		PowerMock.expectNew(BufferedInputStream.class,inputStream).andReturn(bufferedInputStream);

		expect(bufferedInputStream.read()).andReturn(65);
		expect(bufferedInputStream.read()).andReturn(66);
		expect(bufferedInputStream.read()).andReturn(67);

		bufferedInputStream.close();
		
		PowerMock.replayAll();

		byte[] results = servletTransportor.read();

		PowerMock.verifyAll();

		assertEquals("length of results should be 3", 3, results.length);
		assertEquals(65, results[0]);
		assertEquals(66, results[1]);
		assertEquals(67, results[2]);
	}
	
	public void testWrite() throws Exception {
		String testStr = "abc";
		ServletOutputStream outputStream = PowerMock.createMock(ServletOutputStream.class);
		expect(servletResponse.getOutputStream()).andReturn(outputStream);
		outputStream.write(EasyMock.aryEq(testStr.getBytes()));
		PowerMock.replay(servletResponse,outputStream);
		int len = servletTransportor.write(testStr.getBytes());
		PowerMock.verify(servletResponse,outputStream);
		assertEquals(testStr.getBytes().length, len);
	}
	
	public void testGetRemoteAddress(){
		String ip = "192.168.1.123";
		String result;
		EasyMock.expect(servletRequest.getHeader("x-forwarded-for")).andReturn(ip);
		PowerMock.replayAll();
		
		result = servletTransportor.getRemoteAddress();
		PowerMock.verifyAll();
		
		assertEquals(ip,result);
	}
	//ip.length() == 0
	public void testGetRemoteAddress_addr(){
		String ip = "";
		String remoteAddr = "http://www.google.com";
		String result;
		EasyMock.expect(servletRequest.getHeader("x-forwarded-for")).andReturn(ip);
		EasyMock.expect(servletRequest.getRemoteAddr()).andReturn(remoteAddr);
		PowerMock.replayAll();
		
		result = servletTransportor.getRemoteAddress();
		PowerMock.verifyAll();
		
		assertEquals(remoteAddr,result);
	}
	//ip == unknown
	public void testGetRemoteAddress_addr1(){
		String ip = "unknown";
		String remoteAddr = "http://www.baidu.com";
		String result;
		EasyMock.expect(servletRequest.getHeader("x-forwarded-for")).andReturn(ip);
		EasyMock.expect(servletRequest.getRemoteAddr()).andReturn(remoteAddr);
		PowerMock.replayAll();
		
		result = servletTransportor.getRemoteAddress();
		PowerMock.verifyAll();
		
		assertEquals(remoteAddr,result);
	}
	public void testRead_returnNull() throws TransportorException {
		//define variables
		
		//prepare and replay
		EasyMock.expect(servletRequest.getContentLength()).andReturn(-1);
		PowerMock.replayAll();

		//invoke and verify
		byte[] result = servletTransportor.read();
		PowerMock.verifyAll();

		//assert
		assertNull(result);
		//no Exception is OK.
	}
	/**
	 * Just for improvement coverage rate
	 * @throws TransportorException 
	 * @throws IOException 
	 */
	public void testSimple() throws TransportorException, IOException{
		ServletOutputStream os = PowerMock.createMock(ServletOutputStream.class);
		EasyMock.expect(servletResponse.getOutputStream()).andReturn(os);
		os.flush();
		PowerMock.replayAll();
		
		servletTransportor.getType();
		servletTransportor.getRequest();  
		servletTransportor.getResponse();
		servletTransportor.flush();
		servletTransportor.close();
		PowerMock.verifyAll();

	}
	
}
