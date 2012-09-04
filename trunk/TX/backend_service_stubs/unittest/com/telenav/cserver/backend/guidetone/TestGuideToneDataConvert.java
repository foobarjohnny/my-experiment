package com.telenav.cserver.backend.guidetone;

import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.billing.ws.datatypes.userprofile.GuideToneOrder;
import com.telenav.cserver.backend.datatypes.guidetone.BackendGuideToneOrder;

public class TestGuideToneDataConvert
{
	GuideToneOrder order;
	
	@Before
	public void init()
	{
		order = new GuideToneOrder();
		order.setUserId(1234567);
		order.setDefaultTone(0);
		order.setGuideToneId(111222333);
		order.setGuideToneName("Test");
		order.setCreateTime(Calendar.getInstance());
		order.setUpdateTime(Calendar.getInstance());
	}
	
	@Test
	public void parse()
	{
		BackendGuideToneOrder backendGuideToneOrder = GuideToneDataConvert.parse(null);
		Assert.assertNull(backendGuideToneOrder);
		backendGuideToneOrder = GuideToneDataConvert.parse(order);
		Assert.assertEquals(order.getUserId(), backendGuideToneOrder.getUserId());
	}

}
