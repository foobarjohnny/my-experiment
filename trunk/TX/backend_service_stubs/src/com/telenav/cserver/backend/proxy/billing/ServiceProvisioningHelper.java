/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import com.telenav.billing2.common.dataTypes.ClientProfile;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.common.dataTypes.UserCredential;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.CheckPTNVerificationCodeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.GetAccountStatusRequest;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeRequest;
import com.telenav.cserver.backend.facade.billing.BillingConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ServiceProvisioningHelper
 * @author kwwang
 *
 */
public class ServiceProvisioningHelper
{
    public static final String ALL_SERVICES_APP_CODE = "all";
    
    public static final String NAVIGATION_APP_CODE ="Navigation";
    
    public static GetAccountStatusRequest newGetAccountStatusRequestWithAllServices(UserProfile user, TnContext tc)
    {
        GetAccountStatusRequest request = new GetAccountStatusRequest();
        request.setUserCredential(BillingHelper.newUserCredentialOfPtn(user, tc));
        ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(user, tc);
        profile.setAppCode(ALL_SERVICES_APP_CODE);
        //request.setShowHistory(NonEmptyConverter.toNonEmptyBoolean(false));
        request.setClientProfile(profile);
        return request;
    }

    public static GetAccountStatusRequest newGetAccountStatusRequestWithSpecificService(UserProfile user, TnContext tc, String appcode)
    {
        GetAccountStatusRequest request = new GetAccountStatusRequest();
        request.setUserCredential(BillingHelper.newUserCredentialOfPtn(user, tc));
        ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(user, tc);
        profile.setAppCode(appcode);
        request.setClientProfile(profile);
        return request;
    }
    
    
    public static AuthorizeRequest newAuthenticateRequest4NavigationLogin(UserProfile user, TnContext tc, String productFamily)
    {
        AuthorizeRequest request = new AuthorizeRequest();
        ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(user, tc);
        profile.setClientProductType(productFamily);
        profile.setAppCode(NAVIGATION_APP_CODE);
        UserCredential userCred = BillingHelper.newUserCredentialOfUserId(user, tc);
        if(userCred != null)
        	request.setUserCredential(userCred);
        request.setClientProfile(profile);
        
        Property deviceIdPro = new Property();
    	deviceIdPro.setKey(BillingConstants.DEVICE_ID);
    	deviceIdPro.setValue(user.getDeviceID());
    	request.addExtraProperty(deviceIdPro);
    	
    	Property macIdPro = new Property();
    	macIdPro.setKey(BillingConstants.MAC_ADDRESS);
    	macIdPro.setValue(user.getMacID());
    	request.addExtraProperty(macIdPro);
        
        return request;
    }
    
    public static SendPTNVerificationCodeRequest newSendPTNVerificationCodeRequest(UserProfile user, TnContext tc)
    {
        SendPTNVerificationCodeRequest request = new SendPTNVerificationCodeRequest();
        ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(user, tc);
        profile.setAppCode(NAVIGATION_APP_CODE);
        profile.setClientProductType(user.getProduct());
        request.setUserCredential(BillingHelper.newUserCredentialOfPtn(user, tc));
        request.setClientProfile(profile);
        return request;
    }
    
    public static CheckPTNVerificationCodeRequest newCheckPTNVerificationCodeRequest(UserProfile user, TnContext tc)
    {
        CheckPTNVerificationCodeRequest request = new CheckPTNVerificationCodeRequest();
        ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(user, tc);
        profile.setAppCode(NAVIGATION_APP_CODE);
        profile.setClientProductType(user.getProduct());
        request.setUserCredential(BillingHelper.newUserCredentialOfPtn(user, tc));
        request.setClientProfile(profile);
        return request;
    }
}
