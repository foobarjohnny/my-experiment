/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationRequest;
import com.telenav.billing2.accountidentity.dataTypes.AddGlobalUserAssociationResponse;
import com.telenav.billing2.accountidentity.dataTypes.AuthenticateResponse;
import com.telenav.billing2.accountidentity.dataTypes.ConfirmAccountResponse;
import com.telenav.billing2.accountidentity.dataTypes.CreateAccountResponse;
import com.telenav.billing2.accountidentity.dataTypes.GetExternalUserCredentialResponse;
import com.telenav.billing2.accountidentity.dataTypes.GetPasswordResponse;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * AccountidentityProxyTest
 * @author kwwang
 *
 */
public class TestAccountidentityProxy {
	private Logger logger = Logger.getLogger(TestAccountidentityProxy.class);
	private AccountidentityProxy proxy;
	private ServiceProvisioningProxy provisionProxy;
	private TnContext tc;
	private UserProfile user;

	@Before
	public void init() {
		proxy = new AccountidentityProxy();
		provisionProxy = new ServiceProvisioningProxy();
		tc = new TnContext();
		user = new UserProfile();
        user.setPlatform("IPHONE");
        user.setDevice("generic");
        user.setProgramCode("SCOUTPROG");
        user.setVersion("7.1.0");
        user.setUserId("16092933");
        user.setMin("7418529630");
        user.setDeviceCarrier("ATT");
        user.setDeviceID("AAADFZCMsBPKQBAAyhej7q9ZALOePU7SdX16AH6AJ2dZBrZAhJhKqqesvZB4HEfrAJZCB1tfcnvIe6ewF1TjOOvfutJtz7CeoJyO7nRguOhd4ag33zZA7F5f");
        user.setMacID("AAADFZCMsBPKQBAAyhej7q9ZALOePU7SdX16AH6AJ2dZBrZAhJhKqqesvZB4HEfrAJZCB1tfcnvIe6ewF1TjOOvfutJtz7CeoJyO7nRguOhd4ag33zZA7F5f");

	}
	

	@Test
	public void testaddGlobalUserAssociation_Success() throws Exception {
		AuthorizeRequest request = ServiceProvisioningHelper.newAuthenticateRequest4NavigationLogin(user, tc, "");
        request.setResetEqpin(NonEmptyConverter.toNonEmptyBoolean(false));
        AuthorizeResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ServiceProvisioningProxy.class)
                .authenticate(request, tc);
        Assert.assertEquals("0000", response.getResponseCode());
        long newUserId = response.getAccountInfo().getUserId().getValue();
        logger.debug("User id of account created: " + newUserId);
        //9965673
        long globalUserId = 1031759747;
        AddGlobalUserAssociationRequest assRequest = AccountidentityHelper.createAddGlobalUserAssociationRequest(newUserId, globalUserId);
        
        AddGlobalUserAssociationResponse assResponse = proxy.addGlobalUserAssociation(assRequest, tc);
        Assert.assertEquals("0000", assResponse.getResponseCode());
	}

	@Test
	public void testAuthenticate_withRightEmailPassword() {
		AuthenticateResponse resp = proxy.authenticate(
				AccountidentityHelper.createAuthenticateRequest(
						"wrongemail@telenav.cn", "wrongpwd"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals(resp.getResponseCode(), "3301");
	}

	@Test
	public void testGetExternalUserCredential() {
		GetExternalUserCredentialResponse resp = proxy
				.getExternalUserCredential(AccountidentityHelper
						.createGetExternalUserCredentialRequest(12345678), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals(resp.getResponseCode(), "3301");
	}
	
	@Test
	public void testCreateAccount_Success_withUserID() throws Exception{
		AuthorizeRequest request = ServiceProvisioningHelper.newAuthenticateRequest4NavigationLogin(user, tc, "");
        request.setResetEqpin(NonEmptyConverter.toNonEmptyBoolean(false));
        AuthorizeResponse response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ServiceProvisioningProxy.class)
                .authenticate(request, tc);
        Assert.assertEquals("0000", response.getResponseCode());
        long newUserId = response.getAccountInfo().getUserId().getValue();
        logger.info("User id of account created: " + newUserId);
        
        long randLong = Math.round(Math.random()*1000000000);
		String email="jianyuz" + randLong + "@telenav.com";
		String password = "2345";
		 
		
		CreateAccountResponse resp = proxy
				.createAccount(AccountidentityHelper
						.createCreateAccountRequest(email, password, newUserId), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals( "0000", resp.getResponseCode());
		Assert.assertTrue(resp.getToken()== null);
		Assert.assertTrue(resp.getUserId() != -1);
		System.out.println("User ID: " + resp.getUserId());
		logger.info("User ID: " + resp.getUserId());
		Assert.assertEquals(newUserId, resp.getUserId());
	}
	
	@Test
	public void testCreateAccount_Success() {
		long randLong = Math.round(Math.random() * 1000000000);
		String email = "jianyuz" + randLong + "@telenav.com";
		String password = "2345";
		
		CreateAccountResponse resp = proxy
				.createAccount(AccountidentityHelper
						.createCreateAccountRequest(email, password, -1), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals( "0000", resp.getResponseCode());
		Assert.assertTrue(resp.getToken()== null);
		Assert.assertTrue(resp.getUserId() != -1);
		System.out.println("User ID: " + resp.getUserId());
		logger.info("User ID: " + resp.getUserId());
	}
	
	@Test
	public void testCreateAccount_duplicate() {
		String email="jianyuz@telenav.com";
		String password = "2345";
		
		CreateAccountResponse resp = proxy
				.createAccount(AccountidentityHelper
						.createCreateAccountRequest(email, password, -1), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals( "3302", resp.getResponseCode());
	}
	
	@Test
	public void testGetPassword_Success() {
		String email="jianyuz@telenav.com";
		GetPasswordResponse resp = proxy
				.getPassword(AccountidentityHelper
						.createGetPasswordRequest(email), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals( "0000", resp.getResponseCode());
		Assert.assertEquals( "2345", resp.getPassword());
	}
	
	@Test
	public void testGetPassword_NotFound() {
		String email="NotExist@telenav.com";
		GetPasswordResponse resp = proxy
				.getPassword(AccountidentityHelper
						.createGetPasswordRequest(email), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("3301", resp.getResponseCode());
	}

}
