/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing.ws.datatypes.common.RequestSource;
import com.telenav.billing.ws.datatypes.userprofile.CreateAddressRequest;
import com.telenav.billing.ws.datatypes.userprofile.CreateAddressResponse;
import com.telenav.billing.ws.datatypes.userprofile.DeleteAddressRequest;
import com.telenav.billing.ws.datatypes.userprofile.DeleteAddressResponse;
import com.telenav.billing.ws.datatypes.userprofile.GetAddressRequest;
import com.telenav.billing.ws.datatypes.userprofile.GetAddressResponse;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfilePreferencesResponse;
import com.telenav.billing.ws.datatypes.userprofile.GetUserProfileResponse;
import com.telenav.billing.ws.datatypes.userprofile.UpdateAddressRequest;
import com.telenav.billing.ws.datatypes.userprofile.UpdateAddressResponse;
import com.telenav.billing.ws.datatypes.userprofile.UpdateUserProfileResponse;
import com.telenav.billing.ws.userprofilemanagementservice.UserProfileManagementServiceStub;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceManager;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.xnav.v10.XnavServiceStub;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

/**
 * TelepersonalizeProxy
 * 
 * @author kwwang
 * @date 2010-6-4
 */
public class TelepersonalizeProxy
{
    final static Logger logger = Logger.getLogger(TelepersonalizeProxy.class);

    private static final String TELEPERSONALIZATION_SERVICE = "TELEPERSONALIZATION_SERVICE";

    private static TelepersonalizeProxy instance = new TelepersonalizeProxy();

    private static WebServiceConfigInterface userProfileWs = WebServiceConfiguration.getInstance().getServiceConfig("TELEPERSONALIZATION");

    private static WebServiceConfigInterface emailWs = WebServiceConfiguration.getInstance().getServiceConfig("EMAILSERVICE");

    private static ConfigurationContext userProfileCinfigContext;

    private static ConfigurationContext emailCinfigContext;

    static
    {
        try
        {
            userProfileCinfigContext = WebServiceManager.createNewContext(userProfileWs.getWebServiceItem());
            emailCinfigContext = WebServiceManager.createNewContext(emailWs.getWebServiceItem());
        }
        catch (AxisFault e)
        {
            logger.fatal("TelepersonalizeProxy#cinit", e);
        }
    }

    private TelepersonalizeProxy()
    {
    }

    public static TelepersonalizeProxy getInstance()
    {
        return instance;
    }

    protected UserProfileManagementServiceStub createUserProfileManagementServiceStub() throws AxisFault
    {
        return new UserProfileManagementServiceStub(userProfileCinfigContext, userProfileWs.getServiceUrl());
    }

    public UserProfileResponse getUserProfile(UserProfileRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserProfile");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("GetUserProfileRequest======>" + request);
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileResponse response = null;
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("request", request.getCredentialId() + " # " + request.getCredentialType());

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();

                GetUserProfileResponse gpRes = userProfileManagementServiceStub.getUserProfile(TelePersonalizationDataConverter
                        .toGetUserProfileRequest(request));

                cli.addData("response", gpRes == null ? "null" : (gpRes.getResponseCode() + "#" + gpRes.getResponseMessage()));

                response = TelePersonalizationDataConverter.toUserProfileResponse(gpRes);

                if (logger.isDebugEnabled())
                {
                    logger.debug("getUserProfile response======>" + response);
                }

            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#getUserProfile()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }
            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return response;
    }

    public UserProfileResponse getUserProfilePreferences(UserProfileRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserProfilePreferences");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("GetUserProfileRequest======>" + request);
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileResponse response = null;
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("request", request.getCredentialId() + " # " + request.getCredentialType());

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();

                GetUserProfilePreferencesResponse gpRes = userProfileManagementServiceStub.getUserProfilePreferences(TelePersonalizationDataConverter
                        .toGetUserProfilePreferencesRequest(request));

                cli.addData("response", gpRes == null ? "null" : (gpRes.getResponseCode() + "#" + gpRes.getResponseMessage()));

                response = TelePersonalizationDataConverter.toUserProfilePreferencesResponse(gpRes);

                if (logger.isDebugEnabled())
                {
                    logger.debug("getUserProfile response======>" + response);
                }

            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#getUserProfile()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }
            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return response;
    }

    public CSUpdateUserProfileResponse updateUserProfile(CSUpdateUserProfileRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("updateUserProfile");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("updateUserProfile======>" + request);
        }
        boolean startAPICall = false;// the flag whether can start call API
        CSUpdateUserProfileResponse response = new CSUpdateUserProfileResponse();
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("request", request.getCredentialId() + " # " + request.getCredentialType());

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();

                UpdateUserProfileResponse uupRes = userProfileManagementServiceStub.updateUserProfile(TelePersonalizationDataConverter
                        .toUpdateUserProfileRequest(request));

                cli.addData("response", uupRes == null ? "null" : (uupRes.getResponseCode() + "#" + uupRes.getResponseMessage()));

                response = TelePersonalizationDataConverter.toCSUpdateProfileResponse(uupRes);

