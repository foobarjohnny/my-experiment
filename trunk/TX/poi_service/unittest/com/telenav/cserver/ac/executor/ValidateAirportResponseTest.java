package com.telenav.cserver.ac.executor;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.datatypes.content.tnpoi.v20.TnPoi;

public class ValidateAirportResponseTest {

	private ValidateAirportResponse instance = new ValidateAirportResponse();
	private DataSource dataSource = new DataSource();
	    
	@Before
	public void setUp() throws Exception {
		dataSource.addData(List.class.getName(), getPoiList());
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data after testing
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}

	public List getPoiList(){
		List<TnPoi> poiList = new ArrayList();
		poiList.add(new TnPoi());
		return poiList;
	}
}
