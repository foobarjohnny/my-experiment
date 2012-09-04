/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.unittestutil.UnittestUtil;

import junit.framework.TestCase;

/**
 * TestStepWriteTransportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-5
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletTransportor.class,StepWriteTransportor.class})
public class TestStepWriteTransportor extends TestCase{
	private StepWriteTransportor stepWriteTransportor;// = new StepWriteTransportor();
	private ServletTransportor transportor;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	@Override
	protected void setUp() throws Exception {
		servletRequest = PowerMock.createMock(HttpServletRequest.class);
		servletResponse = PowerMock.createMock(HttpServletResponse.class);
		
		PowerMock.suppress(PowerMock.constructor(ServletTransportor.class));
		
		transportor = Whitebox.newInstance(ServletTransportor.class);
		transportor.setRequest(servletRequest);
		transportor.setResponse(servletResponse);
		
		PowerMock.suppress(PowerMock.constructor(StepWriteTransportor.class));
		stepWriteTransportor = Whitebox.newInstance(StepWriteTransportor.class);
		stepWriteTransportor.setTransportor(transportor);
	}
	public void testWriteChunk_ResultLengthLessThanCHUNKSIZE()throws Exception{
		byte[] responseBytes = new byte[]{1,2,3,4,5};
		ServletOutputStream outputStream = PowerMock.createMock(ServletOutputStream.class);
		expect(servletResponse.getOutputStream()).andReturn(outputStream);
		byte[] convert = Whitebox.invokeMethod(StepWriteTransportor.class, "constructStreamingHead", responseBytes);
		outputStream.write(EasyMock.aryEq(convert));
		PowerMock.replayAll();
		stepWriteTransportor.writeChunk(responseBytes);
		PowerMock.verifyAll();
	}
	public void testWriteChunk_ResultLengthMoreThanCHUNKSIZE()throws Exception{
		byte[] responseBytes = new byte[3001];
		ServletOutputStream outputStream = PowerMock.createMock(ServletOutputStream.class);
		expect(servletResponse.getOutputStream()).andReturn(outputStream).times(2);
		byte[] convert = Whitebox.invokeMethod(StepWriteTransportor.class, "constructStreamingHead", responseBytes);
		outputStream.write(MatchBox.byteArrEquals(convert));
		outputStream.write(MatchBox.byteArrEquals(convert));
		PowerMock.replayAll();
		stepWriteTransportor.writeChunk(responseBytes);
		PowerMock.verifyAll();
	}
	public void testConstructStreamingHead() throws Exception{
		byte[] content = new byte[]{0,1,2,3,4,5,6,7,};
		byte[] result = Whitebox.invokeMethod(StepWriteTransportor.class,"constructStreamingHead",content);
		assertEquals(content.length + 8,result.length);
		assertEquals(7,result[result.length - 1]);
		assertEquals(6,result[result.length - 2]);
		assertEquals(5,result[result.length - 3]);
	}
	
	public void testFinish() throws Exception{
		OutputStream os = PowerMock.createMock(ServletOutputStream.class);
		EasyMock.expect(transportor.getOutputStream()).andReturn(os);
		os.write(EasyMock.aryEq(new byte[4]));
		PowerMock.replayAll();
		stepWriteTransportor.finish();
		PowerMock.verifyAll();
	}
	
	public void testWrite_needHead() throws Exception {
		//define variables
		ServletTransportor transportor = PowerMock.createMock(ServletTransportor.class);
		StepWriteTransportor swt = new StepWriteTransportor(transportor);
		swt.setTransportor(transportor);
		
		byte[] responseBytes = new byte[]{1,2,3};
		//prepare and replay
//		PowerMock.expectPrivate(StepWriteTransportor.class,"constructStreamingHead",responseBytes).andReturn(responseBytes);
		EasyMock.expect(transportor.write(MatchBox.byteArrEquals(responseBytes))).andReturn(0);
		PowerMock.replayAll();

		//invoke and verify
		swt.write(responseBytes, true);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 * Just for improve coverage rate
	 * @throws TransportorException
	 * @throws IOException
	 */
	public void testSimple() throws TransportorException, IOException{
		ServletTransportor transportor = PowerMock.createMock(ServletTransportor.class);
		StepWriteTransportor swt = new StepWriteTransportor(transportor);
		swt.setTransportor(transportor);
		
		transportor.close();
		transportor.flush();
		EasyMock.expect(transportor.getResponse()).andReturn(servletResponse);
		servletResponse.flushBuffer();
		EasyMock.expect(transportor.getInputStream()).andReturn(null);
		EasyMock.expect(transportor.getOutputStream()).andReturn(null);
		EasyMock.expect(transportor.getRemoteAddress()).andReturn("");
		EasyMock.expect(transportor.read()).andReturn(new byte[]{1});
		PowerMock.replayAll();
		
		swt.close();
		swt.flush();
		swt.flushBuffer();
		swt.getInputStream();
		swt.getOutputStream();
		swt.getRemoteAddress();
		swt.getType();
		swt.read();
		PowerMock.verifyAll();
	}

}
