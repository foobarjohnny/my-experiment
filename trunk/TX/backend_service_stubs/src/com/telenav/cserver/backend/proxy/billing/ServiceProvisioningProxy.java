/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.identity.ServiceIdentityStub;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeResponse;
import com.telenav.billing2.ws.datatypes.identity.service.CheckPTNVerificationCodeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.CheckPTNVerificationCodeResponse;
import com.telenav.billing2.ws.datatypes.identity.service.GetAccountStatusRequest;
import com.telenav.billing2.ws.datatypes.identity.service.GetAccountStatusResponse;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceManager;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("ServiceProvisioningProxy")
public class ServiceProvisioningProxy extends AbstractStubProxy<ServiceIdentityStub>
{
    private Logger logger = Logger.getLogger(ServiceProvisioningProxy.class);

    protected ServiceProvisioningProxy()
    {
    }

    @ProxyDebugLog
    @Throttling
    public GetAccountStatusResponse getAccountStatus(GetAccountStatusRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getAccountStatus");

        cli.addData("getAccountStatusRequest", ReflectionToStringBuilder.toString(request));

        GetAccountStatusResponse response = null;
        ServiceIdentityStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.getAccountStatus(request);
            cli.addData("getAccountStatusResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
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
    public SendPTNVerificationCodeResponse sendPTNVerificationCode(SendPTNVerificationCodeRequest request, TnContext tc)
            throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("sendPTNVerificationCode");

        cli.addData("sendPTNVerificationCodeReq", ReflectionToStringBuilder.toString(request));

        SendPTNVerificationCodeResponse response = null;
        ServiceIdentityStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.sendPTNVerificationCode(request);
            cli.addData("sendPTNVerificationCodeResp", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
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
    public CheckPTNVerificationCodeResponse checkPTNVerificationCode(CheckPTNVerificationCodeRequest request, TnContext tc)
            throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("checkPTNVerificationCode");

        cli.addData("checkPTNVerificationCodeReq", ReflectionToStringBuilder.toString(request));

        CheckPTNVerificationCodeResponse response = null;
        ServiceIdentityStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.checkPTNVerificationCode(request);
            cli.addData("checkPTNVerificationCodeResp", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
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
    public AuthorizeResponse authenticate(AuthorizeRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("authenticate");

        cli.addData("AuthenticateRequest", ReflectionToStringBuilder.toString(request));

        AuthorizeResponse response = null;
        ServiceIdentityStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.authorize(request);
            cli.addData("AuthenticateResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }

    @Override
    protected ServiceIdentityStub createStub(WebServiceConfigInterface ws) throws Exception
    {
        ServiceIdentityStub stub = null;
        try
        {
            stub = new ServiceIdentityStub(createContext(ws), ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (Exception e)
        {
            logger.fatal("create ServiceProvisioningStub stub failed", e);
        }

        return stub;
    }

    @Override
    public String getProxyConfType()
    {
        return "WS_SERVICE_PROVISION";
    }
}
