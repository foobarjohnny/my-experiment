package com.telenav.cserver.ac.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.Stop;

public class ShareAddressResponseTest {

	private ShareAddressResponse instance = new ShareAddressResponse();
	private DataSource dataSource = new DataSource();
   
	@Before
	public void setUp() throws Exception {

		dataSource.addData(boolean.class.getName(), Boolean.valueOf(true));		
		dataSource.addData(int.class.getName(), Integer.valueOf(0));
		dataSource.addData(Stop.class.getName(), new Stop());	
		dataSource.addData(String.class.getName(), String.valueOf("just for test"));	
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data after testing
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
}
