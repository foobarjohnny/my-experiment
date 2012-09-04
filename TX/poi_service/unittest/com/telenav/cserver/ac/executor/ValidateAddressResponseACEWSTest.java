package com.telenav.cserver.ac.executor;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class ValidateAddressResponseACEWSTest {
    
	private ValidateAddressResponseACEWS instance = new ValidateAddressResponseACEWS();
	private DataSource dataSource = new DataSource();
	    
	    
	@Before
	public void setUp() throws Exception {

		dataSource.addData(boolean.class.getName(), Boolean.valueOf(true));	
		dataSource.addData(int.class.getName(), Integer.valueOf(0));	
		dataSource.addData(long.class.getName(), Long.valueOf(5));	
		dataSource.addData(List.class.getName(), getAddress());		
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
	
	public List getAddress(){		
		List<GeoCodedAddress> addr = new ArrayList();
		GeoCodedAddress address = new GeoCodedAddress();
		addr.add(address);
		return addr;
	}
}
