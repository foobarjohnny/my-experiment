package com.telenav.cserver.poi.datatypes;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.LocalAppInfo;

public class BasePoiTest {

	String AppInfoName = "appInfoName";
	BasePoi instance = new BasePoi();
	@Before
	public void setUp() throws Exception {
		
		LocalAppInfo localAppInfo = new LocalAppInfo(AppInfoName);
		List<LocalAppInfo> localAppInfos = new ArrayList<LocalAppInfo>();
		localAppInfos.add(localAppInfo);
		instance.setLocalAppInfos(localAppInfos);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLocalAppInfos() {
		
		Assert.assertNotNull(instance.getLocalAppInfos());
		Assert.assertEquals(AppInfoName, instance.getLocalAppInfos().get(0).getName());
		
	}

	@Test
	public void testToProtobuf() {
		
		instance.poiId = 1234;
		instance.popularity = 1;
		instance.isRatingEnable = true;
		instance.hasUserRatedThisPoi = true;
		instance.avgRating = 1;
		BizPOI bizPOI = new BizPOI();
		BasePoiExtraInfo basePoiExtraInfo = new BasePoiExtraInfo();
		instance.bizPOI = bizPOI;
		instance.basePoiExtraInfo = basePoiExtraInfo;    
		
		instance.toProtobuf();
	}

}
