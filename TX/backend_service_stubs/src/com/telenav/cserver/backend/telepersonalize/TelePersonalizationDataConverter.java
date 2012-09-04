/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import com.telenav.billing.ws.datatypes.common.CServerRequestSource;
import com.telenav.billing.ws.datatypes.common.CredentialType;
import com.telenav.billing.ws.datatypes.common.RequestSource;
import com.telenav.billing.ws.datatypes.common.UserCredential;
import com.telenav.billing.ws.datatypes.userprofile.Address;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfilePreferencesRequest;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfilePreferencesResponse;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfileRequest;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfileResponse;
import com.telenav.billing.ws.datatypes.userprofile.UpdateUserProfileRequest;
import com.telenav.billing.ws.datatypes.userprofile.UpdateUserProfileResponse;
import com.telenav.billing.ws.datatypes.userprofile.UserProfile;
import com.telenav.billing.ws.datatypes.userprofile.UserProfilePreferences;
import com.telenav.datatypes.xnav.v10.EmailConfirmationTemplateType;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.xnav.v10.EmailConfirmationRequestDTO;
import com.telenav.ws.datatypes.services.ResponseCodeEnum;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;
import com.telenav.xnav.user.UserTypeDef;


/**
 * TelePersonalizationDataConverter, provide the converting methods
 * @author kwwang
 * @date 2010-6-4
 */
public class TelePersonalizationDataConverter
{
    public static final String STATUS_SUCCESS_USERPROFILE = "0000";

    private static final String clientName = "personalization";

    private static final String clientVersion = "1.0";

    public static UserStatus toUserStatus(UserProfile userProfile)
    {
        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(userProfile.getUserId());
        userStatus.setCarrierCode(userProfile.getCarrierCode());
        userStatus.setCarrierId(userProfile.getCarrierId());
        userStatus.setFirstName(userProfile.getFirstName());
        userStatus.setMiddleName(userProfile.getMiddleName());
        userStatus.setLastName(userProfile.getLastName());
        userStatus.setNickname(userProfile.getNickname());
        userStatus.setDeviceId(userProfile.getDeviceId());
        userStatus.setContactEmail(userProfile.getContactEmail());
        userStatus.setCreateTime(userProfile.getCreateTime());
        userStatus.setUpdateTime(userProfile.getUpdateTime());
        userStatus.setEmailConfirmed(userProfile.getEmailConfirmed());
        userStatus.setHomeAddressId(userProfile.getHomeAddressId());
        userStatus.setWorkAddressId(userProfile.getWorkAddressId());
        if (userProfile.getUserCredentialProfile() != null)
        {
            userStatus.setLoginName(userProfile.getUserCredentialProfile().getPtn());
            userStatus.setPassword(userProfile.getUserCredentialProfile().getPassword());
        }
        return userStatus;
    }

    public static UserStatus toUserStatus(UserProfilePreferences userProfile)
    {
        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(userProfile.getUserId());
//        userStatus.setCarrierCode(userProfile.getCarrierCode());
//        userStatus.setCarrierId(userProfile.getCarrierId());
        userStatus.setFirstName(userProfile.getFirstName());
//        userStatus.setMiddleName(userProfile.getMiddleName());
        userStatus.setLastName(userProfile.getLastName());
//        userStatus.setNickname(userProfile.getNickname());
//        userStatus.setDeviceId(userProfile.getDeviceId());
        userStatus.setContactEmail(userProfile.getContactEmail());
//        userStatus.setCreateTime(userProfile.getCreateTime());
//        userStatus.setUpdateTime(userProfile.getUpdateTime());
//        userStatus.setEmailConfirmed(userProfile.getEmailConfirmed());
//        userStatus.setHomeAddressId(userProfile.getHomeAddressId());
//        userStatus.setWorkAddressId(userProfile.getWorkAddressId());
        userStatus.setHomeAddress(TelePersonalizationDataConverter.toDbAddress(userProfile.getHomeAddress()));
        userStatus.setWorkAddress(TelePersonalizationDataConverter.toDbAddress(userProfile.getWorkAddress()));
        userStatus.setSyncRecentStopsFavsFlag(userProfile.getIsAccountSyncEnabled());
//        if (userProfile.getUserCredentialProfile() != null)
//        {
//            userStatus.setLoginName(userProfile.getUserCredentialProfile().getPtn());
//            userStatus.setPassword(userProfile.getUserCredentialProfile().getPassword());
//        }
        return userStatus;
    }

