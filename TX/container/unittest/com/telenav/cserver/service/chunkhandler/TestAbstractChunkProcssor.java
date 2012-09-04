package com.telenav.cserver.service.chunkhandler;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptorHalt;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptorHalt;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.InterceptorManager;
import com.telenav.cserver.framework.executor.impl.DefaultInterceptorManager;
import com.telenav.cserver.unittestutil.UnittestUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExecutorDispatcher.class)
@SuppressStaticInitializationFor("com.telenav.cserver.framework.executor.ExecutorDispatcher")
public class TestAbstractChunkProcssor {

	private AbstractChunkProcssor procssor = null;

	@Before
	public void setUp() {
		procssor = new AbstractChunkProcssorTestingImp2();
	}

	@Test
	public void testProcessorName() {
		procssor.setProcessorName("UnitTest");
		Assert.assertEquals("UnitTest", procssor.getProcessorName());
	}

	@Test
	public void testCallback() {
		procssor.setCallback(new ChunkCallbackImpl());
		Assert.assertTrue(procssor.getCallback() instanceof ChunkCallback);
	}

	@Test
	public void testDoProcess() throws Exception {
		procssor.doProcess(new ExecutorRequest(), new ExecutorResponse(), new ExecutorContext());
		// no Exception is OK
	}

	@Test
	public void testProcessWithPreInterceptor() throws Exception {
		AbstractChunkProcssor procssor = new AbstractChunkProcssorTestingImp2();
		ExecutorRequest request = new ExecutorRequest();
		request.setExecutorType("UnitTest");
		request.setUserProfile(UnittestUtil.createUserProfile());
		ExecutorResponse response = new ExecutorResponse();
		ExecutorContext context = new ExecutorContext();
		InterceptorManager interceptorManager = new DefaultInterceptorManager();
		interceptorManager.addPreInterceptor("UnitTest", new SamplePreInterceptor());
		interceptorManager.addPreInterceptor("UnitTest", new SamplePreInterceptor1());
		interceptorManager.addPreInterceptor("UnitTest", new SamplePreInterceptorHalt());

		ExecutorDispatcher dispatcher = PowerMock.createMock(ExecutorDispatcher.class);
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(dispatcher);
		EasyMock.expect(dispatcher.getInterceptorManager()).andReturn(interceptorManager);

		PowerMock.replayAll();

		procssor.process(request, response, context);

		PowerMock.verifyAll();
		// no Exception is OK
	}

	@Test
	public void testProcessWithPostInterceptor() throws Exception {
		AbstractChunkProcssor procssor = new AbstractChunkProcssorTestingImp2();
		ExecutorRequest request = new ExecutorRequest();
		request.setExecutorType("UnitTest");
		request.setUserProfile(UnittestUtil.createUserProfile());
		ExecutorResponse response = new ExecutorResponse();
		ExecutorContext context = new ExecutorContext();
		InterceptorManager interceptorManager = new DefaultInterceptorManager();
		interceptorManager.addPostInterceptor("UnitTest", new SamplePostInterceptor());
		interceptorManager.addPostInterceptor("UnitTest", new SamplePostInterceptor1());
		interceptorManager.addPostInterceptor("UnitTest", new SamplePostInterceptorHalt());

		ExecutorDispatcher dispatcher = PowerMock.createMock(ExecutorDispatcher.class);
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(dispatcher);
		EasyMock.expect(dispatcher.getInterceptorManager()).andReturn(interceptorManager);

		PowerMock.replayAll();

		procssor.process(request, response, context);

		PowerMock.verifyAll();
		// no Exception is OK
	}

	@Test
	public void testProcessWithNull() throws Exception {
		AbstractChunkProcssor procssor = new AbstractChunkProcssorTestingImp2();
		ExecutorRequest request = new ExecutorRequest();
		// request.setUserProfile(UnittestUtil.createUserProfile());
		ExecutorResponse response = new ExecutorResponse();
		ExecutorContext context = new ExecutorContext();
		InterceptorManager interceptorManager = new DefaultInterceptorManager();

		ExecutorDispatcher dispatcher = PowerMock.createMock(ExecutorDispatcher.class);
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(dispatcher);
		EasyMock.expect(dispatcher.getInterceptorManager()).andReturn(interceptorManager);

		PowerMock.replayAll();

		procssor.process(request, response, context);

		PowerMock.verifyAll();
		// no Exception is OK
	}
}
