package com.telenav.cserver;


import javax.servlet.http.HttpServletRequest;
import org.powermock.api.easymock.PowerMock;
import junit.framework.TestCase;

public abstract class TestRequestParser extends TestCase {
	protected HttpServletRequest httpRequest = PowerMock
			.createMock(HttpServletRequest.class);

	public abstract void testParseBrowserRequest();
}
