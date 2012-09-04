package com.telenav.cserver.backend.proxy.billing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.billing2.ws.datatypes.account.BindDeviceResponse;
import com.telenav.billing2.ws.datatypes.account.UnBindDeviceResponse;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestBindDeviceProxy {
	private BindDeviceProxy proxy;
	private TnContext tc;
	private UserProfile user;

	@Before
	public void init() {
		proxy = new BindDeviceProxy();
		tc = new TnContext();
		user=new UserProfile();
		user.setMin("5198887465");
		user.setUserId("9965289");
		user.setPassword("7465");
		user.setDeviceID("dummyDeviceId");
		user.setProgramCode("SCOUTPROG");
		user.setPlatform("IPHONE");
		user.setVersion("7.0.01");
		user.setDevice("default");
		user.setProduct("SCOUT_PAID");
	}
	
	@Test
	public void bindDevice() {
		BindDeviceResponse resp=proxy.bindDevice(BillingHelper.getBindDeviceRequest(user, tc), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("0000", resp.getResponseCode());
		
		user.setMin("3817799999");
		user.setPassword("9999");
		user.setUserId("9894832");
		resp=proxy.bindDevice(BillingHelper.getBindDeviceRequestWithUserId(user, tc), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("0000", resp.getResponseCode());
	}

	@Test
	public void unbindDevice() {
		UnBindDeviceResponse resp = proxy.unbindDevice(BillingHelper.createUnBindDeviceRequest("dummyDeviceId","9965289"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("0000", resp.getResponseCode());
		
		resp = proxy.unbindDevice(BillingHelper.createUnBindDeviceRequest("dummyDeviceId","9894832"), tc);
		Assert.assertNotNull(resp);
		Assert.assertEquals("0000", resp.getResponseCode());
	}
}
