/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.xnav.user.UserTypeDef;
import com.televigation.db.address.Address;

/**
 * TelepersonalizationFacade, provide the convenient operations for telepersonalization service.
 * 
 * @author kwwang
 * @date 2010-6-4
 */
public class TelepersonalizationFacade
{
    final static Logger logger = Logger.getLogger(TelepersonalizationFacade.class);

    
    /**
     * 
     * @param ptn
     * @param tc
     * @return
     * @throws ThrottlingException
     */
    public static UserStatus getUserProfileViaPTN(String ptn, TnContext tc) throws ThrottlingException
    {
        UserProfileRequest upr = new UserProfileRequest();
        upr.setCredentialType(CSCredentialType.PTN);
        upr.setCredentialId(ptn);
        UserProfileResponse upRes = TelepersonalizeProxy.getInstance().getUserProfile(upr, tc);
        if (upRes == null)
            return null;
        return upRes.getUserStatus();
    }
    
    public static UserStatus getUserProfile(String userId, TnContext tc) throws ThrottlingException
    {
        long userID = Long.parseLong(userId);
        if (!checkUserId(userID))
        {
            return null;
        }
        
        UserProfileRequest upr = new UserProfileRequest();
        upr.setCredentialType(CSCredentialType.USER_ID);
        upr.setCredentialId(userId);
        UserProfileResponse upRes = TelepersonalizeProxy.getInstance().getUserProfile(upr, tc);
        if (upRes == null)
            return null;
        return upRes.getUserStatus();
    }

    public static UserStatus getUserProfilePreference(String userId, TnContext tc) throws ThrottlingException
    {
        long userID = Long.parseLong(userId);
        if (!checkUserId(userID))
        {
            return null;
        }
        
        UserProfileRequest upr = new UserProfileRequest();
        upr.setCredentialType(CSCredentialType.USER_ID);
        upr.setCredentialId(userId);
        UserProfileResponse upRes = TelepersonalizeProxy.getInstance().getUserProfilePreferences(upr, tc);
        if (upRes == null)
            return null;
        return upRes.getUserStatus();
    	
    }

