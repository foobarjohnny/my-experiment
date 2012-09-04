package com.telenav.cserver.service.socket;

import java.security.Permission;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sun.grizzly.ProtocolFilter;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GrizzlyRequest.class, System.class })
public class TestDefaultSocketServer {

	private DefaultSocketServer server = null;

	@Before
	public void setUp() {
		server = new DefaultSocketServer();
		server.setHttpAdapter(new ServletAdapter());
		server.setPort(80);
		server.setTimeout(30000);
		server.setReadThreadsCount(30);
		server.setCorePoolSize(10);
		server.setMaximumPoolSize(30);
		server.setBusinessFilter(new DefaultFilter());
	}

	@After
	public void tearDown() {
		PowerMock.verifyAll();
	}

	@Test
	public void testBusinessFilter() {
		server.setBusinessFilter(new DefaultFilter());
		Assert.assertTrue(server.getBusinessFilter() instanceof ProtocolFilter);
	}

	@Test
	public void testShutdownWithWrongURL() {
		GrizzlyRequest request = PowerMock.createMock(GrizzlyRequest.class);
		EasyMock.expect(request.getRequestURI()).andReturn("/WrongURL");

		PowerMock.replayAll();

		server.shutdown(request);

		PowerMock.verifyAll();
	}

	@Test
	public void testShutdownWithWrongPassword() {
		GrizzlyRequest request = PowerMock.createMock(GrizzlyRequest.class);
		EasyMock.expect(request.getRequestURI()).andReturn("/admin");
		EasyMock.expect(request.getParameter("password")).andReturn("WrongPpassword");

		PowerMock.replayAll();

		server.shutdown(request);

		PowerMock.verifyAll();
	}

	@Test
	public void testStartup() throws InterruptedException {
		GrizzlyRequest request = PowerMock.createMock(GrizzlyRequest.class);
		EasyMock.expect(request.getRequestURI()).andReturn("/admin");
		EasyMock.expect(request.getParameter("password")).andReturn("shutdown-socket-server-right-password");

		PowerMock.replayAll();

		Runnable runServer = new Runnable() {

			@Override
			public void run() {
				server.startup();
			}
		};
		new Thread(runServer).start();

		Thread.sleep(5000);

		// use SecurityManager to block System.exit(0) call in
		// DefaultSocketServer.java , the permission name is exitVM.0
		SecurityManager securityManager = new SecurityManager() {
			public void checkPermission(Permission permission) {
				if ("exitVM.0".equals(permission.getName())) {
					throw new SecurityException("System.exit attempted and blocked.");
				}
			}
		};
		System.setSecurityManager(securityManager);

		try {
			server.shutdown(request);
		} catch (SecurityException e) {
			System.out.println("System.exit(0) called");
		}

		PowerMock.verifyAll();

		System.setSecurityManager(null);
	}

}
