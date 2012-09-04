/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.Constants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ContextMgrService.class,ResourceHolderManager.class,TnContextInterceptor.class})
@SuppressStaticInitializationFor({"com.telenav.client.dsm.ContextMgrService","com.telenav.cserver.common.resource.ResourceHolderManager"})
public class TestTnContextInterceptor extends TestCase{
	//The Objects need to mock
	private ExecutorRequest request = PowerMock.createMock(ExecutorRequest.class);
	private ExecutorContext context = PowerMock.createMock(ExecutorContext.class);
	private TnContext tnContext = PowerMock.createMock(TnContext.class);
	
	private ContextMgrService cms = PowerMock.createMock(ContextMgrService.class);
	private ContextMgrStatus myStatus = PowerMock.createMock(ContextMgrStatus .class);
	private DsmRuleHolder dsmHolder = PowerMock.createMock(DsmRuleHolder.class);
	//
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private TnContext returnedTnContext;
	private Map<String,String> dsmResponsMap = new HashMap<String,String>();
	//invoked instance
	private TnContextInterceptor tnContextInterceptor = new TnContextInterceptor();
	
	private static final String DEFAULT_MAP_DATA = "Navteq";
	private static final String DEFAULT_POI_DATASET = "TA";
	private static final String PROP_DSM_SERVER_STATUS = "dsm_server_status";
	private static final String DSM_SERVER_DOWN = "down";
	private static final String SERVERURL = "testURL";
    public static final String NAV_FLOW_DATA_SRC_KEY="flow_data_src";
    public static final String NAV_ALERT_DATA_SRC_KEY="alert_data_src";
	
	
	@Override
	protected void setUp() throws Exception {
//		ClassPool.getDefault().insertClassPath(org.apache.axis2.context.ConfigurationContext.class.getName());

		dsmResponsMap.put("*","*");
		dsmResponsMap.put("**","**");
	}
	public void testInterceptor()throws Exception{
		int myStatus_getStatusCode = -1;
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(context.getTnContext()).andReturn(tnContext);
		EasyMock.expect(tnContext.size()).andReturn(0);
		EasyMock.expect(request.getUserProfile()).andReturn(new UserProfile());
		EasyMock.expect(context.getServerUrl()).andReturn(SERVERURL);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE)).andReturn(null);
		PowerMock.expectNew(ContextMgrService.class).andReturn(cms);
		EasyMock.expect(cms.updateContext(MatchBox.tnContextEquals(new TnContext()))).andReturn(myStatus);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.DSM_TYPE)).andReturn(dsmHolder);
		EasyMock.expect(dsmHolder.getDsmResponses(MatchBox.userProfileEquals(userProfile), MatchBox.tnContextEquals(new TnContext()))).andReturn(dsmResponsMap);
		EasyMock.expect(myStatus.getStatusCode()).andReturn(myStatus_getStatusCode).anyTimes();
		context.setTnContext(MatchBox.tnContextEquals(new TnContext()));
		PowerMock.replayAll();
		InterceptResult result = tnContextInterceptor.intercept(request,null, context);
		PowerMock.verifyAll();
		
		assertEquals(result,InterceptResult.PROCEED);
		
	}
	
	public void testInterceptor_TnContextSizeMoreThan0()throws Exception{
		EasyMock.expect(context.getTnContext()).andReturn(tnContext);
		EasyMock.expect(tnContext.size()).andReturn(1);
		PowerMock.replayAll();
		InterceptResult result = tnContextInterceptor.intercept(request,null, context);
		PowerMock.verifyAll();
		
		assertEquals(result,InterceptResult.PROCEED);
		
	}
	
	public void testGetDefaultTnContext_null() throws Exception{
		returnedTnContext = (TnContext)Whitebox.invokeMethod(tnContextInterceptor, "getDefaultTnContext", null);
		assertEquals(DEFAULT_MAP_DATA,returnedTnContext.getProperty(TnContext.PROP_MAP_DATASET));
		assertEquals(DEFAULT_POI_DATASET,returnedTnContext.getProperty(TnContext.PROP_POI_DATASET));
		assertEquals(DSM_SERVER_DOWN,returnedTnContext.getProperty(PROP_DSM_SERVER_STATUS));
	}
	
	public void testGetDefaultTnContext_notNull() throws Exception{
		TnContext testTC = new TnContext();
		Whitebox.invokeMethod(tnContextInterceptor, "getDefaultTnContext", testTC);
		assertEquals(DEFAULT_MAP_DATA,testTC.getProperty(TnContext.PROP_MAP_DATASET));
		assertEquals(DEFAULT_POI_DATASET,testTC.getProperty(TnContext.PROP_POI_DATASET));
		assertEquals(DSM_SERVER_DOWN,testTC.getProperty(PROP_DSM_SERVER_STATUS));
	}
	
	public void testGetTnContext() throws Exception{
		int myStatus_getStatusCode = 0;
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(context.getServerUrl()).andReturn(SERVERURL);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE)).andReturn(null);
		PowerMock.expectNew(ContextMgrService.class).andReturn(cms);
		EasyMock.expect(cms.updateContext(MatchBox.tnContextEquals(new TnContext()))).andReturn(myStatus);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.DSM_TYPE)).andReturn(dsmHolder);
		EasyMock.expect(dsmHolder.getDsmResponses(MatchBox.userProfileEquals(userProfile), MatchBox.tnContextEquals(new TnContext()))).andReturn(dsmResponsMap);
		EasyMock.expect(myStatus.getStatusCode()).andReturn(myStatus_getStatusCode).anyTimes();
		PowerMock.replayAll();
		
		
		returnedTnContext = tnContextInterceptor.getTnContext(userProfile, context);
		
		
		PowerMock.verifyAll();
		
		assertEquals(userProfile.getMin(),returnedTnContext.getProperty(TnContext.PROP_LOGIN_NAME));
		assertEquals(userProfile.getCarrier(),returnedTnContext.getProperty(TnContext.PROP_CARRIER));
		assertEquals(userProfile.getDevice(),returnedTnContext.getProperty(TnContext.PROP_DEVICE));
		assertEquals(userProfile.getPlatform(),returnedTnContext.getProperty(TnContext.PROP_PRODUCT));
		assertEquals(userProfile.getVersion(),returnedTnContext.getProperty(TnContext.PROP_VERSION));
		assertEquals(userProfile.getProduct(),returnedTnContext.getProperty("application"));
		assertEquals(Constants.CSERVER_CLASS,returnedTnContext.getProperty("c-server class"));
		assertEquals(SERVERURL,returnedTnContext.getProperty("c-server url"));
		assertEquals(userProfile.getMapDpi(),returnedTnContext.getProperty("map_dpi"));
		assertEquals(userProfile.getLocale(),returnedTnContext.getProperty("locale"));
		assertEquals(userProfile.getProgramCode(),returnedTnContext.getProperty("program_code"));
		assertEquals(userProfile.getProgramCode(),returnedTnContext.getProperty("program_code"));
		
		
		assertEquals(TnContext.TT_REQUESTOR_TNCLIENT,returnedTnContext.getProperty(TnContext.PROP_REQUESTOR));
		
		assertEquals("*",returnedTnContext.getProperty("*"));
		assertEquals("**",returnedTnContext.getProperty("**"));
		
		if(myStatus_getStatusCode != -1){
			assertEquals(DEFAULT_MAP_DATA,returnedTnContext.getProperty(TnContext.PROP_MAP_DATASET));
			assertEquals(DEFAULT_POI_DATASET,returnedTnContext.getProperty(TnContext.PROP_POI_DATASET));
			assertEquals(DSM_SERVER_DOWN,returnedTnContext.getProperty(PROP_DSM_SERVER_STATUS));
		}
	}
	
	
	public void testSimple(){
		TnContextInterceptor t = new TnContextInterceptor();
		t.isNeedRegister();
		t.setNeedRegister(false); 

	}
}
