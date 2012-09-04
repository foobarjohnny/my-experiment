/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.datatypes.Constants;
import com.telenav.cserver.backend.datatypes.navstar.RoutePosition;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.NavResult;
import com.telenav.navstar.proxy.facade.NavPreference;
import com.telenav.navstar.proxy.facade.NavRoutePath;
import com.telenav.navstar.proxy.facade.NavStatus;
import com.telenav.navstar.proxy.facade.NavPreference.RouteStyle;
import com.televigation.proxycommon.ProxyAddress;

/**
 * test case for {@link NavstarServiceProxy}
 * 
 * @author mmwang
 * @version 1.0 2010-7-14
 * 
 */
public class TestNavstarServiceProxy extends TestCase
{

	private static Logger logger = Logger.getLogger(TestNavstarServiceProxy.class);

	private NavstarServiceProxy proxy;

	private TnContext tc;

	private NavPreference preference;

	// meters

	public static final double METER_TO_DEGREES = 1 / NavstarServiceProxy.SCALING_CONSTANT;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		URL url = TestNavstarServiceProxy.class.getClassLoader().getResource(
				"navstarproxy.properties");
		if (url == null)
		{
			fail("this test suite need a config file 'navstarproxy.properties' in the root of classpath");
		}

		proxy = NavstarServiceProxy.getInstance();

		// init the tncontext
		tc = new TnContext();
		tc.addProperty("GENERATE_LANE_INFO", "true");
		tc.addProperty("device", "9630");
		tc.addProperty("c-server url", "localhost:8080");
		tc.addProperty("c-server class", "CServer6x_HTTP");
		tc.addProperty("requestor", "tnclient");
		tc.addProperty("dataset", "Navteq");
		tc.addProperty("poidataset", "TA");
		tc.addProperty("carrier", "SprintPCS");
		tc.addProperty("version", "6.0.01");
		tc.addProperty("application", "SN_prem");
		tc.addProperty("login", "5198887465");
		tc.addProperty("userid", "3698214");
		tc.addProperty("product", "RIM");
		// tc.addProperty("XNAV_TIMESTAMP",
		// String.valueOf(System.currentTimeMillis()));

