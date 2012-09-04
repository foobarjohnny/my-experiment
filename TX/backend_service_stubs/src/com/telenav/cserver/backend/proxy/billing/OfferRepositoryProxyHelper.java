/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import com.telenav.billing2.common.dataTypes.ClientProfile;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.offerrepository.dataTypes.GetPurchaseOptionRequest;
import com.telenav.cserver.backend.facade.billing.BillingConstants;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * OfferRepositoryProxyHelper
 * @author kwwang
 *
 */
public class OfferRepositoryProxyHelper
{
    public static GetPurchaseOptionRequest newGetPurchaseOptionRequest(UserProfile user,TnContext tc)
    {
        GetPurchaseOptionRequest request = new GetPurchaseOptionRequest();
        request.setUserCredential(BillingHelper.newUserCredentialOfPtn(user, tc));
        ClientProfile cp=BillingHelper.newClientProfileWithoutAppCode(user, tc);
        cp.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);
        request.setClientProfile(cp);
        request.setShowActiveOffers(NonEmptyConverter.toNonEmptyBoolean(false));
        
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
    
    public static GetPurchaseOptionRequest getPurchaseOptionRequestByUserId(UserProfile user,TnContext tc)
    {
        GetPurchaseOptionRequest request = new GetPurchaseOptionRequest();
        request.setUserCredential(BillingHelper.newUserCredentialOfUserId(user, tc));
        ClientProfile cp=BillingHelper.newClientProfileWithoutAppCode(user, tc);
        cp.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);
        request.setClientProfile(cp);
        request.setShowActiveOffers(NonEmptyConverter.toNonEmptyBoolean(false));
        
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
    
    public static GetPurchaseOptionRequest getPurchaseOptionRequestByUserIdMacID(UserProfile user,TnContext tc)
    {
        GetPurchaseOptionRequest request = new GetPurchaseOptionRequest();
        request.setUserCredential(BillingHelper.newUserCredentialOfUserId(user, tc));
        ClientProfile cp=BillingHelper.newClientProfileWithoutAppCode(user, tc);
        cp.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);
        Property[] extraProp = new Property[2];
        request.setClientProfile(cp);
        request.setShowActiveOffers(NonEmptyConverter.toNonEmptyBoolean(false));
        Property device_prop = new Property();
        device_prop.setKey("DEVICE_ID");
        device_prop.setValue(user.getDeviceID() != null ? user.getDeviceID():"");
    	Property mac_prop = new Property();
    	mac_prop.setKey("MAC_ADDRESS");
    	mac_prop.setValue(user.getMacID() != null ? user.getMacID():"");
    	extraProp[0] = device_prop;
    	extraProp[1] = mac_prop;
    	request.setExtraProperty(extraProp);
        	
        return request;
    }
}
