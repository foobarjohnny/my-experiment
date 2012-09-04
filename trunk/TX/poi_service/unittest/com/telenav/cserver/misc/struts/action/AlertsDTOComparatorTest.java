package com.telenav.cserver.misc.struts.action;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.traffic.common.v10.AlertDTO;

public class AlertsDTOComparatorTest {
	
	private AlertsDTOComparator alertsDTOComparator = new AlertsDTOComparator();
	private TrafficAlert trafficAlert1 = null;
	private TrafficAlert trafficAlert2 = null;
	
	@Before
	public void setUp() throws Exception {
		
		// san francisco
		int lat = 3777500;
		int lon = -12241833;
		AlertDTO alertDto1 = new AlertDTO();
		alertDto1.setLat(lat);
		alertDto1.setLon(lon);
		
		AlertDTO alertDto2 = new AlertDTO();
		alertDto2.setLat(lat + 100000);
		alertDto2.setLon(lon + 100000);
		
		trafficAlert1 = new TrafficAlert(lat+1000, lon+2000, alertDto1);
		trafficAlert2 = new TrafficAlert(lat + 110000, lon + 120000, alertDto2);		
	}

	@Test
	public void testCompare() {
		int expected = 1;
		int actual = alertsDTOComparator.compare(trafficAlert2, trafficAlert1);
		Assert.assertEquals(expected, actual);
	}

}
