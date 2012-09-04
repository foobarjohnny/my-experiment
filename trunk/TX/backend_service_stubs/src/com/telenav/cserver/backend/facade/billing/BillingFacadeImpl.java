/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationResponse;
import com.telenav.billing2.accountidentity.dataTypes.AuthenticateResponse;
import com.telenav.billing2.accountidentity.dataTypes.CreateAccountResponse;
import com.telenav.billing2.accountidentity.dataTypes.ExternalUserCredential;
import com.telenav.billing2.accountidentity.dataTypes.GetExternalUserCredentialResponse;
import com.telenav.billing2.common.dataTypes.CredentialType;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.common.dataTypes.UserCredential;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.GetUserProfilePreferencesResponse;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesRequest;
import com.telenav.billing2.userprofile2.userprofile.dataTypes.UpdateUserProfilePreferencesResponse;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeResponse;
import com.telenav.billing2.ws.datatypes.identity.service.SendPTNVerificationCodeResponse;
import com.telenav.cserver.backend.proxy.BackendProxyFactory;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.billing.AccountidentityHelper;
import com.telenav.cserver.backend.proxy.billing.AccountidentityProxy;
import com.telenav.cserver.backend.proxy.billing.ServiceProvisioningHelper;
import com.telenav.cserver.backend.proxy.billing.ServiceProvisioningProxy;
import com.telenav.cserver.backend.proxy.billing.UserProfileManagementHelper;
import com.telenav.cserver.backend.proxy.billing.UserProfileManagementProxy;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * BillingFacadeImpl
 *
 * @author kwwang
 *
 */
public class BillingFacadeImpl implements BillingFacade {
        private Logger logger = Logger.getLogger(BillingFacadeImpl.class);
        public final static String SUCCESS = "0000";
        public final static String DEVICE_ALREADY_BOUND = "4005";

        
        private void login(UserProfile user, TnContext tc,
                        FaceBookLoginResponse faceBookLogin) throws ThrottlingException {
                // remove old userId since this might be the userId of original user.
                user.setUserId("");
                
                // create AuthorizeRequest with deviceId
                AuthorizeRequest request = ServiceProvisioningHelper
                                .newAuthenticateRequest4NavigationLogin(user, tc,
                                                user.getProduct());
                Property prop = new Property();
                prop.setKey("DEVICE_ID");
                prop.setValue(user.getDeviceID());
                Property[] extraProp = new Property[1];
                extraProp[0] = prop;
                request.setExtraProperty(extraProp);

                AuthorizeResponse authorizeResp = BackendProxyManager
                                .getBackendProxyFactory()
                                .getBackendProxy(ServiceProvisioningProxy.class)
                                .authenticate(request, tc);

                if (authorizeResp != null)
                        faceBookLogin.setErrorCode(authorizeResp.getResponseCode());

                if (authorizeResp != null
                                && SUCCESS.equals(authorizeResp.getResponseCode())
                                && authorizeResp.getAccountInfo() != null) {
                        faceBookLogin.setErrorCode(SUCCESS);
                        faceBookLogin.setUserId(authorizeResp.getAccountInfo().getUserId()
                                        .getValue());
                }
        }

        void log(String message) {
                if (logger.isDebugEnabled()) {
                        logger.debug(message);
                }
        }
        
        private boolean isUserIdNotEqual(String oldUserId,String newUserId)
        {
                
                
                if(StringUtils.isBlank(oldUserId)||StringUtils.isBlank(newUserId))
                        return false;
                
                if(!oldUserId.equals(newUserId))
                        return true;
                else
                        return false;
        }
        
        private boolean isUserIdValid(String userId){
                long userIdLon = -1;
                try{
                        userIdLon = Long.parseLong(userId);
                }catch(NumberFormatException e){
                }
                return (userIdLon > 0)?true: false;
        }

