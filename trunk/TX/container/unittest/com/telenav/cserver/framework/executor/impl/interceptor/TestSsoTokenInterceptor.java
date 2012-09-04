/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.kernel.sso.SsoTokenManager;

/**
 * TestSsoTokenInterceptor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SsoTokenManager.class)
public class TestSsoTokenInterceptor extends TestCase{
	private ExecutorRequest request;
	private ExecutorResponse response;
	private SsoTokenInterceptor ssoTokenInterceptor = new SsoTokenInterceptor();
	private UserProfile userProfile = new UserProfile();
	private String ssoToken = "testSsoToken";
	
	@Override
	protected void setUp() throws Exception {
		request = PowerMock.createMock(ExecutorRequest.class);
		response = PowerMock.createMock(ExecutorResponse.class);
		
		userProfile.setSsoToken(ssoToken);
	}
	public void testInterceptor()throws Exception{
		expect(request.getUserProfile()).andReturn(userProfile);
		response.setSsoToken(ssoToken);
		PowerMock.mockStatic(SsoTokenManager.class);
		expect(SsoTokenManager.isValidToken(ssoToken)).andReturn(true);
		PowerMock.replayAll();
		InterceptResult ret = ssoTokenInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(ret,InterceptResult.PROCEED);
	}
	
	public void testInterceptor_halt()throws Exception{
		expect(request.getUserProfile()).andReturn(userProfile);
		PowerMock.mockStatic(SsoTokenManager.class);
		expect(SsoTokenManager.isValidToken(ssoToken)).andReturn(false);
		response.setStatus(ExecutorResponse.STATUS_INVALID_IDENTITY);
		response.setErrorMessage("Invalid login");
		PowerMock.replayAll();
		InterceptResult ret = ssoTokenInterceptor.intercept(request, response, null);
		PowerMock.verifyAll();
		assertEquals(InterceptResult.HALT,ret);
	}
	
	public void testSimple(){
		ssoTokenInterceptor.isCanUpdateToken();
		ssoTokenInterceptor.setCanUpdateToken(true);

	}

}
