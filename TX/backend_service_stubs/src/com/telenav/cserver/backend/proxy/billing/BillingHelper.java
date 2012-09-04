/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.telenav.billing2.common.dataTypes.ClientProfile;
import com.telenav.billing2.common.dataTypes.CredentialType;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.common.dataTypes.UserCredential;
import com.telenav.billing2.ws.datatypes.account.BindDeviceRequest;
import com.telenav.billing2.ws.datatypes.account.UnBindDeviceRequest;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * BillingHelper
 * 
 * @author kwwang
 * 
 */
public class BillingHelper
{
    public static final String REQUEST_SOURCE = "TELENAV_CSERVER";

    static Logger logger = Logger.getLogger(BillingHelper.class);

    public static UserCredential newUserCredentialOfPtn(UserProfile user, TnContext tc)
    {
        UserCredential uc = null;
        
        if(user.getMin() != null && !"".equals(user.getMin())){
        	uc = new UserCredential();
        	uc.setCredentialType(CredentialType.PTN);
            uc.setCredentialId(user.getMin());
        }else if(user.getUserId() != null && !"".equals(user.getUserId())){
        	uc = new UserCredential();
        	uc.setCredentialType(CredentialType.USER_ID);
            uc.setCredentialId(user.getUserId());
            uc.setPassword(user.getPassword());
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("UserCredential->" + ReflectionToStringBuilder.toString(uc, ToStringStyle.MULTI_LINE_STYLE));
        }
        return uc;
    }
    
    public static UserCredential newUserCredentialOfUserId(UserProfile user, TnContext tc)
    {
        UserCredential uc = null;
        if(user.getUserId() != null && !"".equals(user.getUserId())){
        	uc = new UserCredential();
        	uc.setCredentialType(CredentialType.USER_ID);
            uc.setCredentialId(user.getUserId());
        } else if(user.getMin() != null && !"".equals(user.getMin())){
        	uc = new UserCredential();
        	uc.setCredentialType(CredentialType.PTN);
            uc.setCredentialId(user.getMin());
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("UserCredential->" + ReflectionToStringBuilder.toString(uc, ToStringStyle.MULTI_LINE_STYLE));
        }
        return uc;
    }
    
    public static BindDeviceRequest getBindDeviceRequest(UserProfile user, TnContext tc)
    {
    	BindDeviceRequest request = new BindDeviceRequest();
    	UserCredential uc = new UserCredential();
    	if(StringUtils.isNotBlank(user.getMin()))
    	{
	    	uc.setCredentialType(CredentialType.PTN);
	        uc.setCredentialId(user.getMin());
	        uc.setPassword(user.getPassword());
    	}
    	else if(StringUtils.isNotBlank(user.getUserId()))
    	{
    		uc.setCredentialType(CredentialType.USER_ID);
	        uc.setCredentialId(user.getUserId());
    	}
    	request.setUserCredential(uc);
    	Property property = new Property();
    	property.setKey("DEVICE_ID");
    	property.setValue(user.getDeviceID());
    	request.addExtraProperty(property);
    	ClientProfile clientProfile = newClientProfileWithoutAppCode(user, tc);
    	clientProfile.setAppCode("Navigation");
    	request.setClientProfile(clientProfile);
    	return request;
    }
    
    public static BindDeviceRequest getBindDeviceRequestWithUserId(UserProfile user, TnContext tc)
    {
    	BindDeviceRequest request = new BindDeviceRequest();
    	UserCredential uc = new UserCredential();
    	uc.setCredentialType(CredentialType.USER_ID);
	    uc.setCredentialId(user.getUserId());
    	request.setUserCredential(uc);
    	Property property = new Property();
    	property.setKey("DEVICE_ID");
    	property.setValue(user.getDeviceID());
    	request.addExtraProperty(property);
    	ClientProfile clientProfile = newClientProfileWithoutAppCode(user, tc);
    	clientProfile.setAppCode("Navigation");
    	request.setClientProfile(clientProfile);
    	return request;
    }
    
    public static UnBindDeviceRequest createUnBindDeviceRequest(String deviceId, String userId)
    {
    	UnBindDeviceRequest request = new UnBindDeviceRequest();
    	request.setDeviceId(deviceId);
    	UserCredential uc = new UserCredential();
    	uc.setCredentialType(CredentialType.USER_ID);
        uc.setCredentialId(userId);
        request.setUserCredential(uc);
    	return request;
    }

    public static ClientProfile newClientProfileWithoutAppCode(UserProfile user, TnContext tc)
    {
        ClientProfile cp = new ClientProfile();
        cp.setRequestSource(REQUEST_SOURCE);
        cp.setCarrierCode(user.getDeviceCarrier());
        cp.setPhoneModel(user.getDevice());
        cp.setPlatform(user.getPlatform());
        cp.setClientVersion(user.getVersion());
        cp.setProgramCode(user.getProgramCode());
        cp.setClientProductType(user.getProduct());
        cp.setLanguage(user.getLocale());
        if (logger.isDebugEnabled())
        {
            logger.debug("ClientProfile->" + ReflectionToStringBuilder.toString(cp, ToStringStyle.MULTI_LINE_STYLE));
        }
        return cp;
    }
}