    public static UserProfile toUserProfile(UserStatus userStatus)
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userStatus.getUserId());
        userProfile.setCarrierCode(userStatus.getCarrierCode());
        userProfile.setCarrierId(userStatus.getCarrierId());
        userProfile.setContactEmail(userStatus.getContactEmail());
        userProfile.setCreateTime(userStatus.getCreateTime());
        userProfile.setUpdateTime(userStatus.getUpdateTime());
        userProfile.setFirstName(userStatus.getFirstName());
        userProfile.setMiddleName(userStatus.getMiddleName());
        userProfile.setLastName(userStatus.getLastName());
        userProfile.setNickname(userStatus.getNickname());
        userProfile.setHomeAddressId(userStatus.getHomeAddressId());
        userProfile.setWorkAddressId(userStatus.getWorkAddressId());
        userProfile.setDeviceId(userStatus.getDeviceId());
        userProfile.setEmailConfirmed(userStatus.getEmailConfirmed());
        userProfile.setUserCredentialProfile(null);
        userProfile.setUserVehicleProfile(null);
        return userProfile;
    }

    public static GetUserProfileRequest toGetUserProfileRequest(UserProfileRequest request)
    {
        GetUserProfileRequest gpReq = new GetUserProfileRequest();
        gpReq.setRequestSource(RequestSource.TelenavCServer);
        UserCredential userCredential = new UserCredential();
        userCredential.setCredentialId(request.getCredentialId());
        userCredential.setCredentialType(getCredentialTypeBy(request.getCredentialType()));
        gpReq.setUserCredential(userCredential);
        return gpReq;
    }

    public static GetUserProfilePreferencesRequest toGetUserProfilePreferencesRequest(UserProfileRequest request)
    {
        GetUserProfilePreferencesRequest gpReq = new GetUserProfilePreferencesRequest();
        gpReq.setRequestSource(CServerRequestSource.TelenavCServer);
        UserCredential userCredential = new UserCredential();
        userCredential.setCredentialId(request.getCredentialId());
        userCredential.setCredentialType(getCredentialTypeBy(request.getCredentialType()));
        gpReq.setUserCredential(userCredential);
        return gpReq;
    }

    private static CredentialType getCredentialTypeBy(CSCredentialType cscType)
    {
        CredentialType credentialType = CredentialType.USER_ID;
        switch (cscType)
        {
            case CONTACT_EMAIL:
                credentialType = CredentialType.CONTACT_EMAIL;
                break;
            case PTN:
                credentialType = CredentialType.PTN;
                break;
            case VIN:
                credentialType = CredentialType.VIN;
                break;
            case THIRD_PARTY:
                credentialType = CredentialType.THIRD_PARTY;
                break;
            case USER_ID:
                credentialType = CredentialType.USER_ID;
                break;
            case EMAIL:
                credentialType = CredentialType.EMAIL;
                break;
        }
        return credentialType;
    }

    public static UserProfileResponse toUserProfileResponse(GetUserProfileResponse response)
    {
        UserProfileResponse upr = null;
        if (Axis2Helper.isNonEmptyArray(response.getUserProfile()))
        {
            upr = new UserProfileResponse();
            upr.setUserStatus(TelePersonalizationDataConverter.toUserStatus(response.getUserProfile()[0]));
        }
        return upr;
    }

    public static UserProfileResponse toUserProfilePreferencesResponse(GetUserProfilePreferencesResponse response)
    {
        UserProfileResponse upr = null;
        if (Axis2Helper.isNonEmptyArray(response.getUserProfilePreferences()))
        {
            upr = new UserProfileResponse();
            upr.setUserStatus(TelePersonalizationDataConverter.toUserStatus(response.getUserProfilePreferences()[0]));
        }
        return upr;
    }

    public static UpdateUserProfileRequest toUpdateUserProfileRequest(CSUpdateUserProfileRequest request)
    {
        UpdateUserProfileRequest uupReq = new UpdateUserProfileRequest();
        uupReq.setRequestSource(RequestSource.TelenavCServer);
        UserCredential userCredential = new UserCredential();
        userCredential.setCredentialId(request.getCredentialId());
        userCredential.setCredentialType(getCredentialTypeBy(request.getCredentialType()));
        uupReq.setUserCredential(userCredential);
        uupReq.setUserProfile(TelePersonalizationDataConverter.toUserProfile(request.getUserStatus()));
        return uupReq;
    }

    public static CSUpdateUserProfileResponse toCSUpdateProfileResponse(UpdateUserProfileResponse response)
    {
        CSUpdateUserProfileResponse csUpdateProfileRes = new CSUpdateUserProfileResponse();
        csUpdateProfileRes.setErrorMessage(response.getResponseMessage());
        csUpdateProfileRes.setSuccess(STATUS_SUCCESS_USERPROFILE.equalsIgnoreCase(response.getResponseCode()));
        return csUpdateProfileRes;
    }

    public static EmailConfirmationRequestDTO toEmailConfirmationRequestDTO(EmailConfirmationRequest request)
    {
        EmailConfirmationRequestDTO emailReqDto = new EmailConfirmationRequestDTO();
        emailReqDto.setClientName(clientName);
        emailReqDto.setClientVersion(clientVersion);
        emailReqDto.setTransactionId("");
        emailReqDto.setUserId(Long.parseLong(request.getUserId()));
        emailReqDto.setEmail(request.getEmail());
        emailReqDto.setContextString(request.getContextString());
        if (UserTypeDef.COMUTE_ALERT_EMAIL.equals(request.getEmailType()))
        {
            emailReqDto.setTemplateType(EmailConfirmationTemplateType.COMMUTE_ALERT);
        }
        else
        {
            emailReqDto.setTemplateType(EmailConfirmationTemplateType.COMMON);
        }
        return emailReqDto;
    }

    public static EmailConfirmationResponse toEmailConfirmationResponse(ServiceMgmtResponseDTO response)
    {
        EmailConfirmationResponse emailRes = new EmailConfirmationResponse();
        if (response.getResponseStatus() != null && ResponseCodeEnum._OK.equalsIgnoreCase(response.getResponseStatus().getStatusCode()))
        {
            emailRes.setSuccess(true);
        }
        emailRes.setErrorMessage(response.getResponseStatus().getStatusMessage());
        return emailRes;
    }

    public static Address toBillingAddress(com.televigation.db.address.Address address)
    {
        if (address == null)
        {
            return null;
        }
        else
        {
            com.telenav.billing.ws.datatypes.userprofile.Address wsAddress = new com.telenav.billing.ws.datatypes.userprofile.Address();
            wsAddress.setAddressId(address.getAddressId());
            wsAddress.setCity(address.getCity());
            wsAddress.setCountry(address.getCountry());
            wsAddress.setGeoCodingSource(address.getGeoCodingSource());
            wsAddress.setLat(address.getLat());
            wsAddress.setLon(address.getLon());
            wsAddress.setPobox(address.getPobox());
            wsAddress.setPostalCode(address.getPostalCode());
            wsAddress.setProvince(address.getProvince());
            wsAddress.setStreet(address.getStreet());
            wsAddress.setStreet2(address.getStreet2());
            wsAddress.setSuite(address.getSuite());
            return wsAddress;
        }
    }

    public static com.televigation.db.address.Address toDbAddress(com.telenav.billing.ws.datatypes.userprofile.Address address)
    {
        if (address == null)
        {
            return null;
        }
        else
        {
            // Notice: If data in database is null,billing returns
            // Long.MIN_VALUE for long
            // type,Integer.MIN_VALUE for int type,Double.NaN for double type.
            com.televigation.db.address.Address dbAddress = new com.televigation.db.address.Address();
            dbAddress.setAddressId(address.getAddressId() == Long.MIN_VALUE ? 0 : address.getAddressId());
            dbAddress.setCity(address.getCity() == null ? "" : address.getCity());
            dbAddress.setCountry(address.getCountry() == null ? "" : address.getCountry());
            dbAddress.setGeoCodingSource(address.getGeoCodingSource() == null ? "" : address.getGeoCodingSource());
            dbAddress.setLat((address.getLat() == Double.NaN) ? 0.000000 : address.getLat());
            dbAddress.setLon(address.getLon() == Double.NaN ? 0.000000 : address.getLon());
            dbAddress.setPobox(address.getPobox());
            dbAddress.setPostalCode(address.getPostalCode() == null ? "" : address.getPostalCode());
            dbAddress.setProvince(address.getProvince() == null ? "" : address.getProvince());
            dbAddress.setStreet(address.getStreet() == null ? "" : address.getStreet());
            dbAddress.setStreet2(address.getStreet2() == null ? "" : address.getStreet2());
            dbAddress.setFirst(dbAddress.getStreet() + dbAddress.getStreet2());
            dbAddress.setLast(dbAddress.getCity() + ", " + dbAddress.getProvince() + " " + dbAddress.getPostalCode());
            dbAddress.setSuite(address.getSuite() == null ? "" : address.getSuite());
            dbAddress.setFromId(com.televigation.db.address.Address.FROM_AB);
            return dbAddress;
        }
    }
}
