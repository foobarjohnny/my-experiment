package com.telenav.cserver.poi.struts.util;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.util.TestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

public class NSPoiUtilTest {

	TnContext tc = new TnContext();
	@Before
	public void setUp() throws Exception {
		tc = TestUtil.getTnContext();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRoutePointsDistnaceApart() throws ThrottlingException {
		
		PoiSearchRequest poiReq = new PoiSearchRequest();
		int routeId = 61779272;
		int range = 35;
		int radius = 100;
		// need mock 
		NSPoiUtil.getReverseRoutePointsDistnaceApart(tc, poiReq, routeId, range, radius, NSPoiUtil.MAX_SEARCH_DIST);
	}

	@Test
	public void testGetReverseRoutePointsDistnaceApart() throws ThrottlingException {
	
		PoiSearchRequest poiReq = new PoiSearchRequest();
		int routeId = 617792720;
		int range = 35;
		int radius = 100;
		int segmentId = 0;
		int edgeId = 1;
		int shapePointId = 0;
		int lat = 3737392;
		int lon = -12199919;
		NSPoiUtil.getRoutePointsDistnaceApart(tc, routeId, segmentId, edgeId, shapePointId, range, lat, lon, radius, NSPoiUtil.MAX_SEARCH_DIST);

	}

	@Test
	public void testGetRoutePointsForOneBox() throws ThrottlingException {
		
		int range = 35;
		int segmentId = 0;
		int edgeId = 1;
		int shapePointId = 0;
		int lat = 3737392;
		int lon = -12199919;
		NSPoiUtil.getRoutePointsForOneBox(tc, 617792720, segmentId, edgeId, shapePointId, range, lat, lon);
	}

	@Test
	public void testSetReverseRoutePointsAndAnchor() throws ThrottlingException {
		
		PoiSearchRequest poiReq = new PoiSearchRequest();
		int routeId = 617792720;
		int range = 35;
		NSPoiUtil.setReverseRoutePointsAndAnchor(tc, poiReq, routeId, range);
	}

	@Test
	public void testGetRoutePoints() throws ThrottlingException {
		
		int routeId = 617792720;
		int range = 35;
		int segmentId = 0;
		int edgeId = 1;
		int shapePointId = 0;
		int lat = 3737392;
		int lon = -12199919;
		try
		{
			NSPoiUtil.getRoutePoints(routeId, segmentId, edgeId, shapePointId, range, lat, lon);
		}catch(Throwable e)
		{
		//	e.printStackTrace();
		}
	}

	@Test
	public void testConvertToDegree() {
		
		NSPoiUtil.convertToDegree(10000);
	}

}
