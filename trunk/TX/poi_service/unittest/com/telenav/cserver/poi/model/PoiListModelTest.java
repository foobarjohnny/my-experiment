package com.telenav.cserver.poi.model;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class PoiListModelTest extends PoiListModel {

	PoiListModel poiListModel = new PoiListModel();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		dataSource.addData(String.class.getName(), String.valueOf("1"));

	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startStaticTest(poiListModel, dataSource);
	}

}