        @Override
        public EmailLoginResponse emailLogin(EmailLoginRequest emailLoginRequest)
                        throws ThrottlingException {
                UserProfile user = emailLoginRequest.getUser();
                TnContext tc = emailLoginRequest.getTc();

                BackendProxyFactory proxyFactory = BackendProxyManager
                                .getBackendProxyFactory();

                AuthenticateResponse authenticateResp = proxyFactory.getBackendProxy(
                                AccountidentityProxy.class).authenticate(
                                AccountidentityHelper.createAuthenticateRequest(
                                                emailLoginRequest.getEmail(),
                                                emailLoginRequest.getPassword()), tc);

                EmailLoginResponse emailLoginResponse = new EmailLoginResponse();

                if (authenticateResp == null)
                        return emailLoginResponse;

                String authErrorCode = authenticateResp.getResponseCode();

                if (SUCCESS.equals(authErrorCode)
                                && authenticateResp.getAccount() != null) {

                        long userId = authenticateResp.getAccount().getUserId().getValue();
                        emailLoginResponse.setErrorCode(authErrorCode);
                        emailLoginResponse.setUserId(userId);
                        
                        if (BillingConstants.EMAIL_ACCOUNT_EXIST_WITHOUT_FAKE_PTN.equals(authenticateResp.getMinorCode()))
                        {
                        	logger.debug("Email account exist without fake ptn. Need authorize.");
                        	AuthorizeRequest request = ServiceProvisioningHelper
                        									.newAuthenticateRequest4NavigationLogin(user, tc, user.getProduct());
                        	UserCredential userCredential = request.getUserCredential();
                        	userCredential.setCredentialType(CredentialType.USER_ID);
                        	userCredential.setCredentialId(String.valueOf(userId));
                        	AuthorizeResponse authorizeResp = proxyFactory.getBackendProxy(ServiceProvisioningProxy.class)
                            									.authenticate(request, tc);
                        	
                        	if (authorizeResp != null && !SUCCESS.equals(authorizeResp.getResponseCode()))
                        	{
                        		emailLoginResponse.setErrorCode(authorizeResp.getResponseCode());
                        		return emailLoginResponse;
                        	}
                        }
                        
                        //do global association
                        if (isUserIdValid(user.getUserId()) && isUserIdNotEqual(user.getUserId(), String.valueOf(userId))) {
                                AddGlobalUserAssociationResponse addGlobalUserResp = proxyFactory
                                                .getBackendProxy(AccountidentityProxy.class)
                                                .addGlobalUserAssociation(
                                                                AccountidentityHelper
                                                                                .createAddGlobalUserAssociationRequest(
                                                                                                Long.valueOf(user.getUserId()),
                                                                                                userId),
                                                                tc);
                                
                                if(addGlobalUserResp!=null)
                                        emailLoginResponse.setErrorCode(addGlobalUserResp.getResponseCode());
                        }
                        
                        
                } else if (BillingConstants.EMAIL_ACCOUNT_NOT_FOUND.equals(authErrorCode) 
                		|| BillingConstants.EMAIL_ACCOUTN_PASSWD_MISSING.equals(authErrorCode)) {
                        
                        emailLoginResponse.setErrorCode(authErrorCode);
                        
                        if (StringUtils.isEmpty(user.getUserId())) {
                                AuthorizeResponse authorizeResp = proxyFactory
                                                .getBackendProxy(ServiceProvisioningProxy.class)
                                                .authenticate(
                                                                ServiceProvisioningHelper.newAuthenticateRequest4NavigationLogin(
                                                                                user, tc, user.getProduct()), tc);

                                if (authorizeResp != null
                                                && authorizeResp.getAccountInfo() != null)
                                        emailLoginResponse.setUserId(authorizeResp.getAccountInfo()
                                                        .getUserId().getValue());
                        } else {
                                emailLoginResponse.setUserId(Long.valueOf(user.getUserId()));
                        }
                } else {
                        emailLoginResponse.setErrorCode(authenticateResp.getResponseCode());
                }

                return emailLoginResponse;
        }
        
        
        @Override
        public DeviceLoginResponse deviceLogin(BillingFacadeCommonRequest authRequest)
                        throws ThrottlingException {
                UserProfile user = authRequest.getUser();
                TnContext tc = authRequest.getTc();

                BackendProxyFactory proxyFactory = BackendProxyManager
                                .getBackendProxyFactory();

                DeviceLoginResponse deviceLoginResponse = new DeviceLoginResponse();
                AuthorizeResponse authorizeResp = proxyFactory
                                .getBackendProxy(ServiceProvisioningProxy.class)
                                .authenticate(
                                                ServiceProvisioningHelper.newAuthenticateRequest4NavigationLogin(
                                                                user, tc, user.getProduct()), tc);

                if (authorizeResp != null ){
                        deviceLoginResponse.setErrorCode(authorizeResp.getResponseCode());
                        if( authorizeResp.getAccountInfo() != null && authorizeResp.getAccountInfo()
                                        .getUserId()!= null){
                                deviceLoginResponse.setUserId(authorizeResp.getAccountInfo()
                                        .getUserId().getValue());
                        }
                }
                return deviceLoginResponse;
        }

        @Override
        public FaceBookLoginResponse facebookLogin(FaceBookRequest faceBookRequest)
                        throws ThrottlingException {
                FaceBookLoginResponse faceBookLoginResponse = new FaceBookLoginResponse();

                if (StringUtils.isBlank(faceBookRequest.getUser().getUserId())) {
                        log("in facebookLogin, userId is null");
                        return faceBookLoginResponse;
                }

                GetExternalUserCredentialResponse resp = BackendProxyManager
                                .getBackendProxyFactory()
                                .getBackendProxy(AccountidentityProxy.class)
                                .getExternalUserCredential(
                                                AccountidentityHelper.createGetExternalUserCredentialRequest(Long
                                                                .parseLong(faceBookRequest.getUser()
                                                                                .getUserId())), faceBookRequest.getTc());

                if (SUCCESS.equals(resp.getResponseCode())
                                && resp.getExternalUserCredential() != null
                                && resp.getExternalUserCredential().length > 0) {

                        faceBookLoginResponse
                                        .setHasFaceBookAccountBound(hasFaceBookAccountBound(
                                                        resp.getExternalUserCredential(),
                                                        faceBookRequest.getToken()));
                        login(faceBookRequest.getUser(), faceBookRequest.getTc(),
                                        faceBookLoginResponse);
                }

                return faceBookLoginResponse;

        }

