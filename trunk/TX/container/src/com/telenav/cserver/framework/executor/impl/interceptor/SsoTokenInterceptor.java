/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;
import com.telenav.kernel.sso.SsoTokenManager;

/**
 * SsoTokenInterceptor
 * This interceptor is supposed to use as a pre interceptor. 
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-17
 *
 */
public class SsoTokenInterceptor implements Interceptor 
{

	Logger log = Logger.getLogger(SsoTokenInterceptor.class);

	/** 
	 * reserved field
	 */
	boolean canUpdateToken = false;
	
	
	/**
	 * @return the canUpdateToken
	 */
	public boolean isCanUpdateToken() {
		return canUpdateToken;
	}


	/**
	 * @param canUpdateToken the canUpdateToken to set
	 */
	public void setCanUpdateToken(boolean canUpdateToken) {
		this.canUpdateToken = canUpdateToken;
	}


	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context) 
	{
		String ssoTokenString = request.getUserProfile().getSsoToken();
		if (log.isDebugEnabled())
		{
			log.debug("ssoToken:" + ssoTokenString);
		}
		if (!SsoTokenManager.isValidToken(ssoTokenString))
		{
			log.warn("InValidToken:" + ssoTokenString);
			//invalid SSO token
			response.setStatus(ExecutorResponse.STATUS_INVALID_IDENTITY);
			response.setErrorMessage("Invalid login");
			//response.setSsoToken(ssoTokenString);
			return InterceptResult.HALT;
		}
		else
		{
			//insert it into response, to ensure client will also save the latest string from server.
			response.setSsoToken(ssoTokenString);
		}
		
		return InterceptResult.PROCEED;
	}


}