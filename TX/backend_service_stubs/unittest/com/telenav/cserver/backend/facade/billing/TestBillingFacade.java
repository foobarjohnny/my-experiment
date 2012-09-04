package com.telenav.cserver.backend.facade.billing;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.ws.datatypes.identity.service.AuthorizeRequest;
import com.telenav.cserver.backend.facade.FacadeManager;
import com.telenav.cserver.backend.proxy.billing.ServiceProvisioningHelper;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Test Billing Facade implementation.
 * Email login -- scout.me account authentication.
 * @author jianyuz
 *
 */
public class TestBillingFacade {
	
	private Logger logger = Logger.getLogger(TestBillingFacade.class);
	private TnContext tc;
	private UserProfile user;
	
	@SuppressWarnings("deprecation")
	@Before
	public void init()
	{
		tc = new TnContext();
		
		user = new UserProfile();
		user.setPlatform("IPHONE");
		user.setDevice("default");
		user.setProgramCode("SCOUTPROG");
		user.setVersion("7.1.0");
//		user.setUserId("16092933");
		//user.setMin("7418529630");
		user.setDeviceCarrier("ATT");
        user.setDeviceID("AAADFZCMsBPKQBAAyhej7q9ZALOePU7SdX16AH6AJ2dZBrZAhJhKqqesvZB4HEfrAJZCB1tfcnvIe6ewF1TjOOvfutJtz7CeoJyO7nRguOhd4ag33zZA7F5f");
        user.setMacID("AAADFZCMsBPKQBAAyhej7q9ZALOePU7SdX16AH6AJ2dZBrZAhJhKqqesvZB4HEfrAJZCB1tfcnvIe6ewF1TjOOvfutJtz7CeoJyO7nRguOhd4ag33zZA7F5f");


	}
	
//	//Test creating device account
//	@Test
//	public void testDeviceLogin_Success() throws Exception {
//		
//		BillingFacadeCommonRequest authRequest = new BillingFacadeCommonRequest();
//		authRequest.setUser(user);
//		authRequest.setTc(tc);
//
//		DeviceLoginResponse authResponse = FacadeManager
//				.getBillingFacade().deviceLogin(authRequest);
//		
//		Assert.assertNotNull(authResponse);
//		logger.debug("authResponse " + authResponse.toString());
//		logger.debug("authResponse errorCode " + authResponse.getErrorCode());
//		Assert.assertEquals("0000", authResponse.getErrorCode());
//		logger.debug("authResponse userId " + authResponse.getUserId());
//		
//	}
	
	@Test
	public void testEmailLogin_Success() throws Exception {

		EmailLoginRequest emailLoginRequest = new EmailLoginRequest();
		emailLoginRequest.setEmail("jianyuz@telenav.com");
		emailLoginRequest.setPassword("2345");
		emailLoginRequest.setUser(user);
		emailLoginRequest.setTc(tc);

		EmailLoginResponse facadeResponse = FacadeManager
				.getBillingFacade().emailLogin(emailLoginRequest);
		
		Assert.assertNotNull(facadeResponse);
		logger.debug("facadeResponse " + facadeResponse.toString());
		logger.debug("facadeResponse errorCode " + facadeResponse.getErrorCode());
		Assert.assertEquals("0000", facadeResponse.getErrorCode());
		logger.debug("facadeResponse userId " + facadeResponse.getUserId());
		Assert.assertTrue( facadeResponse.getUserId() > 0);
	}