        private boolean hasFaceBookAccountBound(
                        ExternalUserCredential[] credentials, String token) {
                boolean hasFaceBookAccountBound = false;
                for (ExternalUserCredential credential : credentials) {
                        if (token.equals(credential.getToken().getValue())) {
                                hasFaceBookAccountBound = true;
                                break;
                        }
                }
                return hasFaceBookAccountBound;
        }

        @Override
        public ScountMeAccountBindResponse isScountMeAccountBound(
                        BillingFacadeCommonRequest scountMeAccountBindRequest)
                        throws ThrottlingException {

                GetUserProfilePreferencesResponse getUserProfPreResp = BackendProxyManager
                                .getBackendProxyFactory()
                                .getBackendProxy(UserProfileManagementProxy.class)
                                .getUserProfilePreferences(
                                                UserProfileManagementHelper.createGetUserProfilePreferencesRequestByUserId(scountMeAccountBindRequest
                                                                .getUser().getUserId()),
                                                scountMeAccountBindRequest.getTc());

                ScountMeAccountBindResponse resp = new ScountMeAccountBindResponse();

                if (getUserProfPreResp != null) {
                        resp.setErrorCode(getUserProfPreResp.getResponseCode());

                        if (getUserProfPreResp.getUserProfilePreferences() != null
                                        && getUserProfPreResp.getUserProfilePreferences().length > 0)
                                resp.setScountMeAccountBound(getUserProfPreResp
                                                .getUserProfilePreferences()[0]
                                                .getIsAccountSyncEnabled());
                }
                return resp;
        }

        @Override
        public com.telenav.cserver.backend.facade.billing.CreateAccountResponse createAccount(
                        CreateAccountRequest request) throws ThrottlingException {
                CreateAccountResponse createAccountResp = BackendProxyManager
                                .getBackendProxyFactory()
                                .getBackendProxy(AccountidentityProxy.class)
                                .createAccount(
                                                AccountidentityHelper.createCreateAccountRequest(
                                                                request.getEmail(), request.getPassword(), request.getUserId()),
                                                request.getTc());

                com.telenav.cserver.backend.facade.billing.CreateAccountResponse billingFacadeResp = new com.telenav.cserver.backend.facade.billing.CreateAccountResponse();
                if (createAccountResp != null) {

                        //if createAccont successful, or email account is already there, will go on updating firstName, lastName, note that even if email account is already there, we can still get the userId from createAccountResponse
                        if (SUCCESS.equals(createAccountResp.getResponseCode())) {
                                logger.debug("account creation response code: " + createAccountResp.getResponseCode());
                                logger.debug("account creation response userId: " + createAccountResp.getUserId());
                                
                                billingFacadeResp.setUserId(createAccountResp.getUserId());
                                UpdateUserProfilePreferencesRequest updReq = UserProfileManagementHelper
                                                .createUpdateUserProfilePreferencesRequest(
                                                                String.valueOf(createAccountResp.getUserId()),
                                                                request.getFirstName(), request.getLastName());

                                // update firstName and lastName
                                UpdateUserProfilePreferencesResponse updProfResp = BackendProxyManager
                                                .getBackendProxyFactory()
                                                .getBackendProxy(UserProfileManagementProxy.class)
                                                .updateUserProfilePreferences(updReq, request.getTc());

                                if (SUCCESS.equals(updProfResp.getResponseCode())){
                                        billingFacadeResp.setErrorCode(SUCCESS);
                                }else{
                                        billingFacadeResp.setErrorCode(updProfResp.getResponseCode());
                                }
                        }else{
                                billingFacadeResp.setErrorCode(createAccountResp.getResponseCode());
                        }

                }

                return billingFacadeResp;
        }

        @Override
        public BillingFacadeCommonResponse sendPTNVerificationCode(BillingFacadeCommonRequest request) throws ThrottlingException
        {
                BillingFacadeCommonResponse resp = new BillingFacadeCommonResponse();
                
                UserProfile userProfile = request.getUser();
                TnContext tc = request.getTc();
                
                SendPTNVerificationCodeResponse verificationResp = null;
                
                ServiceProvisioningProxy proxy = BackendProxyManager.getBackendProxyFactory()
                                                                                                                .getBackendProxy(ServiceProvisioningProxy.class);
                
                verificationResp = proxy.sendPTNVerificationCode(ServiceProvisioningHelper.newSendPTNVerificationCodeRequest(userProfile, tc), tc);

                if (verificationResp != null)
                {
                        resp.setErrorCode(verificationResp.getResponseCode());
                }
                
                return resp;
        }
}
 
