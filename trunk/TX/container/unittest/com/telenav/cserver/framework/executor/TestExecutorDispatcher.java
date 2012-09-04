/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.class4test.SampleDataHandler;
import com.telenav.cserver.class4test.SampleExecutor;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor1;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor2;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPreInterceptor;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPreInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor2;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptorHalt;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptorHalt;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.impl.DefaultResponseDataHandlerManager;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.unittestutil.MockHttpServletResponse;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestExecutorDispatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-5
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExecutorDispatcher.class)
@SuppressStaticInitializationFor("com.telenav.cserver.framework.executor.ExecutorDispatcher")
public class TestExecutorDispatcher extends TestCase{
	//TODO use reflect to 
	private ExecutorDispatcher executorDispatcher ;//= Whitebox.newInstance(ExecutorDispatcher.class);
	private ExecutorRequest request = PowerMock.createMock(ExecutorRequest.class);
	private ExecutorResponse response = PowerMock.createMock(ExecutorResponse.class);
	private ExecutorContext context = new ExecutorContext();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private InterceptorManager interceptorManager = PowerMock.createMock(InterceptorManager.class);
	private String executorType = "executorType";
	private ExecutorRegistry executorRegistry = PowerMock.createMock(ExecutorRegistry.class);
	private SampleExecutor sampleExecutor = new SampleExecutor();
	private DevicePropertiesHolder devicePropertiesHolder = PowerMock.createMock(DevicePropertiesHolder.class);
	private DeviceProperties dp = PowerMock.createMock(DeviceProperties.class);
	private List<Interceptor> preInterceptorList = new ArrayList<Interceptor>();
	private List<Interceptor> globalPreInterceptorList = new ArrayList<Interceptor>();
	private List<Interceptor> postInterceptorList = new ArrayList<Interceptor>();
	private List<Interceptor> globalPostInterceptorList = new ArrayList<Interceptor>();
	
