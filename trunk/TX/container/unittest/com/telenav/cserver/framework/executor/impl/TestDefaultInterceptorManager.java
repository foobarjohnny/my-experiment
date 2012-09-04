/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor1;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPostInterceptor2;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPreInterceptor;
import com.telenav.cserver.class4test.interceptor.SampleGlobalPreInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor1;
import com.telenav.cserver.class4test.interceptor.SamplePostInterceptor2;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor;
import com.telenav.cserver.class4test.interceptor.SamplePreInterceptor1;
import com.telenav.cserver.framework.executor.Interceptor;

/**
 * TestDefaultInterceptorManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-27
 */
public class TestDefaultInterceptorManager extends TestCase{
	private DefaultInterceptorManager defaultInterceptorManager = new DefaultInterceptorManager();
	private Interceptor interceptor;
	private List<Interceptor> preInterceptorsList= new ArrayList<Interceptor>();
	private List<Interceptor> postInterceptorsList= new ArrayList<Interceptor>();
	private String serviceType = "serviceType";
	private InterceptorConfigurationItem interceptorConfigurationItem = new InterceptorConfigurationItem();
	private InterceptorConfigurationItem interceptorConfigurationItem1 = new InterceptorConfigurationItem();
	@Override
	protected void setUp() throws Exception {
		interceptor = PowerMock.createMock(Interceptor.class);
	}
	public void testAddPostInterceptor()throws Exception{
		PowerMock.replayAll();
		defaultInterceptorManager.addPostInterceptor(serviceType, interceptor);
		defaultInterceptorManager.addPostInterceptor(serviceType, interceptor);
		PowerMock.verifyAll();
		postInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPostInterceptors(serviceType);
		assertEquals(2,postInterceptorsList.size());
	}
	
	public void testAddPreInterceptor()throws Exception{
		PowerMock.replayAll();
		defaultInterceptorManager.addPreInterceptor(serviceType, interceptor);
		defaultInterceptorManager.addPreInterceptor(serviceType, interceptor);
		PowerMock.verifyAll();
		preInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPreInterceptors(serviceType);
		assertEquals(2,preInterceptorsList.size());
	}
	
	public void testRemovePostInterceptor()throws Exception{   
		PowerMock.replayAll();
		defaultInterceptorManager.addPostInterceptor(serviceType, interceptor);
		defaultInterceptorManager.removePostInterceptor(serviceType, null);
		PowerMock.verifyAll();
		postInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPostInterceptors(serviceType);
		assertEquals(0,postInterceptorsList.size());
	}
	
	public void testRemovePreInterceptor()throws Exception{
		PowerMock.replayAll();
		defaultInterceptorManager.addPreInterceptor(serviceType, interceptor);
		defaultInterceptorManager.removePreInterceptor(serviceType, null);
		PowerMock.verifyAll();
		preInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPreInterceptors(serviceType);
		assertEquals(0,preInterceptorsList.size());
	}
	
	public void testSetInterceptors(){
		//post interceptors
		List<Interceptor> postInterceptorList = new ArrayList<Interceptor>();
		postInterceptorList.add(new SamplePostInterceptor());
		postInterceptorList.add(new SamplePostInterceptor1());
		postInterceptorList.add(new SamplePostInterceptor2());
		//pre interceptors
		List<Interceptor> preInterceptorList = new ArrayList<Interceptor>();
		preInterceptorList.add(new SamplePreInterceptor());
		preInterceptorList.add(new SamplePreInterceptor1());
		//interceptorConfigurationItems
		interceptorConfigurationItem.setType(serviceType);
		interceptorConfigurationItem.setPostInterceptors(postInterceptorList);
		interceptorConfigurationItem.setPreInterceptors(preInterceptorList);
		
		interceptorConfigurationItem1.setType(serviceType);
		interceptorConfigurationItem1.setPostInterceptors(postInterceptorList);
		interceptorConfigurationItem1.setPreInterceptors(preInterceptorList);
		//List of interceptorConfigurationItems
		List<InterceptorConfigurationItem> interceptorConfigurationList = new ArrayList<InterceptorConfigurationItem>();
		interceptorConfigurationList.add(interceptorConfigurationItem);
		interceptorConfigurationList.add(interceptorConfigurationItem1);
		//invoke
		defaultInterceptorManager.setInterceptors(interceptorConfigurationList);
		//assert
		postInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPostInterceptors(serviceType);
		preInterceptorsList = (List<Interceptor>)defaultInterceptorManager.getAllPreInterceptors(serviceType);
		assertEquals(6,postInterceptorsList.size());
		assertEquals(4,preInterceptorsList.size());
	}
	
	public void testGetAllPostGlobalInterceptors(){
		defaultInterceptorManager.addPostInterceptor("Global", new SampleGlobalPostInterceptor());
		defaultInterceptorManager.addPostInterceptor("Global", new SampleGlobalPostInterceptor1());
		defaultInterceptorManager.addPostInterceptor("Global", new SampleGlobalPostInterceptor2());
		
		List<Interceptor> result = defaultInterceptorManager.getAllPostGlobalInterceptors();
		
		assertEquals(3,result.size());
	}
	
	public void testGetAllPreGlobalInterceptors(){
		defaultInterceptorManager.addPreInterceptor("Global", new SampleGlobalPreInterceptor());
		defaultInterceptorManager.addPreInterceptor("Global", new SampleGlobalPreInterceptor1());
		
		List<Interceptor> result = defaultInterceptorManager.getAllPreGlobalInterceptors();
		
		assertEquals(2,result.size());
	}

}
