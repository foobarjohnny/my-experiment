/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.class4test.GetAudioExecutor;
import com.telenav.cserver.class4test.SyncResourceExecutor;
import com.telenav.cserver.class4test.SampleExecutor;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.executor.Executor;
import com.telenav.cserver.framework.executor.ExecutorRegistry;

/**
 * TestDefaultExecutorRegistryLoader.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-26
 */
@RunWith(PowerMockRunner.class)     
@PrepareForTest({Configurator.class})     
public class TestDefaultExecutorRegistryLoader extends TestCase{
	private DefaultExecutorRegistryLoader defaultExecutorRegistryLoader;
	private DefaultExecutorRegistryLoader configReturnedObj;
	private ExecutorRegistry executorRegistry ;
	@Override
	protected void setUp() throws Exception {
		//define the object for calling loadRegistry() method
		defaultExecutorRegistryLoader = new DefaultExecutorRegistryLoader();
		
		//define the object for setting the return value of the mocked static class
		configReturnedObj = new DefaultExecutorRegistryLoader();
		
		//construct the List whose each element is a Object of Executor
		//The class TestExecutor, GetAudioExecutor and SyncResourceExecutor are for test only, you can delete them if necessary
		List<Executor> executorList = new ArrayList<Executor>();
		executorList.add(new SampleExecutor("Test"));
		executorList.add(new GetAudioExecutor("GetAudio"));
		executorList.add(new SyncResourceExecutor("SyncResource"));
		configReturnedObj.setExecutorList(executorList);
		
		//define the parameter of loadRegistry() method
		executorRegistry = new ExecutorRegistry();
	}
	
	public void testLoadRegistry()throws Exception{
		//mock Static class
		PowerMock.mockStatic(Configurator.class);
		expect(Configurator.getObject("executor/executor_mapping.xml", "executor_registry_loader")).andReturn(configReturnedObj);
		PowerMock.replayAll();
		defaultExecutorRegistryLoader.loadRegistry(executorRegistry);
		PowerMock.verifyAll();
		
		//assert
		assertEquals("Test", executorRegistry.getAction("Test").getExecutorType()) ;
		assertEquals("GetAudio", executorRegistry.getAction("GetAudio").getExecutorType()) ;
		assertEquals("SyncResource", executorRegistry.getAction("SyncResource").getExecutorType()) ;
	}

}
