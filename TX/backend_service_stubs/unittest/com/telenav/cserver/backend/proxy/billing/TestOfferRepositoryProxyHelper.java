package com.telenav.cserver.backend.proxy.billing;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.offerrepository.dataTypes.GetPurchaseOptionRequest;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestOfferRepositoryProxyHelper
{
	UserProfile profile;
	
	TnContext tc = new TnContext();
	
	@Before
	public void init()
	{
		profile = new UserProfile();
		profile.setPlatform("ANDROID");
		profile.setDevice("default");
		profile.setCarrier("ATT");
		profile.setVersion("7.0.01");
		profile.setUserId("16092933");
		profile.setMin("1234567890");
		profile.setMacID("Fake MacID");
		profile.setDeviceID("Fake DeviceId");
	}
	
	@Test
	public void newGetPurchaseOptionRequest()
	{
		GetPurchaseOptionRequest request = OfferRepositoryProxyHelper.newGetPurchaseOptionRequest(profile, tc);
		Assert.assertEquals(profile.getDeviceID(), request.getExtraProperty()[0].getValue());
		Assert.assertEquals(profile.getMacID(), request.getExtraProperty()[1].getValue());
	}
	
	@Test
	public void getPurchaseOptionRequestByUserId()
	{
		GetPurchaseOptionRequest request = OfferRepositoryProxyHelper.getPurchaseOptionRequestByUserId(profile, tc);
		Assert.assertEquals(profile.getUserId(), request.getUserCredential().getCredentialId());
		profile.setUserId("");
		request = OfferRepositoryProxyHelper.getPurchaseOptionRequestByUserId(profile, tc);
		Assert.assertEquals(profile.getMin(), request.getUserCredential().getCredentialId());
	}
	
	@Test
	public void getPurchaseOptionRequestByUserIdMacID()
	{
		GetPurchaseOptionRequest request = OfferRepositoryProxyHelper.getPurchaseOptionRequestByUserIdMacID(profile, tc);
		Assert.assertEquals(profile.getDeviceID(), request.getExtraProperty()[0].getValue());
		Assert.assertEquals(profile.getMacID(), request.getExtraProperty()[1].getValue());
	}
}
