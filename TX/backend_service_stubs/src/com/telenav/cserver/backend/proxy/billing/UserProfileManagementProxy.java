/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.userprofile2.UserProfileManagementServiceStub;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.GetUserProfilePreferencesRequest;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.GetUserProfilePreferencesResponse;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesRequest;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * UserProfileManagementProxy
 * 
 * @author kwwang
 * 
 */
@BackendProxy
@ThrottlingConf("UserProfileManagementProxy")
public class UserProfileManagementProxy extends
		AbstractStubProxy<UserProfileManagementServiceStub> {
	private Logger logger = Logger.getLogger(UserProfileManagementProxy.class);

	@Override
	public String getProxyConfType() {
		return "WS_SERVICE_USERPROFILEMANAGEMENT";
	}

	@ProxyDebugLog
	@Throttling
	public GetUserProfilePreferencesResponse getUserProfilePreferences(
			GetUserProfilePreferencesRequest request, TnContext tc) {
		UserProfileManagementServiceStub stub = null;
		GetUserProfilePreferencesResponse response = null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("authenticate");
		cli.addData("GetUserProfilePreferencesRequest",
				ReflectionToStringBuilder.toString(request));
		try {
			stub = createStub(getWebServiceConfigInterface());
			response = stub.getUserProfilePreferences(request);
			cli.addData("GetUserProfilePreferencesResponse", ReflectionToStringBuilder.toString(request));
		} catch (Exception ex) {
			cli.setStatus(ex);
			logger.fatal("getUserProfilePreferences failed,", ex);
		} finally {
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}
	
	
	@ProxyDebugLog
	@Throttling
	public UpdateUserProfilePreferencesResponse updateUserProfilePreferences(
			UpdateUserProfilePreferencesRequest request, TnContext tc) {
		UserProfileManagementServiceStub stub = null;
		UpdateUserProfilePreferencesResponse response = null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("UpdateUserProfilePreferences");
		cli.addData("GetUserProfilePreferencesRequest",
				ReflectionToStringBuilder.toString(request));
		try {
			stub = createStub(getWebServiceConfigInterface());
			response = stub.updateUserProfilePreferences(request);
			cli.addData("UpdateUserProfilePreferencesResponse", ReflectionToStringBuilder.toString(request));
		} catch (Exception ex) {
			cli.setStatus(ex);
			logger.fatal("updateUserProfilePreferences failed,", ex);
		} finally {
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}

	@Override
	protected UserProfileManagementServiceStub createStub(
			WebServiceConfigInterface ws) throws Exception {
		UserProfileManagementServiceStub stub = null;
		try {
			stub = new UserProfileManagementServiceStub(createContext(ws),
					ws.getServiceUrl());
			stub._getServiceClient()
					.getOptions()
					.setTimeOutInMilliSeconds(
							ws.getWebServiceItem().getWebServiceTimeout());
		} catch (Exception e) {
			logger.fatal("create IdentityServiceStub stub failed", e);
		}

		return stub;
	}

}
