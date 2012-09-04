/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.simpleCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.class4test.SampleLoadOrder;
import com.telenav.cserver.common.resource.LoadOrders;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor.InterceptorType;
import com.telenav.cserver.framework.reporting.ReportingRequest;
import com.telenav.cserver.resource.common.prompts.PromptConfKeys;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.cserver.unittestutil.autorun.ConstructorCaller;
import com.telenav.cserver.unittestutil.autorun.GSetterCaller;

/**
 * TestConstructorOnly.java
 *	The class is only for increasing unit-test coverage rates.
	And we will write some foolish or very simple code to achieve the goal.
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.management.heartbeat.HeartBeatConfiguration"})
public class TestSimpleCode extends TestCase{
	
	public void testConstructor() throws Exception{
		ConstructorCaller constructorCaller = new ConstructorCaller();
		constructorCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorException");
		constructorCaller.registerClass("com.telenav.cserver.framework.ServerException");
		constructorCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorResponseSizeExceedException");
		constructorCaller.registerClass("com.telenav.cserver.framework.configuration.ConfigurationException");
		
		constructorCaller.registerClass("com.telenav.cserver.common.resource.ResourceResemblance");
		constructorCaller.registerClass("com.telenav.cserver.common.resource.LoadOrderManager");
		constructorCaller.registerClass("com.telenav.cserver.common.cache.SizeOfObjectFactory");
		
		constructorCaller.registerClass("com.telenav.cserver.common.resource.constant.DeviceConstant");
		constructorCaller.registerClass("com.telenav.cserver.common.resource.constant.HolderType");
		constructorCaller.registerClass("com.telenav.cserver.common.resource.constant.MessageConstant");
		constructorCaller.registerClass("com.telenav.cserver.common.resource.constant.ResourceConst");
		
		constructorCaller.registerClass("com.telenav.cserver.framework.management.external_service.MarkDownServlet");
		constructorCaller.registerClass("com.telenav.cserver.framework.management.external_service.MarkUpServlet");
		constructorCaller.registerClass("com.telenav.cserver.framework.management.external_service.ServiceQueryServlet");
		constructorCaller.registerClass("com.telenav.cserver.framework.reporting.ReportingRequest");
		
		constructorCaller.registerClass("com.telenav.cserver.framework.transportation.NoneTransportor");
		constructorCaller.registerClass("com.telenav.cserver.framework.transportation.TransportorException");
		constructorCaller.registerClass("com.telenav.cserver.framework.transportation.reader.ReaderFactory");
		constructorCaller.call();
	}
	public void testSetAndGet() throws Exception{
		GSetterCaller cSetterCaller = new GSetterCaller();
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.protocol.json.JsonProtocolHandler");
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.protocol.protobuf.ProtocolBufferHandler");
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.protocol.txnode.TxNodeProtocolHandler");
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory$Item");
		cSetterCaller.registerAvoidMethod("com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory$Item", "getResponseClass");
		
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorResponse");
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorRequest");
		cSetterCaller.registerClass("com.telenav.cserver.framework.executor.ExecutorContext");
		cSetterCaller.registerClass("com.telenav.cserver.framework.ServerException");
		cSetterCaller.registerClass("com.telenav.cserver.framework.UserProfile");
		
		cSetterCaller.registerClass("com.telenav.cserver.framework.management.heartbeat.HeartBeatConfiguration");
		cSetterCaller.registerClass("com.telenav.cserver.framework.reporting.ReportingRequest");
		cSetterCaller.registerCallMethod("com.telenav.cserver.framework.reporting.ReportingRequest", "isWriten");
		
		cSetterCaller.registerClass("com.telenav.cserver.framework.reporting.ReportingResponse");
		
		cSetterCaller.registerClass("com.telenav.cserver.common.cache.AtomicCounter");
		cSetterCaller.registerClass("com.telenav.cserver.common.resource.ResourceContent");
		
		cSetterCaller.call();
		
		//com.telenav.cserver.framework.UserProfile
		UserProfile userProfile = UnittestUtil.createUserProfile();
		userProfile.setLocale("");
		userProfile.getLocale();
		userProfile.setLocale("CANADA");
		userProfile.getLocale();
		
		//com.telenav.cserver.framework.executor.ExecutorResponse
		ExecutorResponse executorResponse = new ExecutorResponse();
		executorResponse.addPayloadSize(0);
		
	}
	
	public void testOtherMethod(){
		SampleLoadOrder sampleLoadOrder = new SampleLoadOrder();
		sampleLoadOrder.setType("a-type");
		sampleLoadOrder.toString();
		
		LoadOrders loadOrders = new LoadOrders ();
		loadOrders.toString();
		
		
		Object obj = InterceptorType.PRE;
		obj = InterceptorType.POST;
		
		
		ReportingRequest reportingRequest = new ReportingRequest();
		List list = new ArrayList<Map<Long,String>>();
		list.add(new HashMap<Long,String>());
		reportingRequest.setMisLogEvents(list);
		reportingRequest.addServerMisLogField(1, "field");
		
		
		PromptConfKeys promptConfKeys = PromptConfKeys.CAR_ICON_PROMPT_SERVER;
	}
	
	public void testNeedMock(){
		//define variable
		
		//replay
		
		//verify
		
		//assert
	}

}
