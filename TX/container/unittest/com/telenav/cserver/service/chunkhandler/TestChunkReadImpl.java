package com.telenav.cserver.service.chunkhandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.ServerException;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.data.impl.NoneDataProcessor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.framework.executor.protocol.txnode.TxNodeProtocolHandler;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.unittestutil.MockHttpServletRequest;
import com.telenav.cserver.unittestutil.MockHttpServletResponse;
import com.telenav.cserver.unittestutil.UnittestUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ExecutorDataFactory.class, ChunkProcessorFactory.class })
public class TestChunkReadImpl {

	private ChunkReadImpl listener = null;
	private HttpServletRequest req = null;
	private HttpServletResponse resp = null;
	private ExecutorRequest request = null;
	private ExecutorResponse response = null;
	private DataProcessor dataProcessor = null;
	private ExecutorContext context = null;
	private CliTransaction cli = null;

	@Before
	public void setUp() {
		byte[] mockRequest = { 1, 2, 3, 4, 5 };
		req = new MockHttpServletRequest(mockRequest);
		resp = new MockHttpServletResponse();
		request = new ExecutorRequest();
		request.setUserProfile(UnittestUtil.createUserProfile());
		response = new ExecutorResponse();
		dataProcessor = new NoneDataProcessor();
		context = new ExecutorContext();
		cli = new CliTransaction("URL");

		listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);
	}

	@Test
	public void testCliLoggingException() {
		listener.cliLoggingException(new ExecutorRequest(), "UnitTest", new Throwable("It's Unit. Test Please Ingore"));
		// no Exception is OK
	}

	@Test
	public void testCliLoggingClientInfo() {
		ExecutorRequest request = new ExecutorRequest();
		request.setUserProfile(UnittestUtil.createUserProfile());
		String executorType = "UnitTest";
		CliTransaction cli = new CliTransaction("URL");
		listener.cliLoggingClientInfo(request, executorType, cli);
		// no Exception is OK
	}

	@Test
	public void testCliLoggingClientInfoWithNull() {
		// Case 1 CliTransaction is null
		ExecutorRequest request = new ExecutorRequest();
		String executorType = "UnitTest";
		listener.cliLoggingClientInfo(request, executorType, null);

		// Case 2 request is null
		listener.cliLoggingClientInfo(null, executorType, new CliTransaction("URL"));

		// Case 3 UserProfile is null
		request.setUserProfile(UnittestUtil.createUserProfile());
		listener.cliLoggingClientInfo(request, executorType, new CliTransaction("URL"));

		// no Exception is OK
	}

	@Test
	public void testReadChunk() {
		byte[] mockRequest = { 1, 2, 3, 4, 5 };

		ProtocolRequestParser parser = PowerMock.createMock(ProtocolRequestParser.class);
		TxNodeProtocolHandler protocolHandler = new TxNodeProtocolHandler();
		protocolHandler.setRequestParser(parser);
		context.setProtocolHandler(protocolHandler);

		ExecutorRequest executorRequest = new ExecutorRequest();
		executorRequest.setExecutorType("UnitTest");
		ExecutorRequest[] requests = { executorRequest };

		ExecutorResponse executorResponse = new ExecutorResponse();

		PowerMock.mockStatic(ExecutorDataFactory.class);
		ExecutorDataFactory dataFactory = PowerMock.createMock(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(dataFactory).anyTimes();
		EasyMock.expect(dataFactory.createExecutorResponse(EasyMock.anyObject(String.class))).andReturn(executorResponse).anyTimes();

		PowerMock.mockStatic(ChunkProcessorFactory.class);
		ChunkProcessorFactory processorFactory = PowerMock.createMock(ChunkProcessorFactory.class);
		EasyMock.expect(ChunkProcessorFactory.getInstance()).andReturn(processorFactory).anyTimes();

		try {
			EasyMock.expect(processorFactory.createProcessor(EasyMock.anyObject(String.class))).andReturn(new AbstractChunkProcssorTestingImp()).anyTimes();
			EasyMock.expect(parser.parse(EasyMock.anyObject())).andReturn(requests).anyTimes();
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail(e1.getMessage());
		}

		PowerMock.replayAll();

		// Test Case 1
		ChunkReadListener listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);

		try {
			listener.readChunk(mockRequest);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		PowerMock.verifyAll();
	}

	@Test
	public void testReadChunk2() {
		byte[] mockRequest = { 1, 2, 3, 4, 5 };

		ProtocolRequestParser parser = PowerMock.createMock(ProtocolRequestParser.class);
		TxNodeProtocolHandler protocolHandler = new TxNodeProtocolHandler();
		protocolHandler.setRequestParser(parser);
		context.setProtocolHandler(protocolHandler);

		ExecutorRequest nullExecutorRequest = new ExecutorRequest();
		UserProfile userProfile = UnittestUtil.createUserProfile();
		nullExecutorRequest.setExecutorType("UnitTest");
		nullExecutorRequest.setUserProfile(userProfile);
		userProfile.setDataProcessType("gzip");
		nullExecutorRequest.setExecutorType("UnitTest");
		ExecutorRequest[] nullRequests = { nullExecutorRequest };

		ExecutorResponse executorResponse = new ExecutorResponse();

		PowerMock.mockStatic(ExecutorDataFactory.class);
		ExecutorDataFactory dataFactory = PowerMock.createMock(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(dataFactory).anyTimes();
		EasyMock.expect(dataFactory.createExecutorResponse(EasyMock.anyObject(String.class))).andReturn(executorResponse).anyTimes();

		PowerMock.mockStatic(ChunkProcessorFactory.class);
		ChunkProcessorFactory processorFactory = PowerMock.createMock(ChunkProcessorFactory.class);
		EasyMock.expect(ChunkProcessorFactory.getInstance()).andReturn(processorFactory).anyTimes();

		try {
			EasyMock.expect(processorFactory.createProcessor(EasyMock.anyObject(String.class))).andReturn(new AbstractChunkProcssorTestingImp()).anyTimes();
			EasyMock.expect(parser.parse(EasyMock.anyObject())).andReturn(nullRequests);
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail(e1.getMessage());
		}

		PowerMock.replayAll();

		ChunkReadListener listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);

		try {
			listener.readChunk(mockRequest);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		PowerMock.verifyAll();
	}

	@Test(expected = NullPointerException.class)
	public void testReadChunk3() {
		byte[] mockRequest = { 1, 2, 3, 4, 5 };

		ProtocolRequestParser parser = PowerMock.createMock(ProtocolRequestParser.class);
		TxNodeProtocolHandler protocolHandler = new TxNodeProtocolHandler();
		protocolHandler.setRequestParser(parser);
		context.setProtocolHandler(protocolHandler);

		ExecutorResponse executorResponse = new ExecutorResponse();

		PowerMock.mockStatic(ExecutorDataFactory.class);
		ExecutorDataFactory dataFactory = PowerMock.createMock(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(dataFactory).anyTimes();
		EasyMock.expect(dataFactory.createExecutorResponse(EasyMock.anyObject(String.class))).andReturn(executorResponse).anyTimes();

		PowerMock.mockStatic(ChunkProcessorFactory.class);
		ChunkProcessorFactory processorFactory = PowerMock.createMock(ChunkProcessorFactory.class);
		EasyMock.expect(ChunkProcessorFactory.getInstance()).andReturn(processorFactory).anyTimes();

		try {
			EasyMock.expect(processorFactory.createProcessor(EasyMock.anyObject(String.class))).andReturn(new AbstractChunkProcssorTestingImp()).anyTimes();
			EasyMock.expect(parser.parse(EasyMock.anyObject())).andReturn(null);
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail(e1.getMessage());
		}

		PowerMock.replayAll();

		ChunkReadListener listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);
		try {
			listener.readChunk(mockRequest);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		PowerMock.verifyAll();
	}

	@Test
	public void testHandleResponse() throws ServerException {
		response.setStatus(ExecutorResponse.STATUS_WRITE_FINISHED);

		ChunkReadListener listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);
		listener.handleResponse(request, response);
		// no Exception is OK
	}

	@Test
	public void testHandleResponse2() throws ServerException {
		response.setStatus(ExecutorResponse.STATUS_OK);

		TxNodeProtocolHandler protocolHandler = new TxNodeProtocolHandler();
		protocolHandler.setResponseFormatter(new ProtocolResponseFormatter() {

			@Override
			public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException {
				ByteArrayWrapper wrapper = (ByteArrayWrapper) formatTarget;
				byte[] bytes = { 1, 2, 3, 4, 5 };
				wrapper.bytes = bytes;
			}
		});
		context.setProtocolHandler(protocolHandler);
		byte[] bytes = { 1, 2, 3, 4, 5 };
		context.setTransportor(new ServletTransportor(new MockHttpServletRequest(bytes), new MockHttpServletResponse()));

		ChunkReadListener listener = new ChunkReadImpl(req, resp, request, response, dataProcessor, context, cli);
		listener.handleResponse(request, response);
		// no Exception is OK
	}

	@Test
	public void testHandleResponseWithNull() throws ServerException {
		listener.handleResponse(null, null);
		listener.handleResponse(null, new ExecutorResponse());
		listener.handleResponse(new ExecutorRequest(), new ExecutorResponse());

		// for code coverage
		listener.getFirstExecutorType();
		listener.getSbExecutorType();
		listener.finish();
		listener.awake();
		listener.await();
	}

	@Test
	public void testReadChunkWithNull() {
		byte[] nullRequest = null;
		Boolean result = null;
		try {
			result = listener.readChunk(nullRequest);
		} catch (ServerException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result);
		Assert.assertTrue(listener.finished);

		result = null;
		byte[] emptyRequest = {};
		try {
			result = listener.readChunk(emptyRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result);
		Assert.assertTrue(listener.finished);
	}
}