                if (logger.isDebugEnabled())
                {
                    logger.debug("updateUserProfile response======>" + response);
                }
            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#getUserProfile()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return response;
    }

    public EmailConfirmationResponse sendEmail(EmailConfirmationRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("sendEmail");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("sendEmail======>" + request);
        }
        boolean startAPICall = false;// the flag whether can start call API
        EmailConfirmationResponse response = new EmailConfirmationResponse();
        XnavServiceStub xnavServiceStub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("request", request.getUserId() + " # " + request.getEmail() + " # " + request.getEmailType());

                xnavServiceStub = new XnavServiceStub(emailCinfigContext, emailWs.getServiceUrl());

                ServiceMgmtResponseDTO res = xnavServiceStub.sendConfirmationEmail(TelePersonalizationDataConverter
                        .toEmailConfirmationRequestDTO(request));

                cli.addData("response", res == null ? "null" : ReflectionToStringBuilder.toString(res.getResponseStatus()));

                response = TelePersonalizationDataConverter.toEmailConfirmationResponse(res);
                if (logger.isDebugEnabled())
                {
                    logger.debug("sendEmail response======>" + response);
                }
            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#sendEmail()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(xnavServiceStub);
        }
        return response;
    }

    public long saveAddress(com.telenav.billing.ws.datatypes.userprofile.Address address, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("saveAddress");
        if (address != null && logger.isDebugEnabled())
        {
            logger.debug("saveAddress======>" + address.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        long addressId = -1;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("inputAddr", address == null ? "null" : (address.getStreet() + "," + address.getCity() + "," + address
                        .getCountry()));

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();
                CreateAddressRequest request = new CreateAddressRequest();
                request.setRequestSource(RequestSource.TelenavCServer);
                request.setAddress(address);

                CreateAddressResponse response = userProfileManagementServiceStub.createAddress(request);

                addressId = response.getAddressId();
                cli.addData("response", response == null ? "null"
                        : (response.getResponseCode() + "#" + response.getResponseMessage() + "#" + addressId));

            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#saveAddress()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return addressId;
    }

    public boolean updateAddress(com.telenav.billing.ws.datatypes.userprofile.Address address, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("updateAddress");
        if (address != null && logger.isDebugEnabled())
        {
            logger.debug("updateAddress======>" + address);
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        boolean isSuccess = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {

                cli.addData("inputAddr", address == null ? "null" : (address.getAddressId() + "," + address.getStreet() + ","
                        + address.getCity() + "," + address.getCountry()));

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();

                UpdateAddressRequest request = new UpdateAddressRequest();
                request.setRequestSource(RequestSource.TelenavCServer);
                request.setAddress(address);

                UpdateAddressResponse response = userProfileManagementServiceStub.updateAddress(request);
                isSuccess = TelePersonalizationDataConverter.STATUS_SUCCESS_USERPROFILE.equalsIgnoreCase(response.getResponseCode());

                cli.addData("response", response == null ? "null" : (response.getResponseCode() + "#" + response.getResponseMessage()));
            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#updateAddress()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return isSuccess;
    }

    public com.telenav.billing.ws.datatypes.userprofile.Address getAddress(long addressId, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getAddress");
        if (logger.isDebugEnabled())
        {
            logger.debug("getAddress======>" + addressId);
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        com.telenav.billing.ws.datatypes.userprofile.Address address = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                if (addressId < 0)
                {
                    return address;
                }
                
                cli.addData("inputAddrID", String.valueOf(addressId));

                userProfileManagementServiceStub = createUserProfileManagementServiceStub();
                GetAddressRequest request = new GetAddressRequest();
                request.setRequestSource(RequestSource.TelenavCServer);
                request.setAddressId(addressId);

                GetAddressResponse response = userProfileManagementServiceStub.getAddress(request);
                cli.addData("response", response == null ? "null" : (response.getResponseCode() + "#" + response.getResponseMessage()));
                address = response.getAddress();
            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#getAddress()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(userProfileManagementServiceStub);
        }
        return address;
    }

    public boolean delAddress(long addressId, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("delAddress");
        if (logger.isDebugEnabled())
        {
            logger.debug("delAddress======>" + addressId);
        }
        boolean startAPICall = false;// the flag whether can start call API
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        boolean isSuccess = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(TELEPERSONALIZATION_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                cli.addData("inputAddrID",String.valueOf(addressId));
                
                userProfileManagementServiceStub = createUserProfileManagementServiceStub();
                DeleteAddressRequest request = new DeleteAddressRequest();
                request.setRequestSource(RequestSource.TelenavCServer);
                request.setAddressId(addressId);

                DeleteAddressResponse response = userProfileManagementServiceStub.deleteAddress(request);
                cli.addData("response", response == null ? "null" : (response.getResponseCode() + "#" + response.getResponseMessage()));
                isSuccess = TelePersonalizationDataConverter.STATUS_SUCCESS_USERPROFILE.equalsIgnoreCase(response.getResponseCode());
            }
            catch (Exception ex)
            {
                logger.fatal("TelepersonalizeProxy#delAddress()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(TELEPERSONALIZATION_SERVICE, tc);
            }

            cli.complete();
            closeResource(userProfileManagementServiceStub);

        }
        return isSuccess;
    }

    /**
     * Close stub, actually it is to return the connection back to pool.
     * 
     * @author kwwang
     * @date 2010-5-27
     * @param stub
     */
    private static void closeResource(Stub stub)
    {
        try
        {
            Axis2Helper.close(stub);
        }
        catch (AxisFault fault)
        {
            logger.fatal("TelepersonalizeProxy#closeResource", fault);
        }
    }

}
