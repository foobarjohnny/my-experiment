/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

/**
 * TestCServerFilter.java 
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
public class TestCServerFilter extends TestCase{
	private CServerFilter cServerFilter = new CServerFilter();
	private FilterConfig FilterConfig = PowerMock.createMock(FilterConfig.class);
	private ServletRequest request = PowerMock.createMock(ServletRequest.class);
	private ServletResponse response = PowerMock.createMock(ServletResponse.class);
	private FilterChain chain = PowerMock.createMock(FilterChain.class);
	private RequestDispatcher rd = PowerMock.createMock(RequestDispatcher.class);
	
	public void testDoFilter() throws Exception{
		//[1] prepare and replay
		EasyMock.expect(request.getRequestDispatcher("telenav-server")).andReturn(rd);
		rd.forward(request, response);
		PowerMock.replayAll();
		
		//[2] invoke method and verify
		cServerFilter.init(FilterConfig);
		cServerFilter.doFilter(request, response, chain);
		cServerFilter.destroy();
		
		PowerMock.verifyAll();
		//[3] assert
	}

}
