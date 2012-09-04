package com.telenav.cserver.backend.proxy.billing;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.ws.datatypes.payment.ConfirmPurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.DeclinePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.PreparePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.ProcessAMPMessageRequest;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestPaymentProxyHelper
{
	TnContext tc;
	
	UserProfile profile;
	
	long orderId = 123456789;
	@Before
	public void init()
	{
		tc = new TnContext();
		profile = new UserProfile();
		profile.setPlatform("ANDROID");
		profile.setDevice("default");
		profile.setCarrier("ATT");
		profile.setVersion("7.0.01");
		profile.setUserId("16092933");
		profile.setMin("1234567890");
		profile.setMacID("Fake MacID");
	}
	
	
	@Test
	public void newConfirmPurchaseRequest()
	{
		String receipt = "receipt";
		ConfirmPurchaseRequest request = PaymentProxyHelper.newConfirmPurchaseRequest(profile, tc, orderId, receipt);
		Assert.assertEquals(orderId, request.getOrderId().getValue());
		Assert.assertEquals(receipt, request.getExtraProperty()[0].getValue());
	}
	
	@Test
	public void newDeclinePurchaseRequest()
	{
		DeclinePurchaseRequest request =  PaymentProxyHelper.newDeclinePurchaseRequest(profile, tc, orderId);
		Assert.assertEquals(profile.getMacID(), request.getExtraProperty()[0].getValue());
		Assert.assertEquals(ServiceProvisioningHelper.NAVIGATION_APP_CODE, request.getClientProfile().getAppCode());
	}
	
	@Test
	public void newPreparePurchaseRequest()
	{
		String credentialId = "Fake credentialId";
		PreparePurchaseRequest request = PaymentProxyHelper.newPreparePurchaseRequest(profile, tc, credentialId);
		Assert.assertEquals(credentialId, request.getUserCredential().getCredentialId());
		Assert.assertEquals("tnsc_itunes_30D", request.getPurchaseItems()[0].getOfferCode());
	}
	
	@Test
	public void newProcessAMPMessageRequest()
	{
		String signature = "Fake Signature";
		String signedData = "Fake SignedData";
		ProcessAMPMessageRequest request = PaymentProxyHelper.newProcessAMPMessageRequest(orderId, signature, signedData, profile, tc);
		Assert.assertEquals("16092933", request.getUserCredential().getCredentialId());
		
		profile.setUserId("");
		request = PaymentProxyHelper.newProcessAMPMessageRequest(orderId, signature, signedData, profile, tc);
		Assert.assertEquals("1234567890", request.getUserCredential().getCredentialId());
	}
}
