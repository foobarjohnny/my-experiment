/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.json;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestJsonRequestParser.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-20
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutorDataFactory.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory"})
public class TestJsonRequestParser extends TestCase{
	//define mock object
	private ExecutorDataFactory executorDataFactory = PowerMock.createMock(ExecutorDataFactory.class);
	private ProtocolRequestParser parser = PowerMock.createMock(ProtocolRequestParser.class);
	//define to be tested object
	private JsonRequestParser jsonRequestParser = new JsonRequestParser();
	
	public void testParse_exception() throws ExecutorException{
		jsonRequestParser.parse(new byte[]{});
	}
	public void testParse() throws ExecutorException{
		//define varables
		ExecutorRequest[] executorRequests = new ExecutorRequest[3];
		executorRequests[0] = new ExecutorRequest();
		executorRequests[1] = new ExecutorRequest();
		executorRequests[2] = new ExecutorRequest();
		//prepare and replay
		PowerMock.mockStatic(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		EasyMock.expect(executorDataFactory.createProtocolRequestParser(UTConstant.JSONARRAY_EXECUTORTYPE_KEY)).andReturn(parser);
		EasyMock.expect(parser.parse(UTConstant.EXECUTORTYPE)).andReturn(executorRequests);
		PowerMock.replayAll();
		
		//invoke and verify
		ExecutorRequest[] result = jsonRequestParser.parse(UnittestUtil.getJSONRequestJsonArray().getBytes());
		PowerMock.verifyAll();
		
		//assert
		assertEquals(3,result.length);
		assertEquals(UTConstant.JSONARRAY_EXECUTORTYPE_KEY,result[0].getExecutorType());
		assertEquals(5,result[0].getGpsData().get(0).heading);
		assertEquals(UTConstant.USERPROFILE_AUDIOFORMAT,result[0].getUserProfile().getAudioFormat());
	}
}
