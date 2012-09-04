package com.telenav.cserver.backend.proxy.billing;

/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 *  @author Jianyu Zhou
 */
import com.telenav.billing2.common.dataTypes.ClientProfile;
import com.telenav.billing2.common.dataTypes.CredentialType;
import com.telenav.billing2.common.dataTypes.NonEmptyLong;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.common.dataTypes.UserCredential;
import com.telenav.billing2.ws.datatypes.payment.ConfirmPurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.DeclinePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.PreparePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.ProcessAMPMessageRequest;
import com.telenav.billing2.ws.datatypes.payment.PurchaseItem;
import com.telenav.cserver.backend.facade.billing.BillingConstants;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

public class PaymentProxyHelper {

	public static ConfirmPurchaseRequest newConfirmPurchaseRequest(
			UserProfile user, TnContext tc, long orderId, String receipt) {
		ConfirmPurchaseRequest request = new ConfirmPurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));
		
		//set extra properties here.
		Property param = new Property();
		param.setKey(BillingConstants.RECEIPT);
		param.setValue(receipt);
		request.addExtraProperty(param);
		param = new Property();
		param.setKey(BillingConstants.MAC_ADDRESS);
		param.setValue(user.getMacID());
		request.addExtraProperty(param); 
				 
		 
		ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(
				user, tc);
		profile.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);  
		request.setClientProfile(profile);
		return request;
	}
	
	
	public static DeclinePurchaseRequest newDeclinePurchaseRequest(
			UserProfile user, TnContext tc, long orderId) {
		DeclinePurchaseRequest request = new DeclinePurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));
		
		Property param = new Property();
		param.setKey(BillingConstants.MAC_ADDRESS);
		param.setValue(user.getMacID());
		request.addExtraProperty(param); 
		
		ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(
				user, tc);
		profile.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);  
		request.setClientProfile(profile);
		return request;
	}
	
	//for testing only
	public static PreparePurchaseRequest newPreparePurchaseRequest(
			UserProfile user, TnContext tc, String credentialId) {
		PreparePurchaseRequest request = new PreparePurchaseRequest();
		
		UserCredential uc = new UserCredential();
		uc.setCredentialType(CredentialType.PTN);
		uc.setCredentialId(credentialId);
		
		request.setUserCredential(uc);
		
		PurchaseItem item = new PurchaseItem();
		item.setOfferCode("tnsc_itunes_30D");
		item.setQuantity(NonEmptyConverter.toNonEmptyInteger(1));
		
		PurchaseItem[] items = new PurchaseItem[1];
		items[0] = item;
		
		request.setPurchaseItems(items);
		
		ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(
				user, tc);
		profile.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);  
		request.setClientProfile(profile);
		
		Property param = new Property();
		param.setKey(BillingConstants.MAC_ADDRESS);
		param.setValue(user.getMacID());
		request.addExtraProperty(param); 
		
		return request;
	}
	
	
	public static ProcessAMPMessageRequest newProcessAMPMessageRequest(long orderId,String signature,String signedData,UserProfile user, TnContext tc)
	{
		ProcessAMPMessageRequest request = new ProcessAMPMessageRequest();
		NonEmptyLong ampOrderId=new NonEmptyLong();
		ampOrderId.setValue(orderId);
		request.setOrderId(ampOrderId);
		request.setSignature(signature);
		request.setSignedData(signedData);
		ClientProfile profile = BillingHelper.newClientProfileWithoutAppCode(
				user, tc);
		profile.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);  
		request.setClientProfile(profile);
		request.setUserCredential(BillingHelper.newUserCredentialOfUserId(user, tc));
		return request;
	}
}
