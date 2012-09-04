/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.class4test.SampleExecutorResponse;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory.Item;

/**
 * TestExecutorDataFactory.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-10
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutorDataFactory.class})
@SuppressStaticInitializationFor("com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory")
public class TestExecutorDataFactory extends TestCase{
	ExecutorDataFactory executorDataFactory = Whitebox.newInstance(ExecutorDataFactory.class);
	
	private ProtocolRequestParser protocolRequestParser = PowerMock.createMock(ProtocolRequestParser.class);
	private ProtocolRequestParser protocolRequestParser_default = PowerMock.createMock(ProtocolRequestParser.class);
	private ProtocolResponseFormatter protocolResponseFormatter = PowerMock.createMock(ProtocolResponseFormatter.class);
	private ProtocolResponseFormatter protocolResponseFormatter_default = PowerMock.createMock(ProtocolResponseFormatter.class);
	
	private Map<String, Item> classMap = new HashMap<String, Item>();
	private String executorType = "executorType";
	private String fakeExecutorType = "fakeExecutorType";
	private Item item = new Item();
	private Item item_default = new Item();
	
	
	@Override
	protected void setUp() throws Exception {
		item.setRequestParser(protocolRequestParser);
		item.setResponseFormatter(protocolResponseFormatter);
		item.setResponseClassName("com.telenav.cserver.class4test.SampleExecutorResponse");
		
		item_default.setRequestParser(protocolRequestParser_default);
		item_default.setResponseFormatter(protocolResponseFormatter_default);
		item_default.setResponseClassName("com.telenav.cserver.class4test.SampleExecutorResponse");
		
		classMap.put(executorType, item);
		classMap.put("Proto_"+executorType, item);
		classMap.put("DEFAULT", item_default);
		executorDataFactory.setClassMap(classMap);
	}
	public void testCreateProtocolRequestParser(){
		ProtocolRequestParser result = executorDataFactory.createProtocolRequestParser(executorType);
		assertEquals(protocolRequestParser,result);
		
		classMap.clear();
		try {
			executorDataFactory.createProtocolRequestParser(executorType);
		} catch (IllegalArgumentException e) {
			// an IllegalArgumentException is OK
		}
	}
	
	public void testCreateProtocolRequestParser_default(){
		ProtocolRequestParser result = executorDataFactory.createProtocolRequestParser(fakeExecutorType);
		assertEquals(protocolRequestParser_default,result);
	}
	
	public void testCreateProtocolResponseFormatter() {
		ProtocolResponseFormatter result = executorDataFactory.createProtocolResponseFormatter(executorType);
		assertEquals(protocolResponseFormatter, result);

		classMap.clear();
		try {
			executorDataFactory.createProtocolResponseFormatter(executorType);
		} catch (IllegalArgumentException e) {
			// an IllegalArgumentException is OK
		}
	}
	
	public void testCreateProtocolResponseFormatter_default(){
		ProtocolResponseFormatter result = executorDataFactory.createProtocolResponseFormatter(fakeExecutorType);
		assertEquals(protocolResponseFormatter_default,result);
	}
	
	public void testCreateExecutorResponse_Proto_executorType() {
		ExecutorResponse result = executorDataFactory.createExecutorResponse(executorType);
		assertNotNull(result);
		assertTrue(result instanceof SampleExecutorResponse);
		assertEquals(executorType, result.getExecutorType());

		classMap.clear();
		try {
			executorDataFactory.createExecutorResponse(executorType);
		} catch (IllegalArgumentException e) {
			// an IllegalArgumentException is OK
		}

		assertNull(executorDataFactory.getInstance());
		assertNotNull(executorDataFactory.getClassMap());
		assertNotNull(item.getResponseClassName());
	}
	
	public void testCreateExecutorResponse_default_executorType(){
		ExecutorResponse result = executorDataFactory.createExecutorResponse(fakeExecutorType);
		assertNotNull(result);
		assertTrue(result instanceof SampleExecutorResponse);
		assertEquals(fakeExecutorType,result.getExecutorType());
	}

}
