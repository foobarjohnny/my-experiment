/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.accountidentity.IdentityServiceStub;
import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationRequest;
import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationResponse;
import com.telenav.billing2.accountidentity.dataTypes.AuthenticateRequest;
import com.telenav.billing2.accountidentity.dataTypes.AuthenticateResponse;
import com.telenav.billing2.accountidentity.dataTypes.ConfirmAccountRequest;
import com.telenav.billing2.accountidentity.dataTypes.ConfirmAccountResponse;
import com.telenav.billing2.accountidentity.dataTypes.CreateAccountRequest;
import com.telenav.billing2.accountidentity.dataTypes.CreateAccountResponse;
import com.telenav.billing2.accountidentity.dataTypes.GetExternalUserCredentialRequest;
import com.telenav.billing2.accountidentity.dataTypes.GetExternalUserCredentialResponse;
import com.telenav.billing2.accountidentity.dataTypes.GetPasswordRequest;
import com.telenav.billing2.accountidentity.dataTypes.GetPasswordResponse;
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
 * AccountidentityProxy
 * @author kwwang
 *
 */
@BackendProxy
@ThrottlingConf("AccountidentityProxy")
public class AccountidentityProxy extends
		AbstractStubProxy<IdentityServiceStub> {
	private Logger logger = Logger.getLogger(AccountidentityProxy.class);

	@Override
	public String getProxyConfType() {
		return "WS_SERVICE_ACCOUNTIDENTITY";
	}
	
	@ProxyDebugLog
    @Throttling
	public AuthenticateResponse authenticate(AuthenticateRequest request, TnContext tc)
	{
		IdentityServiceStub stub=null;
		AuthenticateResponse response=null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("authenticate");
        cli.addData("AuthenticateRequest", ReflectionToStringBuilder.toString(request));
		try
		{
			stub=createStub(getWebServiceConfigInterface());
			response=stub.authenticate(request);
			cli.addData("AuthenticateResponse",
					ReflectionToStringBuilder.toString(response));
		}
		catch(Exception ex)
		{
			cli.setStatus(ex);
			logger.fatal("authenticate failed, ",ex);
		}
		finally
		{
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}
	
	
	@ProxyDebugLog
    @Throttling
	public GetExternalUserCredentialResponse getExternalUserCredential(GetExternalUserCredentialRequest request, TnContext tc)
	{
		IdentityServiceStub stub=null;
		GetExternalUserCredentialResponse response=null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GetExternalCredential");
        cli.addData("GetExternalCredentialRequest", ReflectionToStringBuilder.toString(request));
		try
		{
			stub=createStub(getWebServiceConfigInterface());
			response=stub.getExternalUserCredential(request);
			cli.addData("GetExternalCredentialResponse",
					ReflectionToStringBuilder.toString(response));
		}
		catch(Exception ex)
		{
			cli.setStatus(ex);
			logger.fatal("GetExternalCredential failed, ",ex);
		}
		finally
		{
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}

	
	
	@ProxyDebugLog
    @Throttling
	public GetPasswordResponse getPassword(GetPasswordRequest request, TnContext tc)
	{
		IdentityServiceStub stub=null;
		GetPasswordResponse response=null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GetPassword");
        cli.addData("GetPasswordRequest", ReflectionToStringBuilder.toString(request));
		try
		{
			stub=createStub(getWebServiceConfigInterface());
			response=stub.getPassword(request);
			cli.addData("GetPasswordResponse",
					ReflectionToStringBuilder.toString(response));
		}
		catch(Exception ex)
		{
			cli.setStatus(ex);
			logger.fatal("getPassword failed, ",ex);
		}
		finally
		{
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}
	
	@ProxyDebugLog
    @Throttling
	public CreateAccountResponse createAccount(CreateAccountRequest request, TnContext tc)
	{
		IdentityServiceStub stub=null;
		CreateAccountResponse response=null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("CreateAccount");
        cli.addData("CreateAccountRequest", ReflectionToStringBuilder.toString(request));
		try
		{
			stub=createStub(getWebServiceConfigInterface());
			response=stub.createAccount(request);
			cli.addData("CreateAccountResponse",
					ReflectionToStringBuilder.toString(response));
		}
		catch(Exception ex)
		{
			cli.setStatus(ex);
			logger.fatal("CreateAccount failed, ",ex);
		}
		finally
		{
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}
	
	@ProxyDebugLog
    @Throttling
	public AddGlobalUserAssociationResponse addGlobalUserAssociation(AddGlobalUserAssociationRequest request, TnContext tc)
	{
		IdentityServiceStub stub=null;
		AddGlobalUserAssociationResponse response=null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("AddGlobalUserAssociation");
        cli.addData("AddGlobalUserAssociationRequest", ReflectionToStringBuilder.toString(request));
		try
		{
			stub=createStub(getWebServiceConfigInterface());
			response=stub.addGlobalUserAssociation(request);
			cli.addData("AddGlobalUserAssociationResponse",
					ReflectionToStringBuilder.toString(response));
		}
		catch(Exception ex)
		{
			cli.setStatus(ex);
			logger.fatal("AddGlobalUserAssociation failed, ",ex);
		}
		finally
		{
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}
		return response;
	}
	
//	@ProxyDebugLog
//    @Throttling
//	public ConfirmAccountResponse confirmAccount(ConfirmAccountRequest request, TnContext tc)
//	{
//		IdentityServiceStub stub=null;
//		ConfirmAccountResponse response=null;
//		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
//        cli.setFunctionName("CreateAccount");
//        cli.addData("CreateAccountRequest", ReflectionToStringBuilder.toString(request));
//		try
//		{
//			stub=createStub(getWebServiceConfigInterface());
//			response=stub.confirmAccount(request);
//			cli.addData("CreateAccountResponse",
//					ReflectionToStringBuilder.toString(response));
//		}
//		catch(Exception ex)
//		{
//			cli.setStatus(ex);
//			logger.fatal("CreateAccount failed, ",ex);
//		}
//		finally
//		{
//			cli.complete();
//			WebServiceUtils.cleanupStub(stub);
//		}
//		return response;
//	}
	
	@Override
	protected IdentityServiceStub createStub(WebServiceConfigInterface ws)
			throws Exception {
		IdentityServiceStub stub = null;
		try {
			stub = new IdentityServiceStub(createContext(ws),
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
