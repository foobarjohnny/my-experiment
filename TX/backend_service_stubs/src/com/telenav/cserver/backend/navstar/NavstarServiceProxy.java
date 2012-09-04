/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.util.List;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.NavFactory;
import com.telenav.navstar.proxy.NavResult;
import com.telenav.navstar.proxy.NavStarProxy;
import com.telenav.navstar.proxy.facade.NavPreference;
import com.telenav.navstar.proxy.traffic.TrafficIdResponse;
import com.televigation.proxycommon.ProxyAddress;

/**
 * Invoke navstar functions from NavstarProxy <li>
 * this class encapsulate the
 * <code>com.telenav.navstar.proxy.NavStarProxy</code> in the navstar-proxy*.jar
 * </li>
 * 
 * @author mmwang@telenav.cn
 * @version 1.0 2010-7-13
 * 
 */
public class NavstarServiceProxy
{

	/** scaling constants to convert KM to 1e-5 degrees */
	public static final double SCALING_CONSTANT = 1.113194908;// degrees to
	
	public static final int TIME_MULTIPLIER = 10; // 10 ms steps
	public static final int MS_IN_SEC = 1000;
	
	public final static String SERVICE_NAVSTAR = "SERVICE_NAVSTAR";

	private static NavstarServiceProxy instance = new NavstarServiceProxy(); // singleton

	private NavstarServiceProxy()
	{
		// can't instance out the class
	}

	/**
	 * It's a factory method, only could get this class's instance in this way
	 * 
	 * @return
	 */
	public static NavstarServiceProxy getInstance()
	{
		return instance;
	}

	public NavResultResponse findRoute(TnContext tc, List measurements,
			double latchedHeading, NavPreference preference)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.findRoute(tc, measurements, latchedHeading,
					preference);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findRoute(TnContext tc, ProxyAddress origin,
			NavPreference preference) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.findRoute(tc, origin, preference);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findRoute(TnContext tc, ProxyAddress destination,
			ProxyAddress origin, NavPreference preference)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy
					.findRoute(tc, destination, origin, preference);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse retrieveLastResult(TnContext tc, int routeId,
			int routePathId) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.retrieveLastResult(tc, routeId, routePathId);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse retrieveSegmentsFromSession(TnContext tc,
			int routeId, int routePathId, int startSegIndex, int endSegIndex,
			boolean bWithPoints) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();

			NavResult res = proxy.retrieveSegmentsFromSession(tc, routeId,
					routePathId, startSegIndex, endSegIndex, bWithPoints);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse selectAlternateRoute(TnContext tc)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.selectAlternateRoute(tc);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse startSession(TnContext tc, ProxyAddress destination)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.startSession(tc, destination);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse calculateDelayTime(TnContext tc, int routeId,
			int routePathId, int segIndex, int edgeIndex, int pointIndex,
			float offset) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.calculateDelayTime(tc, routeId, routePathId,
					segIndex, edgeIndex, pointIndex, offset);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse getSPTByIndex(TnContext tc, int routeId,
			int routePathId, int edgeStartIndex, int edgeEndIndex)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.getSPTByIndex(tc, routeId, routePathId,
					edgeStartIndex, edgeEndIndex);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findReverseRoute(TnContext tc)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				// can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = proxy.findReverseRoute(tc);
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public TrafficSummaryResponse getTrafficSummary(TnContext tc,
			TrafficSummaryRequest request) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			// modified by mmwang on 2010-07-13. hide the navstar response from
			// invoker
			com.telenav.navstar.proxy.traffic.TrafficSummaryResponse response = proxy
					.getTrafficSummary(request.getTrafficSummaryRequest());
			if (response != null)
			{
				return new TrafficSummaryResponse(response);
			}
			return null;
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public TrafficAlertResponse getTrafficAlerts(TnContext tc,
			TrafficAlertRequest request) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			com.telenav.navstar.proxy.traffic.TrafficAlertResponse response = proxy
					.getTrafficAlerts(request.getTrafficAlertRequest());
			if (response != null)
			{
				return new TrafficAlertResponse(response);
			}
			return null;
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findBetterDynamicRoute(TnContext tc,
			BetterDynamicRouteRequest betterdynamicrouterequest)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = (NavResult) proxy
					.findBetterDynamicRoute(betterdynamicrouterequest
							.getRequest());
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findDynamicAlternateRoute(TnContext tc,
			DynamicAlternateRouteRequest dynamicalternaterouterequest)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = (NavResult) proxy
					.findDynamicAlternateRoute(dynamicalternaterouterequest
							.getRequest());
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public NavResultResponse findStaticAlternateRoute(TnContext tc,
			StaticAlternateRouteRequest staticalternaterouterequest)
			throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			NavResult res = (NavResult) proxy
					.findStaticAlternateRoute(staticalternaterouterequest
							.getRequest());
			return new NavResultResponse(res);
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}

	public String findTrafficId(TnContext tc, int routePathId,
			int segmentIndex, int edgeIndex) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_NAVSTAR, tc);
			if (!startAPICall)
			{
				throw new ThrottlingException();
			}
			NavStarProxy proxy = NavFactory.createNavStarProxy();
			TrafficIdResponse res = proxy.findTrafficId(tc, routePathId,
					segmentIndex, edgeIndex);
			String trafficID = "";
			if (res != null && res.getMfId() != null
					&& res.getMfId().length() > 0)
			{
				trafficID = res.getMfId();
			}
			return trafficID;
		} finally
		{
			if (startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_NAVSTAR, tc);
			}
		}
	}
}
