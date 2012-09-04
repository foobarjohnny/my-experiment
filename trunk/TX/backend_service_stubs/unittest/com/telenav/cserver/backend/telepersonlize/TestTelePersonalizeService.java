/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonlize;

import java.util.Calendar;

import junit.framework.TestCase;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.telepersonalize.CSCredentialType;
import com.telenav.cserver.backend.telepersonalize.CSUserTypeDef;
import com.telenav.cserver.backend.telepersonalize.EmailConfirmationRequest;
import com.telenav.cserver.backend.telepersonalize.EmailConfirmationResponse;
import com.telenav.cserver.backend.telepersonalize.TelepersonalizationFacade;
import com.telenav.cserver.backend.telepersonalize.TelepersonalizeProxy;
import com.telenav.cserver.backend.telepersonalize.UserProfileRequest;
import com.telenav.cserver.backend.telepersonalize.UserProfileResponse;
import com.telenav.cserver.backend.telepersonalize.UserStatus;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.xnav.client.telepersonalize.TelePersonalize;
import com.telenav.xnav.user.UserTypeDef;
import com.televigation.db.address.Address;

/**
 * TestTelePersonalizeService
 * 
 * @author kwwang
 * @date 2010-6-4
 */
public class TestTelePersonalizeService extends TestCase
{
    private TelepersonalizeProxy proxy;

    private TnContext tc;

    private String userID;

    @Override
    protected void setUp() throws Exception
    {
        proxy = TelepersonalizeProxy.getInstance();
        tc = new TnContext();
        tc.addProperty(TnContext.PROP_CARRIER, "Cingular");
        tc.addProperty(TnContext.PROP_DEVICE, "9630");
        tc.addProperty(TnContext.PROP_PRODUCT, "ATT_NAV");
        tc.addProperty(TnContext.PROP_VERSION, "6.0.01");
        // userID = "7251848";
        userID = "3698214"; //for us billing
        //userID = "4742694"; for cn billing
        // add this to test cli
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_URL);
        cli.addData(CliConstants.LABEL_CLIENT_INTO, "url=testTelepersonalization&min=8883330030");
    }

    public void testGetUserProfile() throws ThrottlingException
    {
        UserProfileRequest upr = new UserProfileRequest();
        upr.setCredentialType(CSCredentialType.USER_ID);
        upr.setCredentialId(userID);
        UserProfileResponse upRes = proxy.getUserProfile(upr, tc);
        assertNotNull(upRes);
        assertNotNull(upRes.getUserStatus());
        assertEquals(Long.parseLong(userID), upRes.getUserStatus().getUserId());

    }

    public void testUpdateUserProfile() throws Exception
    {
        // first get user profile
        UserStatus user = TelepersonalizationFacade.fetchUser(CSUserTypeDef.USERINFO_BASIC, userID, tc);
        String oldFirstName = user.getFirstName();
        String oldLastName = user.getLastName();
        String oldEmail = user.getContactEmail();

        // update user profile
        TelepersonalizationFacade.updateUserProfile(userID, "kevin_test", "wang_test", "kwwang@telenav.cn", -1, -1, true, tc);

        // get user profile
        UserProfileRequest upr = new UserProfileRequest();
        upr.setCredentialType(CSCredentialType.USER_ID);
        upr.setCredentialId(userID);
        UserProfileResponse upRes = proxy.getUserProfile(upr, tc);

        assertEquals("kevin_test".toUpperCase(), upRes.getUserStatus().getFirstName());
        assertEquals("wang_test".toUpperCase(), upRes.getUserStatus().getLastName());
        assertEquals("kwwang@telenav.cn", upRes.getUserStatus().getContactEmail());

        // roll back to old user info without sending email
        TelepersonalizationFacade.updateUserProfile(userID, oldFirstName, oldLastName, oldEmail, -1, -1, false, tc);

    }

    public void testSendEmail() throws Exception
    {
        EmailConfirmationRequest emailReq = new EmailConfirmationRequest();
        tc.addProperty(TnContext.PROP_CARRIER, "Cingular");
        tc.addProperty(TnContext.PROP_DEVICE, "9630");
        tc.addProperty(TnContext.PROP_PRODUCT, "ATT_NAV");
        tc.addProperty(TnContext.PROP_VERSION, "6.0.01");
        emailReq.setContextString(tc.toContextString());
        emailReq.setEmail("kwwang@telenav.cn");
        emailReq.setEmailType(UserTypeDef.COMMON_EMAIL);
        emailReq.setUserId(userID);

        EmailConfirmationResponse res = proxy.sendEmail(emailReq, tc);
        assertTrue(res.isSuccess());
    }

    public void testFetchUserAddress() throws Exception
    {
        // first set the address
        com.telenav.billing.ws.datatypes.userprofile.Address address = new com.telenav.billing.ws.datatypes.userprofile.Address();
        address.setAddressId(123456789);
        address.setCity("SUNNYVALE");
        address.setProvince("CA");
        address.setStreet("1130 KIFER RD");
        address.setPostalCode("94086");
        address.setCountry("US");
        address.setTimeZoneId(-2147483648);
        address.setLat(37.373919);
        address.setLon(-121.999303);
        address.setGeoCodingSource("NAVTECH");
        address.setCreateTime(Calendar.getInstance());
        address.setUpdateTime(Calendar.getInstance());
        long addrID = proxy.saveAddress(address, tc);

        // fetch address by id
        com.telenav.billing.ws.datatypes.userprofile.Address fetchAddr = proxy.getAddress(addrID, tc);
        assertNotNull(fetchAddr);
        assertEquals("SUNNYVALE", fetchAddr.getCity());
        assertEquals("CA", fetchAddr.getProvince());
        assertEquals("1130 KIFER RD", fetchAddr.getStreet());
        assertEquals("94086", fetchAddr.getPostalCode());
        assertEquals("US", fetchAddr.getCountry());

        // delete it
        boolean isDel = proxy.delAddress(addrID, tc);
        assertTrue(isDel);

    }

    public void testHomeWorkAddress() throws ThrottlingException
    {
        updateAddressBy(CSUserTypeDef.HOMEADDR);
        updateAddressBy(CSUserTypeDef.WORKADDR);
    }

    private void updateAddressBy(String addressType) throws ThrottlingException
    {
        Address addr = TelepersonalizationFacade.fetchUserAddress(addressType, userID, tc);

        // store oldAddr
        Address oldAddr = addr;

        // update to test address
        Address testAddr = createDBAddress();
        boolean isUpd = TelepersonalizationFacade.updateUserAddress(userID, addressType, testAddr, tc);
        assertTrue(isUpd);

        // check updated address
        Address updAddr = TelepersonalizationFacade.fetchUserAddress(addressType, userID, tc);
        assertEquals(testAddr.getCity(), updAddr.getCity());
        assertEquals(testAddr.getProvince(), updAddr.getProvince());
        assertEquals(testAddr.getStreet(), updAddr.getStreet());
        assertEquals(testAddr.getPostalCode(), updAddr.getPostalCode());
        assertEquals(testAddr.getCountry(), updAddr.getCountry());

        // rollback to original address
        assertTrue(TelepersonalizationFacade.updateUserAddress(userID, addressType, oldAddr, tc));

    }

    private Address createDBAddress()
    {
        Address testAddr = new Address();
        testAddr.setCity("SUNNYVALE_test");
        testAddr.setProvince("CA_test");
        testAddr.setStreet("1130 KIFER RD_test");
        testAddr.setPostalCode("94086_test");
        testAddr.setCountry("US_test");
        testAddr.setTimeZoneId(-2147483648);
        testAddr.setLat(37.373919);
        testAddr.setLon(-121.999303);
        testAddr.setGeoCodingSource("NAVTECH");
        return testAddr;
    }

    /**
     * This test case test the following flow:
     * 1.first updateUserProfile
     * 2.check emailconfirmed, it should be 0, since user doesn't confirmed the email yet
     * 3.updateUserProfile again, check emailconfirmed, it should be 0
     * 4.user confirmed email
     * 5.check emailconfirmed, it should be 1
     * 6.updateUserProfile,check emailconfirmed, it should be 1
     * @throws Exception
     */
