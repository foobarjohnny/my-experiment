/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeResponse;
import com.telenav.billing2.ws.datatypes.identity.service.CheckPTNVerificationCodeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.CheckPTNVerificationCodeResponse;
import com.telenav.billing2.ws.datatypes.identity.service.GetAccountStatusResponse;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.RecursionMultilineToStringStyle;
import com.telenav.cserver.backend.proxy.billing.ServiceProvisioningHelper;
import com.telenav.cserver.backend.proxy.billing.ServiceProvisioningProxy;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestServiceProvisionProxy
 * 
 * @author kwwang
 * 
 */
public class TestServiceProvisionProxy
{
    private UserProfile user;

    private TnContext tc = new TnContext();

    @Before
    public void Setup()
    {
        user = new UserProfile();
        user.setPlatform("ANDROID");
        user.setDevice("default");
        user.setProgramCode("ATTNAVPROG");
        user.setVersion("7.0.01");
        user.setUserId("16092933");
        user.setMin("7418529630");
        user.setDeviceCarrier("attnav");

    }

    @Test
    public void getAccountStatus() throws Exception
    {
        GetAccountStatusResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ServiceProvisioningProxy.class)
                .getAccountStatus(
                    ServiceProvisioningHelper.newGetAccountStatusRequestWithSpecificService(user, tc,
                        ServiceProvisioningHelper.NAVIGATION_APP_CODE), tc);
        Assert.assertEquals("0000", response.getResponseCode());
    }

    @Test
    public void anthenticate() throws Exception
    {
        AuthorizeRequest request = ServiceProvisioningHelper.newAuthenticateRequest4NavigationLogin(user, tc, "");
        request.setResetEqpin(NonEmptyConverter.toNonEmptyBoolean(false));
        AuthorizeResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ServiceProvisioningProxy.class)
                .authenticate(request, tc);
        Assert.assertEquals("0000", response.getResponseCode());
    }

    @Test
    public void sendPTNVerificationCode() throws Exception
    {
        SendPTNVerificationCodeRequest request = ServiceProvisioningHelper.newSendPTNVerificationCodeRequest(user, tc);
        SendPTNVerificationCodeResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
            ServiceProvisioningProxy.class).sendPTNVerificationCode(request, tc);
        Assert.assertEquals("0000", response.getResponseCode());
    }

    @Test
    public void checkPTNVerificationCode() throws Exception
    {
        CheckPTNVerificationCodeRequest request = ServiceProvisioningHelper.newCheckPTNVerificationCodeRequest(user, tc);
        request.setVerificationCode("590181");
        CheckPTNVerificationCodeResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
            ServiceProvisioningProxy.class).checkPTNVerificationCode(request, tc);
        Assert.assertEquals("0000", response.getResponseCode());
    }
}
