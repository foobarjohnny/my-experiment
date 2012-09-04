package com.telenav.cserver.ac.holder;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class CountryMessagesTest {
	
	private CountryMessages instance = new CountryMessages();
	private DataSource dataSource = new DataSource();

	
	@Before
	public void setUp() throws Exception {
		
		dataSource.addData(Map.class.getName(), getMap());
		dataSource.addData(String.class.getName(), getMap().toString());

	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		dataSource.clear();
		
	}

	@Test
	public void testSuite() {
		
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public Map getMap(){
		Map<String, String> map = new HashMap();
		return map;
	}

}
