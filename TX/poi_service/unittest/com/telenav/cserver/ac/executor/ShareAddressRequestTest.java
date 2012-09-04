package com.telenav.cserver.ac.executor;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.cserver.weather.executor.TestUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.xnav.datatype.telenavfinder.Poi;

public class ShareAddressRequestTest {

	private ShareAddressRequest instance = new ShareAddressRequest();
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {

		dataSource.addData(long.class.getName(), Long.valueOf(555555));		
		dataSource.addData(boolean.class.getName(), Boolean.valueOf(true));		
		dataSource.addData(String.class.getName(), String.valueOf("5555218135"));	
		dataSource.addData(List.class.getName(), new ArrayList());
		dataSource.addData(Stop.class.getName(), new Stop());	
		dataSource.addData(TnContext.class.getName(), TestUtil.getTnContext());
		dataSource.addData(IPoi.class.getName(), new Poi());
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
