/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.management.heartbeat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.transportation.ServletUtil;

import junit.framework.TestCase;

/**
 * TestHeartBeatServlet.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServletUtil.class,HeartBeatServlet.class,HeartBeatConfiguration.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.management.heartbeat.HeartBeatConfiguration"})
public class TestHeartBeatServlet extends TestCase{
	private HttpServletRequest request = PowerMock.createMock(HttpServletRequest.class);
	private HttpServletResponse response = PowerMock.createMock(HttpServletResponse.class);
	
	private HeartBeatConfiguration heartBeatConfiguration = PowerMock.createMock(HeartBeatConfiguration.class);
	@Override
	protected void setUp() throws Exception {
		Object nullObj = null;
		Whitebox.setInternalState(heartBeatConfiguration, "serverName", nullObj);
	}
	public void testDoGet() throws Exception{
		HeartBeatServlet hbs = PowerMock.createPartialMock(HeartBeatServlet.class, "doPost");
		hbs.doPost(request, response);
		PowerMock.replayAll();
		
		
		hbs.doGet(request, response);
		PowerMock.verifyAll();
	}
	
	public void testDoPost() throws Exception{
		//define variable
		byte[] respBuff = new byte[]{1,2};
		HeartBeatServlet hbs = PowerMock.createPartialMock(HeartBeatServlet.class, "generateHeartbeatResult");
		
		PowerMock.expectPrivate(hbs, "generateHeartbeatResult",true).andReturn(respBuff);
		PowerMock.mockStatic(ServletUtil.class);
		ServletUtil.sendResponse(response, respBuff);
		PowerMock.replayAll();
		
		hbs.doPost(request, response);
		PowerMock.verifyAll();
	}
	/**
	 * hbc.serverName = null, status = true
	 * @throws Exception 
	 */
	public void testGenerateHeartbeatResult() throws Exception{
		//prepare and replay
		HeartBeatServlet hbs = new HeartBeatServlet();
		
		Whitebox.setInternalState(hbs, "hbc", heartBeatConfiguration);
		EasyMock.expect(heartBeatConfiguration.getHeadString()).andReturn("123");
		EasyMock.expect(heartBeatConfiguration.getServerName()).andReturn(null).anyTimes();
		PowerMock.replayAll();
		
		//invoke and verify
		byte[] result = (byte[])Whitebox.invokeMethod(hbs, "generateHeartbeatResult", true);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(3,result.length);
		assertEquals(49,result[0]);
		assertEquals(50,result[1]);
		assertEquals(51,result[2]);
	}
	/**
	 * hbc.serverName != null, status = false
	 * @throws Exception 
	 */
	public void testGenerateHeartbeatResult1() throws Exception{
		//prepare and replay
		HeartBeatServlet hbs = new HeartBeatServlet();
		
		Whitebox.setInternalState(hbs, "hbc", heartBeatConfiguration);
		EasyMock.expect(heartBeatConfiguration.getHeadString()).andReturn("123");
		EasyMock.expect(heartBeatConfiguration.getServerName()).andReturn("serverName").anyTimes();
		PowerMock.replayAll();
		
		//invoke and verify
		byte[] result = (byte[])Whitebox.invokeMethod(hbs, "generateHeartbeatResult", false);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(3,result.length);
		assertEquals(49,result[0]);
		assertEquals(50,result[1]);
		assertEquals(51,result[2]);
	}
}