		// set default preference;
		preference = new NavPreference();
		preference.setStyle(RouteStyle.FASTEST);
		preference.setFilters(NavPreference.NO_FILTERS);
		preference.setSettingGetMFID(NavPreference.GETMFIDS_YES);
	}

	/**
	 * get a list of gpsdata for test
	 * 
	 * @return
	 */
	private List<GpsData> getGpsDataList()
	{
		List<GpsData> result = new ArrayList<GpsData>();
		long timeflag = System.currentTimeMillis() / 10;
		GpsData gps = createGpsData(timeflag);
		result.add(gps);
		timeflag -= 1500;
		gps = createGpsData(timeflag);
		result.add(gps);
		timeflag -= 1500;
		gps = createGpsData(timeflag);
		result.add(gps);
		timeflag -= 1500;
		gps = createGpsData(timeflag);
		result.add(gps);
		timeflag -= 1500;
		gps = createGpsData(timeflag);
		result.add(gps);
		return result;
	}

	/**
	 * @param timeflag
	 * @return
	 */
	private GpsData createGpsData(long timeflag)
	{
		GpsData gps = new GpsData();
		gps.lat = (int) (37.37392 * Constants.DEGREE_MULTIPLIER);
		gps.lon = (int) (-121.99934 * Constants.DEGREE_MULTIPLIER);
		gps.timeTag = timeflag;
		gps.speed = (int) (1 * METER_TO_DEGREES * 10);
		gps.heading = 180;
		gps.speedAndHeadingToVelocity();
		return gps;
	}

	/**
	 * @return
	 */
	private ProxyAddress getDestProxyAddress()
	{
		return new ProxyAddress("7440 FILICE DR", "GILROY", "CA", null,
				37.55570, -122.37028);
	}

	/**
	 * @return
	 */
	private ProxyAddress getOriginalProxyAddress()
	{
		return new ProxyAddress("535 Pacific Ave", "San Francisco", "CA", null,
				37.797328, -122.403785);
	}

	/**
	 * get the message of navstatus
	 * 
	 * @param status
	 * @return
	 */
	protected String getNavResultMessage(NavStatus status)
	{
		StringBuffer strBuff = new StringBuffer();

		if (status != null)
		{
			if (status.getStatusCode() != null)
			{
				strBuff.append("RESULT_ERROR_CODE=").append(
						status.getStatusId()).append("&MESSAGE=").append(
						status.getMessage()).append("&CEC-CODE=").append(
						status.getCecCode());
			}

		}

		return strBuff.toString();
	}

	/**
	 * some case need start a route session first
	 * 
	 * @return
	 * @throws ThrottlingException
	 */
	private int startDynamicRouteAndReturnRoutePathId()
			throws ThrottlingException
	{
		int routePathId = 0;

		// start a dyna route first
		ProxyAddress address = getDestProxyAddress();
		NavResult sessionResult = proxy.startSession(tc, address)
				.getNavResult();
		assertNotNull(sessionResult);
		boolean isSuccess = sessionResult.isSuccess();
		logger.debug(getNavResultMessage(sessionResult.getStatus()));
		assertTrue("shouldSuccess", isSuccess);

		// prepare the arguments;
		List measurements = NavstarDataConverter.convertGPSDataListToNavMeasList(
				getGpsDataList(), false);
		double lkh = -1d;
		NavResultResponse dynamicRoute = proxy.findRoute(tc, measurements, lkh,
				preference);
		assertNotNull(dynamicRoute);
		NavResult res = dynamicRoute.getNavResult();
		assertNotNull(res);
		logger.debug(getNavResultMessage(res.getStatus()));
		assertTrue(res.isSuccess());
		NavRoutePath routePath = res.getPreferredActiveRoutePath();
		assertNotNull(routePath);
		routePathId = routePath.id();
		return routePathId;
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findRoute(com.telenav.kernel.util.datatypes.TnContext, java.util.List, double, com.telenav.navstar.proxy.facade.NavPreference)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindRouteTnContextListDoubleNavPreference()
			throws ThrottlingException
	{
		// need start session first
		ProxyAddress address = getDestProxyAddress();
		NavResult sessionResult = proxy.startSession(tc, address)
				.getNavResult();
		assertNotNull(sessionResult);
		boolean isSuccess = sessionResult.isSuccess();
		logger.debug(getNavResultMessage(sessionResult.getStatus()));
		assertTrue("shouldSuccess", isSuccess);

		// prepare the arguments;
		List measurements = NavstarDataConverter.convertGPSDataListToNavMeasList(
				getGpsDataList(), false);
		double lkh = -1d;
		NavResultResponse result = proxy.findRoute(tc, measurements, lkh,
				preference);
		assertNotNull(result);
		NavResult res = result.getNavResult();
		assertNotNull(res);
		logger.debug(getNavResultMessage(res.getStatus()));
		assertTrue(res.isSuccess());

	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findRoute(com.telenav.kernel.util.datatypes.TnContext, com.televigation.proxycommon.ProxyAddress, com.telenav.navstar.proxy.facade.NavPreference)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindRouteTnContextProxyAddressNavPreference()
			throws ThrottlingException
	{
		// need start session first
		ProxyAddress address = getDestProxyAddress();
		NavResult sessionResult = proxy.startSession(tc, address)
				.getNavResult();
		assertNotNull(sessionResult);
		boolean isSuccess = sessionResult.isSuccess();
		logger.debug(getNavResultMessage(sessionResult.getStatus()));
		assertTrue("shouldSuccess", isSuccess);

		// instant a original address
		ProxyAddress origin = getOriginalProxyAddress();

		NavResultResponse result = proxy.findRoute(tc, origin, preference);
		assertNotNull(result);
		NavResult res = result.getNavResult();
		assertNotNull(res);
		logger.debug(getNavResultMessage(res.getStatus()));
		assertTrue(res.isSuccess());
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findRoute(com.telenav.kernel.util.datatypes.TnContext, com.televigation.proxycommon.ProxyAddress, com.televigation.proxycommon.ProxyAddress, com.telenav.navstar.proxy.facade.NavPreference)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindRouteTnContextProxyAddressProxyAddressNavPreference()
			throws ThrottlingException
	{
		// no need start session in this case;
		ProxyAddress dest = getDestProxyAddress();
		ProxyAddress original = getOriginalProxyAddress();
		NavResultResponse response = proxy.findRoute(tc, dest, original,
				preference);

		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		logger.debug(getNavResultMessage(result.getStatus()));
		assertTrue(result.isSuccess());

	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#retrieveLastResult(com.telenav.kernel.util.datatypes.TnContext, int, int)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testRetrieveLastResult() throws ThrottlingException
	{
		int routeId = 0;
		int routePathId = startDynamicRouteAndReturnRoutePathId();

		// start test last result
		NavResultResponse response = proxy.retrieveLastResult(tc, routeId,
				routePathId);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (result.isSuccess())
		{
			NavRoutePath path = result.getActiveRoutePath(routeId, routePathId);
			assertNotNull(path);
			assertTrue(result.getRoutes().size() > 0);
			assertNotNull(result.getRoutes().get(0));
		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#retrieveSegmentsFromSession(com.telenav.kernel.util.datatypes.TnContext, int, int, int, int, boolean)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testRetrieveSegmentsFromSession() throws ThrottlingException
	{
		int routeId = 0;
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int startSegmentId = 0;
		int endSegmentId = 4;

		// start test last result
		NavResultResponse response = proxy.retrieveSegmentsFromSession(tc,
				routeId, routePathId, startSegmentId, endSegmentId, true);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (result.isSuccess())
		{
			NavRoutePath path = result.getActiveRoutePath(routeId, routePathId);
			assertNotNull(path);
			assertNotNull(path.getSegments());
			assertTrue(path.getSegments().size() > 0);
		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#selectAlternateRoute(com.telenav.kernel.util.datatypes.TnContext)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testSelectAlternateRoute() throws ThrottlingException
	{
		// must do find a alter route first
		testFindDynamicAlternateRoute();

		NavResultResponse response = proxy.selectAlternateRoute(tc);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (!result.isSuccess())
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#startSession(com.telenav.kernel.util.datatypes.TnContext, com.televigation.proxycommon.ProxyAddress)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testStartSession() throws ThrottlingException
	{
		ProxyAddress dest = getDestProxyAddress();
		NavResultResponse response = proxy.startSession(tc, dest);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (!result.isSuccess())
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#calculateDelayTime(com.telenav.kernel.util.datatypes.TnContext, int, int, int, int, int, float)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testCalculateDelayTime() throws ThrottlingException
	{
		int routeId = 0;
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int segIndex = 0;
		int edgeIndex = 4;

		NavResultResponse response = proxy.calculateDelayTime(tc, routeId,
				routePathId, segIndex, edgeIndex, 0, 0);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (result.isFailure())
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#getSPTByIndex(com.telenav.kernel.util.datatypes.TnContext, int, int, int, int)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testGetSPTByIndex() throws ThrottlingException
	{
		int routeId = 0;
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int edgeStartIndex = 0; // copy from regression test case
		int edgeEndIndex = 50; // copy from regression test case

		NavResultResponse response = proxy.getSPTByIndex(tc, routeId,
				routePathId, edgeStartIndex, edgeEndIndex);
		assertNotNull(response);
		NavResult result = response.getNavResult();

		assertNotNull(result);
		if (result.isFailure())
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findReverseRoute(com.telenav.kernel.util.datatypes.TnContext)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindReverseRoute() throws ThrottlingException
	{
		startDynamicRouteAndReturnRoutePathId();

		NavResultResponse response = proxy.findReverseRoute(tc);

		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);

		if (result.isSuccess())
		{
			if (result.getPreferredActiveRoutePath() == null)
			{
				fail("not found route");
			}
		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#getTrafficSummary(com.telenav.kernel.util.datatypes.TnContext, com.telenav.cserver.backend.navstar.TrafficSummaryRequest)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testGetTrafficSummary() throws ThrottlingException
	{
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int segIndex = 0;
		int edgeIndex = 0;
		int pointIndex = 0;
		int distance = 200000;
		TrafficSummaryRequest request = new TrafficSummaryRequest(tc,
				routePathId, segIndex, edgeIndex, pointIndex, 0f, distance);

		TrafficSummaryResponse response = proxy.getTrafficSummary(tc, request);
		if (response != null && response.isSuccess())
		{
			assertNotNull(response.getSegments());
			assertTrue(response.getSegments().size() > 0);
		} else
		{
			fail(getNavResultMessage(response.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#getTrafficAlerts(com.telenav.kernel.util.datatypes.TnContext, com.telenav.cserver.backend.navstar.TrafficAlertRequest)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testGetTrafficAlerts() throws ThrottlingException
	{
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int segIndex = 0;
		int edgeIndex = 0;
		int pointIndex = 0;
		int distance = 200000;
		TrafficAlertRequest request = new TrafficAlertRequest(tc, routePathId,
				segIndex, edgeIndex, pointIndex, 0, distance);

		TrafficAlertResponse response = proxy.getTrafficAlerts(tc, request);
		if (response != null && response.isSuccess())
		{
			assertNotNull(response.getAlerts());
//			assertTrue(response.getAlerts().size() > 0);
		} else
		{
			fail(getNavResultMessage(response.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findBetterDynamicRoute(com.telenav.kernel.util.datatypes.TnContext, com.telenav.cserver.backend.navstar.BetterDynamicRouteRequest)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindBetterDynamicRoute() throws ThrottlingException
	{
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		routePathId = 814790740;
		int segIndex = 0;
		int edgeIndex = 0;
		int pointIndex = 0;
		float offset = 0f;
		RoutePosition position = new RoutePosition(0, routePathId, segIndex,
				edgeIndex, pointIndex, offset);

		List measurements = NavstarDataConverter.convertGPSDataListToNavMeasList(
				getGpsDataList(), false);
		BetterDynamicRouteRequest request = new BetterDynamicRouteRequest(tc,
				measurements, -1d, preference, position);

		NavResultResponse response = proxy.findBetterDynamicRoute(tc, request);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (result.isSuccess())
		{
			// maybe there are no better route
			// assertNotNull(result.getAlternateRoutePaths());
			// assertNotNull(result.getPreferredAlternateRoutePath());
		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findDynamicAlternateRoute(com.telenav.kernel.util.datatypes.TnContext, com.telenav.cserver.backend.navstar.DynamicAlternateRouteRequest)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindDynamicAlternateRoute() throws ThrottlingException
	{
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		routePathId = 814790740;
		int segIndex = 0;
		int edgeIndex = 0;
		int pointIndex = 0;
		float offset = 0f;
		RoutePosition position = new RoutePosition(0, routePathId, segIndex,
				edgeIndex, pointIndex, offset);

		List measurements = NavstarDataConverter.convertGPSDataListToNavMeasList(
				getGpsDataList(), false);
		DynamicAlternateRouteRequest request = new DynamicAlternateRouteRequest(
				tc, measurements, -1d, preference, position);

		NavResultResponse response = proxy.findDynamicAlternateRoute(tc,
				request);
		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		if (result.isSuccess())
		{

		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findStaticAlternateRoute(com.telenav.kernel.util.datatypes.TnContext, com.telenav.cserver.backend.navstar.StaticAlternateRouteRequest)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindStaticAlternateRoute() throws ThrottlingException
	{
		// no need start session in this case;
		ProxyAddress dest = getDestProxyAddress();
		ProxyAddress original = getOriginalProxyAddress();
		NavResultResponse response = proxy.findRoute(tc, dest, original,
				preference);

		assertNotNull(response);
		NavResult result = response.getNavResult();
		assertNotNull(result);
		logger.debug(getNavResultMessage(result.getStatus()));
		assertTrue(result.isSuccess());

		preference.setFilters(NavPreference.AVOID_TRAFFIC);

		StaticAlternateRouteRequest request = new StaticAlternateRouteRequest(
				tc, original, preference, 0);

		response = proxy.findStaticAlternateRoute(tc, request);
		assertNotNull(response);
		result = response.getNavResult();
		assertNotNull(result);
		if (result.isSuccess())
		{

		} else
		{
			fail(getNavResultMessage(result.getStatus()));
		}
	}

	/**
	 * Test method for
	 * {@link com.telenav.cserver.backend.navstar.NavstarServiceProxy#findTrafficId(com.telenav.kernel.util.datatypes.TnContext, int, int, int)}
	 * .
	 * 
	 * @throws ThrottlingException
	 */
	public void testFindTrafficId() throws ThrottlingException
	{
		int routePathId = startDynamicRouteAndReturnRoutePathId();
		int segIndex = 0;
		int edgeIndex = 0;

		String trafficId = proxy.findTrafficId(tc, routePathId, segIndex,
				edgeIndex);
		assertNotNull(trafficId);
	}

}
