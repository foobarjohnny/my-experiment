/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.json;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.json.me.JSONObject;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.matcher.MatchBox;

/**
 * TestJsonResponseFormatter.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-20
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutorDataFactory.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory"})
public class TestJsonResponseFormatter extends TestCase{
	//define mock object
	private ExecutorDataFactory executorDataFactory = PowerMock.createMock(ExecutorDataFactory.class);
	private ProtocolResponseFormatter formatter = PowerMock.createMock(ProtocolResponseFormatter.class);
	
	private JsonResponseFormatter jsonResponseFormatter = new JsonResponseFormatter();
	private String executorType = "executorType";
	
	public void testFormat_fail() throws ExecutorException{
		jsonResponseFormatter.format(new ByteArrayWrapper(), null);
	}
	
	public void  testFormat() throws Exception{
		//define varables
		ByteArrayWrapper formatTarget = new ByteArrayWrapper();
		JSONObject jsonObj = new JSONObject();
		
		ExecutorResponse[] responses = new ExecutorResponse[1];
		ExecutorResponse response = new ExecutorResponse();
		response.setExecutorType(executorType);
		responses[0] = response;
		
		//prepare and replay
		PowerMock.mockStatic(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		EasyMock.expect(executorDataFactory.createProtocolResponseFormatter(executorType)).andReturn(formatter);
		formatter.format(MatchBox.JSONObjectEquals(jsonObj),EasyMock.aryEq(responses));
		PowerMock.replayAll();
		
		//invoke and verify
		jsonResponseFormatter.format(formatTarget, responses);
		PowerMock.verifyAll();
		//assert
	}
	
	public void  testFormat_condition_coverage() throws Exception{
		//define varables
		ByteArrayWrapper formatTarget = new ByteArrayWrapper();
		JSONObject jsonObj = new JSONObject();
		
		ExecutorResponse[] responses = new ExecutorResponse[1];
		ExecutorResponse response = new ExecutorResponse();
		response.setExecutorType(executorType);
		response.setMaxPayloadSize(1);
		responses[0] = response;
		
		//prepare and replay
		PowerMock.mockStatic(ExecutorDataFactory.class);
		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		EasyMock.expect(executorDataFactory.createProtocolResponseFormatter(executorType)).andReturn(formatter);
		formatter.format(MatchBox.JSONObjectEquals(jsonObj),EasyMock.aryEq(responses));
		PowerMock.replayAll();
		
		//invoke and verify
		jsonResponseFormatter.format(formatTarget, responses);
		PowerMock.verifyAll();
		//assert
		System.out.println();
	}

}
