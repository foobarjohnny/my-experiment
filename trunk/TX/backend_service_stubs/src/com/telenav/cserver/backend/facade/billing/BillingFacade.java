/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

import com.telenav.cserver.framework.throttling.ThrottlingException;

/**
 * BillingFacade
 * @author kwwang
 *
 */
public interface BillingFacade {

        EmailLoginResponse emailLogin(EmailLoginRequest emailLoginRequest)
                        throws ThrottlingException;

        FaceBookLoginResponse facebookLogin(FaceBookRequest faceBookRequest) throws ThrottlingException;
        
        ScountMeAccountBindResponse isScountMeAccountBound(BillingFacadeCommonRequest scoutMeBindRequest) throws ThrottlingException;
        
        
        com.telenav.cserver.backend.facade.billing.CreateAccountResponse createAccount(CreateAccountRequest request) throws ThrottlingException;
        
        DeviceLoginResponse deviceLogin(BillingFacadeCommonRequest authRequest) throws ThrottlingException;
        
        BillingFacadeCommonResponse sendPTNVerificationCode(BillingFacadeCommonRequest request) throws ThrottlingException;
        
}