    public static boolean updateUserAddress(String userId, String addressType, Address address, TnContext tc) throws ThrottlingException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("updateUserAddress, userID=" + userId + " addressType=" + addressType + " address=" + address);
        }

        long userID = Long.parseLong(userId);

        boolean updateSuccess = false;

        if (!checkUserId(userID))
        {
            return updateSuccess;
        }

        UserStatus userStatus = getUserProfile(userId, tc);

        com.telenav.billing.ws.datatypes.userprofile.Address oldAddress = TelepersonalizeProxy.getInstance().getAddress(
            getAddressIdBy(addressType, userStatus), tc);

        long addressId = -1;
        com.telenav.billing.ws.datatypes.userprofile.Address wsAddress = TelePersonalizationDataConverter.toBillingAddress(address);
        if (oldAddress != null && address == null)
        {
            TelepersonalizeProxy.getInstance().delAddress(oldAddress.getAddressId(), tc);
        }
        else if (oldAddress == null && address != null)
        {
            wsAddress.setTimeZoneId(Integer.MIN_VALUE);
            wsAddress.setValidInd(null);
            addressId = TelepersonalizeProxy.getInstance().saveAddress(wsAddress, tc);
        }
        else if (oldAddress != null && address != null)
        {
            addressId = oldAddress.getAddressId();
            wsAddress.setAddressId(oldAddress.getAddressId());
            wsAddress.setValidInd("");
            TelepersonalizeProxy.getInstance().updateAddress(wsAddress, tc);
        }

        // update address id
		//the following properties isn't changed, so we set the default values and billing willn't change them. verify by zhiyong.
        UserStatus newUser = new UserStatus();
        newUser.setCarrierCode(Integer.MIN_VALUE);
        newUser.setCarrierId(Long.MIN_VALUE);
        newUser.setContactEmail(null);
        newUser.setDeviceId(Long.MIN_VALUE);
        newUser.setEmailConfirmed(Integer.MIN_VALUE);
        newUser.setFirstName(null);
        newUser.setLastName(null);
        newUser.setMiddleName(null);
        newUser.setNickname(null);
        newUser.setUserId(userID);

        if (UserTypeDef.HOMEADDR.equals(addressType))
        {
            newUser.setHomeAddressId(addressId);
            newUser.setWorkAddressId(userStatus.getWorkAddressId());
        }
        else
        {
            newUser.setWorkAddressId(addressId);
            newUser.setHomeAddressId(userStatus.getHomeAddressId());
        }

        CSUpdateUserProfileRequest updateUserReq = new CSUpdateUserProfileRequest();
        updateUserReq.setCredentialId(userId);
        updateUserReq.setCredentialType(CSCredentialType.USER_ID);
        updateUserReq.setUserStatus(newUser);
        CSUpdateUserProfileResponse updateUserRes = TelepersonalizeProxy.getInstance().updateUserProfile(updateUserReq, tc);

        if (updateUserRes.isSuccess())
        {
            updateSuccess = true;
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("updateUserAddress#isSuccess:" + updateSuccess);
        }

        return updateSuccess;
    }

    public static CSUpdateUserProfileResponse updateUserProfile(String strUserId, String firstName, String lastName, String email,
            long carrierId, long deviceId, boolean isSendEmail, TnContext tc) throws ThrottlingException
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("updateUserProfile:strUserId=" + strUserId + " firstName=" + firstName + " lastName=" + lastName + " email="
                    + email + " carrierId=" + carrierId + " deviceId=" + deviceId + " isSendEmail=" + isSendEmail + " tc=" + tc);
        }

        CSUpdateUserProfileResponse res = new CSUpdateUserProfileResponse();

        long userId = Long.parseLong(strUserId);
        if (!checkUserId(userId))
        {
            return res;
        }

        try
        {
            UserProfileRequest getUserProfileReq = new UserProfileRequest();
            getUserProfileReq.setCredentialType(CSCredentialType.USER_ID);
            getUserProfileReq.setCredentialId(strUserId);
            UserProfileResponse getUserProfileRes = TelepersonalizeProxy.getInstance().getUserProfile(getUserProfileReq, tc);

            if (getUserProfileRes != null && getUserProfileRes.getUserStatus() != null)
            {
                UserStatus userStatus = new UserStatus();

                boolean needUpdate = false;

                userStatus.setEmailConfirmed(getUserProfileRes.getUserStatus().getEmailConfirmed());
                
                // set email
                String originalEmail = getUserProfileRes.getUserStatus().getContactEmail();
                if ((email != null && !"NULL".equalsIgnoreCase(email) && email.equalsIgnoreCase(originalEmail))
                        || (email == null || email.trim().length() == 0))
                {
                    userStatus.setContactEmail(null);
                }
                else if ("NULL".equalsIgnoreCase(email))
                {
                    userStatus.setContactEmail("");
                }
                else if (email != null && email.trim().length() > 0)
                {
                    needUpdate = true;
                    userStatus.setContactEmail(email.toLowerCase());
                }

                // set email comfirmed
                boolean sendMail = checkIfNeed2SendMail(isSendEmail, email, originalEmail);
                if (sendMail && email != null && email.trim().length() > 0)
                {
                    needUpdate = true;
                    userStatus.setEmailConfirmed(0);
                }
                

                // set firstname
                if (firstName != null && "$NULL$".equals(firstName))
                {
                    userStatus.setFirstName("");
                    needUpdate = true;
                }
                else if (firstName != null && firstName.trim().length() > 0)
                {
                    userStatus.setFirstName(firstName);
                    needUpdate = true;
                }
                else
                {
                    userStatus.setFirstName(null);
                }

                // set lastname
                if (lastName != null && "$NULL$".equals(lastName))
                {
                    userStatus.setLastName("");
                    needUpdate = true;
                }
                else if (lastName != null && lastName.trim().length() > 0)
                {
                    userStatus.setLastName(lastName);
                    needUpdate = true;
                }
                else
                {
                    userStatus.setLastName(null);
                }

                // set carrierid
                if (carrierId > 0)
                {
                    needUpdate = true;
                    userStatus.setCarrierId(carrierId);
                }
                else
                {
                    userStatus.setCarrierId(Long.MIN_VALUE);
                }

                // set deviceId
                if (deviceId > 0)
                {
                    needUpdate = true;
                    userStatus.setDeviceId(deviceId);
                }
                else
                {
                    userStatus.setDeviceId(Long.MIN_VALUE);
                }

                // set common fields
                userStatus.setCarrierCode(Integer.MIN_VALUE);
                userStatus.setHomeAddressId(Long.MIN_VALUE);
                userStatus.setWorkAddressId(Long.MIN_VALUE);
                userStatus.setMiddleName(null);
                userStatus.setNickname(null);
                userStatus.setUserCredentialProfile(null);
                userStatus.setUserVehicleProfile(null);

                if (needUpdate)
                {
                    // update profile
                    CSUpdateUserProfileRequest csUpdReq = new CSUpdateUserProfileRequest();
                    csUpdReq.setCredentialId(strUserId);
                    csUpdReq.setCredentialType(CSCredentialType.USER_ID);
                    csUpdReq.setUserStatus(userStatus);
                    res = TelepersonalizeProxy.getInstance().updateUserProfile(csUpdReq, tc);

                    if (res != null && res.isSuccess() && sendMail)
                    {

                        if (logger.isDebugEnabled())
                        {
                            logger.debug("updateUserProfile successfully, send email");
                        }

                        // send mail
                        EmailConfirmationRequest emailReq = new EmailConfirmationRequest();
                        emailReq.setContextString(tc.toContextString());
                        emailReq.setEmail(email);
                        emailReq.setEmailType(UserTypeDef.COMMON_EMAIL);
                        emailReq.setUserId(strUserId);

                        TelepersonalizeProxy.getInstance().sendEmail(emailReq, tc);
                    }
                }
            }

        }
        catch (ThrottlingException ex)
        {
            throw ex;
        }
        catch (Exception e)
        {
            logger.fatal("TelepersonalizationFacade#updateUserProfile(strUserId,firstName,lastName)", e);
        }

        return res;
    }

    /**
     * Check if the userId is valid
     * 
     * @author kwwang
     * @date 2010-5-27
     * @param userId
     * @return
     */
    private static boolean checkUserId(long userId)
    {
        boolean isValid = true;
        if (userId == Long.MIN_VALUE)
        {
            isValid = false;
        }
        if (userId <= 0)
        {

            isValid = false;
        }

        if (!isValid && logger.isDebugEnabled())
        {
            logger.debug("userId is not valid," + userId);
        }

        return isValid;
    }

    /**
     * Check does it need to send email.
     * 
     * @author kwwang
     * @date 2010-5-27
     * @param inputIsSendmail, it is from the caller, if it is set to false, no need to send email.
     * @param email, the new email address
     * @param originalEmail, the old email address stored in xnav db.
     * @return
     */
    private static boolean checkIfNeed2SendMail(boolean inputIsSendmail, String email, String originalEmail)
    {
        boolean isSendEmail = true;
        if (!inputIsSendmail)
            return inputIsSendmail;

        if (email != null && !"NULL".equalsIgnoreCase(email) && email.equalsIgnoreCase(originalEmail))
        {
            isSendEmail = false;
        }
        else if (email == null || email.trim().length() == 0)
        {
            isSendEmail = false;
        }
        else if ("NULL".equalsIgnoreCase(email))
        {
            isSendEmail = false;
        }

        if (isSendEmail && logger.isDebugEnabled())
        {
            logger.debug("need to send eamil.");
        }

        return isSendEmail;
    }

    private static long getAddressIdBy(String addressType, UserStatus userStatus)
    {
        if (UserTypeDef.HOMEADDR.equals(addressType))
        {
            return userStatus.getHomeAddressId();
        }
        else
        {
            return userStatus.getWorkAddressId();
        }
    }

    public static UserStatus fetchUserPreferences(String userId, TnContext tc) throws ThrottlingException
    {
        long userID = Long.parseLong(userId);
        UserStatus userStatus = null;
        if (!checkUserId(userID))
        {
            return userStatus;
        }
        return getUserProfilePreference(userId, tc);
    	
    }

    public static UserStatus fetchUser(String type, String userId, TnContext tc) throws ThrottlingException
    {
        long userID = Long.parseLong(userId);
        UserStatus userStatus = null;
        if (!checkUserId(userID))
        {
            return userStatus;
        }

        if (CSUserTypeDef.LOGIN.equalsIgnoreCase(type) || CSUserTypeDef.USERINFO_BASIC.equalsIgnoreCase(type))
        {
            userStatus = getUserProfile(userId, tc);
        }
        else if (CSUserTypeDef.USERINFO.equalsIgnoreCase(type))
        {
            userStatus = getUserProfile(userId, tc);
            if (userStatus != null)
            {
                userStatus.setHomeAddress(fetchUserAddress(CSUserTypeDef.HOMEADDR, userId, tc));
                userStatus.setWorkAddress(fetchUserAddress(CSUserTypeDef.WORKADDR, userId, tc));
            }
        }

        return userStatus;
    }

    public static Address fetchUserAddress(String addressType, String userId, TnContext tc) throws ThrottlingException
    {

        long userID = Long.parseLong(userId);
        if (!checkUserId(userID))
        {
            return null;
        }
        UserStatus userStatus = getUserProfile(userId, tc);

        long addressId = -1;
        
        if (CSUserTypeDef.HOMEADDR.equalsIgnoreCase(addressType) && null != userStatus)
        {
            addressId = userStatus.getHomeAddressId();
        }
        else if (CSUserTypeDef.WORKADDR.equalsIgnoreCase(addressType) && null != userStatus)
        {
            addressId = userStatus.getWorkAddressId();
        }

        com.telenav.billing.ws.datatypes.userprofile.Address addr = TelepersonalizeProxy.getInstance().getAddress(addressId, tc);

        return TelePersonalizationDataConverter.toDbAddress(addr);
    }

    public static Address fetchUserAddress(long addressId, TnContext tc) throws ThrottlingException {
        com.telenav.billing.ws.datatypes.userprofile.Address addr = TelepersonalizeProxy.getInstance().getAddress(addressId, tc);
        return TelePersonalizationDataConverter.toDbAddress(addr);
    }
}
