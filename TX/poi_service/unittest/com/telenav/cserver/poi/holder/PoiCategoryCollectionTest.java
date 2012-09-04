package com.telenav.cserver.poi.holder;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import static org.junit.Assert.assertEquals;

public class PoiCategoryCollectionTest extends PoiCategoryCollection {

	private PoiCategoryCollection instance = new PoiCategoryCollection();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
		List<PoiCategoryItem> list = new ArrayList<PoiCategoryItem>();
		dataSource.addData(List.class.getName(), list);
		dataSource.addData(UserProfile.class.getName(), TestUtil.getUserProfileForTN64("VIVOGSM"));
	}
		
	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	@Test
	public void testGetPoiHotList()
	{
		UserProfile userprofile = TestUtil.getUserProfileForTN64("VIVOGSM");
		List<PoiCategoryItem> list = instance.getPoiHotList(userprofile);
		assertEquals(12, list.size());
		assertEquals("374", ((PoiCategoryItem)list.get(0)).getId());
		assertEquals("601", ((PoiCategoryItem)list.get(9)).getId());
		userprofile = TestUtil.getUserProfileForTN64("MMI");
		list = instance.getPoiHotList(userprofile);
		assertEquals(12, list.size());
		assertEquals("1026", ((PoiCategoryItem)list.get(0)).getId());
		assertEquals("100040", ((PoiCategoryItem)list.get(9)).getId());
		userprofile = TestUtil.getUserProfile();
		list = instance.getPoiHotList(userprofile);
		assertEquals(12, list.size());
		assertEquals("374", ((PoiCategoryItem)list.get(0)).getId());
		assertEquals("601", ((PoiCategoryItem)list.get(9)).getId());
		
	}


}