//    public void testEmailConfirmed() throws Exception
//    {
//        // first set userprofile to test userprofile
//        String oldFirstName = "test_firstname";
//        String oldLastName = "test_lastname";
//        String oldEmail = "test_email";
//        TelepersonalizationFacade.updateUserProfile(userID, oldFirstName, oldLastName, oldEmail, -1, -1, true, tc);
//
//        // update user profile to new test userprofile
//        TelepersonalizationFacade.updateUserProfile(userID, "kevin_test", "wang_test", "kwwang@telenav.cn", -1, -1, true, tc);
//
//        // get user profile
//        UserProfileRequest upr = new UserProfileRequest();
//        upr.setCredentialType(CSCredentialType.USER_ID);
//        upr.setCredentialId(userID);
//        UserProfileResponse upRes = proxy.getUserProfile(upr, tc);
//        assertEquals(0, upRes.getUserStatus().getEmailConfirmed());
//        
//        // update user profile to new test userprofile
//        TelepersonalizationFacade.updateUserProfile(userID, "kevin_test", "wang_test", "kwwang@telenav.cn", -1, -1, true, tc);
//
//        // get user profile
//        upr = new UserProfileRequest();
//        upr.setCredentialType(CSCredentialType.USER_ID);
//        upr.setCredentialId(userID);
//        upRes = proxy.getUserProfile(upr, tc);
//        assertEquals(0, upRes.getUserStatus().getEmailConfirmed());
//        
//        // confirmed email
//        TelePersonalize telePersonalize = new TelePersonalize();
//        telePersonalize.sendConfirmEmail(userID, "kwwang@telenav.cn" , "");
//
//        // get user profile
//        upr = new UserProfileRequest();
//        upr.setCredentialType(CSCredentialType.USER_ID);
//        upr.setCredentialId(userID);
//        upRes = proxy.getUserProfile(upr, tc);
//
//        assertEquals(1, upRes.getUserStatus().getEmailConfirmed());
//
//        // update user profile to new test userprofile again to check emailconfirmed field
//        TelepersonalizationFacade.updateUserProfile(userID, "kevin_test", "wang_test", "kwwang@telenav.cn", -1, -1, true, tc);
//
//        // get userprofile to check emailconfirmed field
//        UserProfileResponse upRes1 = proxy.getUserProfile(upr, tc);
//        assertEquals(1, upRes1.getUserStatus().getEmailConfirmed());
//
//        // roll back to old user info without sending email
//        TelepersonalizationFacade.updateUserProfile(userID, oldFirstName, oldLastName, oldEmail, -1, -1, false, tc);
//    }
}