	@Test
	public void testEmailLogin_MissingPasswd() throws Exception{
		EmailLoginRequest emailLoginRequest = new EmailLoginRequest();
		emailLoginRequest.setEmail("jianyuz@telenav.com");
		emailLoginRequest.setPassword("");
		emailLoginRequest.setUser(user);
		emailLoginRequest.setTc(tc);

		EmailLoginResponse facadeResponse = FacadeManager
				.getBillingFacade().emailLogin(emailLoginRequest);
		
		Assert.assertNotNull(facadeResponse);
		logger.debug("facadeResponse " + facadeResponse.toString());
		logger.debug("facadeResponse errorCode " + facadeResponse.getErrorCode());
		Assert.assertEquals(BillingConstants.EMAIL_ACCOUTN_PASSWD_MISSING, facadeResponse.getErrorCode());
		logger.debug("facadeResponse userId " + facadeResponse.getUserId());
		Assert.assertEquals(-1, facadeResponse.getUserId());
	}
	
	/**
	 * email login without user id in user profile.
	 * The call create a default device account if no account associated with the email is found.
	 * @throws Exception
	 */
	@Test	
	public void testEmailLogin_withoutUserID_AccountNotFound() throws Exception{
		EmailLoginRequest emailLoginRequest = new EmailLoginRequest();
		emailLoginRequest.setEmail("notExist@telenav.com");
		emailLoginRequest.setPassword("");
		emailLoginRequest.setUser(user);
		emailLoginRequest.setTc(tc);

		EmailLoginResponse facadeResponse = FacadeManager
				.getBillingFacade().emailLogin(emailLoginRequest);
		
		Assert.assertNotNull(facadeResponse);
		logger.debug("facadeResponse " + facadeResponse.toString());
		logger.debug("facadeResponse errorCode " + facadeResponse.getErrorCode());
		Assert.assertEquals(BillingConstants.EMAIL_ACCOUNT_NOT_FOUND, facadeResponse.getErrorCode());
		logger.debug("facadeResponse userId " + facadeResponse.getUserId());
		Assert.assertTrue(facadeResponse.getUserId()>0);
	}
	
	@Test	
	public void testCreateAccount_Success() throws Exception{

		CreateAccountRequest accountRequest = new CreateAccountRequest();
		
		long randLong = Math.round(Math.random() * 1000000000);
		String email = "jianyuz" + randLong + "@telenav.com";
		String password = "2345";
		
		accountRequest.setEmail(email);
		accountRequest.setPassword(password);
		accountRequest.setFirstName("jack");
		accountRequest.setLastName("zhou");
		accountRequest.setUser(user);
		accountRequest.setTc(tc);
		

		com.telenav.cserver.backend.facade.billing.CreateAccountResponse response = FacadeManager
				.getBillingFacade().createAccount(accountRequest);
		
		Assert.assertNotNull(response);
		logger.debug("createAccountResponse " + response.toString());
		logger.debug("createAccountResponse errorCode " + response.getErrorCode());
		Assert.assertEquals("0000", response.getErrorCode());
		logger.debug("createAccountResponse userId " + response.getUserId());
		Assert.assertTrue(response.getUserId()> 0);
	}
	
	
	/**
	 * Email login with user id in user profile.
	 * won't create new device account if account not found.
	 * @throws Exception
	 */
	@Test	
	public void testEmailLogin_withUserID_AccountNotFound() throws Exception{
		user.setUserId("16092933");
		EmailLoginRequest emailLoginRequest = new EmailLoginRequest();
		emailLoginRequest.setEmail("notExist@telenav.com");
		emailLoginRequest.setPassword("");
		emailLoginRequest.setUser(user);
		emailLoginRequest.setTc(tc);
		

		EmailLoginResponse facadeResponse = FacadeManager
				.getBillingFacade().emailLogin(emailLoginRequest);
		
		Assert.assertNotNull(facadeResponse);
		logger.debug("facadeResponse " + facadeResponse.toString());
		logger.debug("facadeResponse errorCode " + facadeResponse.getErrorCode());
		Assert.assertEquals(BillingConstants.EMAIL_ACCOUNT_NOT_FOUND, facadeResponse.getErrorCode());
		logger.debug("facadeResponse userId " + facadeResponse.getUserId());
		Assert.assertEquals(facadeResponse.getUserId()+"", user.getUserId());
	}
	
	

}
