package com.telenav.cserver.poi.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoiListMenuIndexTest
{
	private PoiListMenuIndex testObject;
	private int disableFlag=100;

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testRefreshMenuIndex()
	{
		testObject=new PoiListMenuIndex();
		testObject.setFeedBackEnable(false);
		testObject.refreshMenuIndex();
		Assert.assertEquals(disableFlag, PoiListMenuIndex.FEEDBACK);
		Assert.assertEquals(10, PoiListMenuIndex.POPULAR);
		
		testObject=new PoiListMenuIndex();
		testObject.setShareAddressEnable(false);
		testObject.refreshMenuIndex();
		Assert.assertEquals(disableFlag, PoiListMenuIndex.SHARE);
		Assert.assertEquals(disableFlag, PoiListMenuIndex.SHARE_CONTEXT);
		Assert.assertEquals(10, PoiListMenuIndex.POPULAR);
		Assert.assertEquals(19, PoiListMenuIndex.RATE_CONTEXT);
	}

}
