/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.holder;

import java.util.List;

import com.telenav.cserver.framework.UserProfile;

import junit.framework.TestCase;

/**
 * Unit test for PoiCategoryHolder, make sure you have added the url
 * path'poi_service/trunk/unittest' as classpath before you run this test.
 * 
 * And the file 'txnode_old_YP50.properties' under folder
 * 'poi_service/trunk/unittest/categoryTree/' is the category tree txnode from
 * tn5.x, you can compare your newly create category tree txnode with this old
 * one.
 * 
 * @author kwwang
 * 
 */
public class PoiCategoryHolderTest extends TestCase {

	public void testGetPoiHotList() {
		UserProfile userProfile = new UserProfile();
		userProfile.setCarrier("MMI");
		PoiCategoryCollection pcc = new PoiCategoryCollection();
		List<PoiCategoryItem> hotList = pcc.getPoiHotList(userProfile);
		assertNotNull(hotList);
		assertEquals(12, hotList.size());
		userProfile.setCarrier("SprintPCS");
		hotList = pcc.getPoiHotList(userProfile);
		assertNotNull(hotList);
		assertEquals(12, hotList.size());
	}

}
