package com.telenav.cserver.poi.datatypes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class PoiReviewSummaryTest extends PoiReviewSummary {

	PoiReviewSummary instance = new PoiReviewSummary();
	DataSource dataSrc = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		dataSrc.addData(String.class.getName(), "");
		dataSrc.addData(double.class.getName(), 0.0);
		dataSrc.addData(long.class.getName(), 1);
		dataSrc.addData(int.class.getName(), 2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSrc);
	}

}
