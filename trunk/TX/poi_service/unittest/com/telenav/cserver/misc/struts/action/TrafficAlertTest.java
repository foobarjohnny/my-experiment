package com.telenav.cserver.misc.struts.action;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.datatypes.traffic.common.v10.AlertDTO;

public class TrafficAlertTest {

	private TrafficAlert instance = null;
	private DataSource dataSource = new DataSource();

	@Before
	public void setUp() throws Exception {
		
		int value = 400000;
		AlertDTO alertDto = new AlertDTO();
		alertDto.setLat(value);
		alertDto.setLon(value);
		
		instance = new TrafficAlert(value, value, alertDto);
		
		dataSource.addData(int.class.getName(), Integer.valueOf(value));        
		dataSource.addData(AlertDTO.class.getName(), alertDto);
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
}