	@Override
	protected void setUp() throws Exception {
		Constructor<?> constructor = ExecutorDispatcher.class.getDeclaredConstructors()[0];//("ExecutorDispatcher");
		executorDispatcher = (ExecutorDispatcher)constructor.newInstance();
		
		Whitebox.setInternalState(ExecutorDispatcher.class, "logger", Logger.getLogger(ExecutorDispatcher.class));
		executorDispatcher.setInterceptorManager(interceptorManager);
		executorDispatcher.setExecutorRegistry(executorRegistry);
		//set static field value
		Whitebox.setInternalState(ExecutorDispatcher.class, "devicePropertiesHolder", devicePropertiesHolder);
		Whitebox.setInternalState(ExecutorDispatcher.class, "logger", Logger.getLogger(ExecutorDispatcher.class));
		//construct interceptors
		preInterceptorList.add(new SamplePreInterceptor());
		preInterceptorList.add(new SamplePreInterceptor1());
		
		globalPreInterceptorList.add(new SampleGlobalPreInterceptor());
		globalPreInterceptorList.add(new SampleGlobalPreInterceptor1());
		
		postInterceptorList.add(new SamplePostInterceptor());
		postInterceptorList.add(new SamplePostInterceptor1());
		postInterceptorList.add(new SamplePostInterceptor2());
		
		globalPostInterceptorList.add(new SampleGlobalPostInterceptor());
		globalPostInterceptorList.add(new SampleGlobalPostInterceptor1());
		globalPostInterceptorList.add(new SampleGlobalPostInterceptor2());
	}
	public void testExecute() throws Exception{
		//normal invoke
		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(userProfile, context.getTnContext())).andReturn(dp);
		EasyMock.expect(dp.getInt("MAX_PAYLOAD_SIZE", -1)).andReturn(10);
		response.setMaxPayloadSize(10);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(sampleExecutor);
		//postInterceptors
		EasyMock.expect(interceptorManager.getAllPostInterceptors(executorType)).andReturn(postInterceptorList);
		EasyMock.expect(interceptorManager.getAllPostGlobalInterceptors()).andReturn(globalPostInterceptorList);
		PowerMock.replayAll();
		
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
		
	}
	public void testExecute_with_null_DeviceProperties()throws Exception{
		//normal invoke
		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(userProfile, context.getTnContext())).andReturn(null);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(sampleExecutor);
		//postInterceptors
		EasyMock.expect(interceptorManager.getAllPostInterceptors(executorType)).andReturn(postInterceptorList);
		EasyMock.expect(interceptorManager.getAllPostGlobalInterceptors()).andReturn(globalPostInterceptorList);
		PowerMock.replayAll();
		
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
	}
	public void testExecute_with_null_devicePropertiesHolder()throws Exception{
		Object nullObj = null;
		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		Whitebox.setInternalState(ExecutorDispatcher.class, "devicePropertiesHolder", nullObj);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(sampleExecutor);
		//postInterceptors
		EasyMock.expect(interceptorManager.getAllPostInterceptors(executorType)).andReturn(postInterceptorList);
		EasyMock.expect(interceptorManager.getAllPostGlobalInterceptors()).andReturn(globalPostInterceptorList);
		PowerMock.replayAll();
		
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
		
		Whitebox.setInternalState(ExecutorDispatcher.class, "devicePropertiesHolder", devicePropertiesHolder);
	}
	public void testExecute_withDataHandlerManager() throws Exception{
		DefaultResponseDataHandlerManager dataHandlerManager = new DefaultResponseDataHandlerManager();
		dataHandlerManager.addDataHandlers(executorType, new SampleDataHandler());
		dataHandlerManager.addDataHandlers(executorType, new SampleDataHandler());
		dataHandlerManager.addDataHandlers(executorType, new SampleDataHandler());
		executorDispatcher.setDataHandlerManager(dataHandlerManager);
		
		
		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(userProfile, context.getTnContext())).andReturn(dp);
		EasyMock.expect(dp.getInt("MAX_PAYLOAD_SIZE", -1)).andReturn(10);
		response.setMaxPayloadSize(10);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(sampleExecutor);
		//postInterceptors
		EasyMock.expect(interceptorManager.getAllPostInterceptors(executorType)).andReturn(postInterceptorList);
		EasyMock.expect(interceptorManager.getAllPostGlobalInterceptors()).andReturn(globalPostInterceptorList);
		
		// servletTransportor
		ServletTransportor servletTransportor = PowerMock.createMock(ServletTransportor.class);
		context.setTransportor(servletTransportor);
		EasyMock.expect(servletTransportor.getResponse()).andReturn(new MockHttpServletResponse());

		PowerMock.replayAll();
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
	}
	public void testExecute_exception() throws Exception{
		//executor == null && userProfile == null
		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(null).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(null, context.getTnContext())).andReturn(dp);
		EasyMock.expect(dp.getInt("MAX_PAYLOAD_SIZE", -1)).andReturn(10);
		response.setMaxPayloadSize(10);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(null);
		PowerMock.replayAll();
		
		try {
			executorDispatcher.execute(request, response, context);
		} catch (ExecutorException e) {
			UnittestUtil.printExceptionMsg(e);
		}
		PowerMock.verifyAll();
	}
	
	private void go(Executor executor,UserProfile up) throws Exception{
		
	}
	
	public void testPreHalt() throws Exception{
		//perIntorceptor halt
		SamplePreInterceptorHalt spih = new SamplePreInterceptorHalt();
		preInterceptorList.add(spih);

		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(userProfile, context.getTnContext())).andReturn(dp);
		EasyMock.expect(dp.getInt("MAX_PAYLOAD_SIZE", -1)).andReturn(10);
		response.setMaxPayloadSize(10);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		
		
		PowerMock.replayAll();
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
		
		
		preInterceptorList.remove(spih);//ensure the following code can be executed correctly
		
	}
	public void testPostHalt() throws Exception{
		
		//postIntorceptor halt
		SamplePostInterceptorHalt spoih = new SamplePostInterceptorHalt();
		postInterceptorList.add(spoih);

		EasyMock.expect(request.getExecutorType()).andReturn(executorType).anyTimes();
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile).anyTimes();
		//response.setMaxPayloadSize
		EasyMock.expect(devicePropertiesHolder.getDeviceProperties(userProfile, context.getTnContext())).andReturn(dp);
		EasyMock.expect(dp.getInt("MAX_PAYLOAD_SIZE", -1)).andReturn(10);
		response.setMaxPayloadSize(10);
		//preInterceptors
		EasyMock.expect(interceptorManager.getAllPreInterceptors(executorType)).andReturn(preInterceptorList);
		EasyMock.expect(interceptorManager.getAllPreGlobalInterceptors()).andReturn(globalPreInterceptorList);
		
		//execute
		EasyMock.expect(executorRegistry.getAction(executorType)).andReturn(sampleExecutor);
		//postInterceptors
		EasyMock.expect(interceptorManager.getAllPostInterceptors(executorType)).andReturn(postInterceptorList);
		EasyMock.expect(interceptorManager.getAllPostGlobalInterceptors()).andReturn(globalPostInterceptorList);
		PowerMock.replayAll();
		
		executorDispatcher.execute(request, response, context);
		PowerMock.verifyAll();
		
		postInterceptorList.remove(spoih);//ensure the following code can be executed correctly
	}
	
	public void testSimple() throws Exception{
		ExecutorDispatcher ed = Whitebox.invokeConstructor(ExecutorDispatcher.class);
		ed.getInterceptorManager();
		ed.setInterceptorManager(null);
		ed.getExecutorRegistryLoader();
		ed.setExecutorRegistryLoader(null);
		ed.setExecutorRegistry(null);
		ed.getDataHandlerManager();
		ed.setDataHandlerManager(null);

	}

}
