package com.telenav.cserver.misc.struts.datatype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class AddressTest {

	
	private Address instance = new Address();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {

		dataSource.addData(String.class.getName(), "just for test");		
		dataSource.addData(long.class.getName(), Long.valueOf(1));
		dataSource.addData(List.class.getName(), new ArrayList());
		dataSource.addData(Date.class.getName(), new Date(System.currentTimeMillis()));
		
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing
		dataSource.clear();
		
	}

	@Test
	public void testSuite(){		
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
}
