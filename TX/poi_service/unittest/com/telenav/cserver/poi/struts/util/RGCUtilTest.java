package com.telenav.cserver.poi.struts.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.util.TestUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.televigation.proxycommon.LatLonPoint;

public class RGCUtilTest extends RGCUtil {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCurrentLocation() {
		RGCUtil rgcUtil = new RGCUtil();
		TnContext tc = TestUtil.getTnContext();
		rgcUtil.getCurrentLocation(3737453, -12199983, tc);
		
		rgcUtil.getCurrentLocation(0, 0, tc);
	}

	@Test
	public void testQueryRGC() throws ThrottlingException {
		double radius = 5.0D;
		TnContext tnContext = TestUtil.getTnContext();
		
		LatLonPoint latLonPoint = new LatLonPoint(3737453, -12199983);
		RGCUtil.queryRGC(latLonPoint, radius, tnContext);
		
	}

	@Test
	public void testConvertToDM5() {
		RGCUtil.convertToDM5(100000);
	}

}
