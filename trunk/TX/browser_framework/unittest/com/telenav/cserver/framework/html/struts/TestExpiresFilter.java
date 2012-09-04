package com.telenav.cserver.framework.html.struts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.html.struts.ExpiresFilter.DurationUnit;
import com.telenav.cserver.framework.html.struts.ExpiresFilter.StartingPoint;
import com.telenav.cserver.framework.html.struts.ExpiresFilter.XHttpServletResponse;
import static org.junit.Assert.*;

public class TestExpiresFilter {
	
	ExpiresFilter expiresfilter;
	IMocksControl control;
	HttpServletRequest request;
	HttpServletResponse response;
	@Before
	public void setUp()
	{
		control = EasyMock.createControl();
		request = control.createMock(HttpServletRequest.class);
		//control = EasyMock.createControl();
		response = control.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getContentType()).andReturn("text/html").anyTimes();
		EasyMock.expect(response.containsHeader("Expires")).andReturn(true).anyTimes();
		//response.reset();
		//EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(response);
		expiresfilter = new ExpiresFilter();
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testDurationMinutes()
	{
		ExpiresFilter.Duration duration = ExpiresFilter.Duration.minutes(1);
		assertNotNull(duration);
		assertEquals(1, duration.getAmount());
		assertEquals(duration.getUnit(), duration.unit);
		assertEquals(DurationUnit.MINUTE, duration.unit);
		assertEquals(1, duration.amount);
	}
	@Test
	public void testDurationSeconds()
	{
		ExpiresFilter.Duration duration = ExpiresFilter.Duration.seconds(1);
		assertNotNull(duration);
		assertEquals(1, duration.getAmount());
		assertEquals(duration.getUnit(), duration.unit);
		assertEquals(DurationUnit.SECOND, duration.unit);
		assertEquals(1, duration.amount);
	}
	@Test
	public void testExpiresConfiguration()
	{
		ExpiresFilter.ExpiresConfiguration expiresConfiguration = new ExpiresFilter.ExpiresConfiguration(StartingPoint.ACCESS_TIME);
		assertNotNull(expiresConfiguration);
		assertEquals(expiresConfiguration.getStartingPoint(), StartingPoint.ACCESS_TIME);
	}
	@Test
	public void testExpiresConfigurationWithDurationList()
	{
		ExpiresFilter.Duration durationMinutes = ExpiresFilter.Duration.minutes(1);
		ExpiresFilter.Duration durationSeconds = ExpiresFilter.Duration.seconds(1);
		List<ExpiresFilter.Duration> list = new ArrayList<ExpiresFilter.Duration>();
		list.add(durationMinutes);
		list.add(durationSeconds);
		ExpiresFilter.ExpiresConfiguration expiresConfiguration = new ExpiresFilter.ExpiresConfiguration(StartingPoint.LAST_MODIFICATION_TIME, durationMinutes, durationSeconds);
		assertNotNull(expiresConfiguration);
		assertEquals(StartingPoint.LAST_MODIFICATION_TIME, expiresConfiguration.getStartingPoint());
		assertEquals(list, expiresConfiguration.getDurations());
	}
//	@Test
//	public void testXHttpServletResponse() throws IOException
//	{
//		XHttpServletResponse xhttpservletresponse;
//		xhttpservletresponse = expiresfilter.new XHttpServletResponse(request, response);
//		assertNotNull(xhttpservletresponse);
//		xhttpservletresponse.addDateHeader("name", 1000);
//		assertEquals(1000, xhttpservletresponse.getLastModifiedHeader());
//		assertTrue(xhttpservletresponse.isLastModifiedHeaderSet());
//		xhttpservletresponse.addDateHeader("Last-Modified", 1000);
//		assertTrue(xhttpservletresponse.isLastModifiedHeaderSet());
//		xhttpservletresponse.addHeader("name", "vale");
//		assertNull(xhttpservletresponse.getCacheControlHeader());
//		xhttpservletresponse.addHeader("Cache-Control", "value");
//		assertEquals("value", xhttpservletresponse.getCacheControlHeader());
//		assertNotNull(xhttpservletresponse.getOutputStream());
//		assertNotNull(xhttpservletresponse.getWriter());
		//xhttpservletresponse.reset();
		//assertEquals(0, xhttpservletresponse.getLastModifiedHeader());
		//assertTrue(!xhttpservletresponse.isLastModifiedHeaderSet());
		//assertNull(xhttpservletresponse.getCacheControlHeader());
//	}
	
//	public void testXPrintWriter()
//	{
//		PrintWriter out;
//		control = EasyMock.createControl();
//		out = control.createMock(PrintWriter.class);
//		XHttpServletResponse xhttpservletresponse;
//		xhttpservletresponse = expiresfilter.new XHttpServletResponse(request, response);
//		XPrintWriter  xPrintWriter = expiresfilter.new XPrintWriter (out, request, xhttpservletresponse);
//	}
	
	
	
	

}
