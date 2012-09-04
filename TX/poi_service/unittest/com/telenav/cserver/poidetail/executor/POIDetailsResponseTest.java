package com.telenav.cserver.poidetail.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.poidetail.executor.v20.POIDetailsResponse;
import com.telenav.cserver.util.helper.DataSource;
import com.telenav.cserver.util.helper.GenericTest;

public class POIDetailsResponseTest extends POIDetailsResponse{

	private POIDetailsResponse instance = new POIDetailsResponse();
	private DataSource dataSrc = new DataSource();

	@Before
	public void setUp() throws Exception {
	
		dataSrc.addData(String.class.getName(), "POIDetails");		
		dataSrc.addData(long.class.getName(), 1234);
	}
	
	@After
	public void tearDown() throws Exception {
		dataSrc.clear();    // clear resource
	}

	@Test
	public void testSuite(){
		
		GenericTest.getInstance().startTest(instance, dataSrc);
	}

}
