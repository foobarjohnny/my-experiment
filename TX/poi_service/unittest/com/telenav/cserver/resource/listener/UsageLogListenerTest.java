package com.telenav.cserver.resource.listener;

import javax.servlet.ServletContextEvent;
import org.apache.struts.mock.MockServletContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UsageLogListenerTest extends UsageLogListener {

	UsageLogListener instance = new UsageLogListener();
	ServletContextEvent servletContextEvent = new ServletContextEvent(new MockServletContext());
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testContextDestroyed() {
		instance.contextDestroyed(servletContextEvent);
	}

	@Test
	public void testContextInitialized() {
		instance.contextInitialized(servletContextEvent);
	}

}
