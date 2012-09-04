package com.telenav.cserver.poi.struts.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.apontador.apirequest.data.ApontadorPoint;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.common.resource.ResourceUtil;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.NavResult;
import com.telenav.navstar.proxy.facade.LatLon;
import com.telenav.navstar.proxy.facade.NavEdge;
import com.telenav.navstar.proxy.facade.NavRoute;
import com.telenav.navstar.proxy.facade.NavRoutePath;
import com.telenav.navstar.proxy.facade.NavSegment;
import com.telenav.navstar.proxy.facade.NavStarStatusCode;

/**
 * for search along to get points 
 * @author chbzhang
 *
 */
public class NSPoiUtil {
    public static final int MAX_SEARCH_DIST = 25000;
    private static final int MAX_SEARCH_DIST_ONEBOX = 8000; // 5 miles
    private static final double DEGREE_MULTIPLIER = 1.e5; 
    
    static Logger logger = Logger.getLogger(NSPoiUtil.class);
    
    
    public static List<ApontadorPoint> getRoutePointsDistnaceApart(TnContext tc, int routePathId, int segmentId, int edgeId, 
			int shapePointId, int range, int lat, int lon,int radius,int maxDistance) throws ThrottlingException
	{
		double distanceApart = radius*2;
		List<ApontadorPoint> points = new ArrayList<ApontadorPoint>();
		ApontadorPoint p = new ApontadorPoint(new LatLon(lat, lon),radius);
        points.add(p);
        

		NavResult res = NavstarStandaloneOperator.retrieveLastResult(tc, 0, routePathId);
		if(res.getStatus().getStatusCode().getStatusId() == NavStarStatusCode.UNSYNC_CLIENT_SERVER_STATE.getStatusId())
		{
			logger.error("client and server route state unsync:" + routePathId);
			return null;
		}
		NavRoutePath routePath = NSNavStarUtil.fetchActiveRoutePath(res, routePathId);
		if (routePath == null)
		{
			logger.error("route not found in session:" + routePathId);
			return null;
		}

		double distance = 0;
		double totalDistance = 0;
		boolean reachMaxDistance=false;
		boolean flag = false;
		for(int i = segmentId; i < routePath.getSegments().size()&&!reachMaxDistance; i++)
		{
			NavSegment segment = (NavSegment)routePath.getSegments().get(i);
			int j = (i == segmentId ? edgeId : 0);
			for(; j < segment.getEdges().size()&&!reachMaxDistance; j++)
			{
				NavEdge edge = (NavEdge)segment.getEdges().get(j);
                List pts = edge.getPoints();
				
				int k = flag ? 0 : shapePointId;
				
				for(; k < pts.size() -1&&!reachMaxDistance; k++)
				{
					if(!flag)
					{
						flag = true;
					}
					LatLon point1 = (LatLon)pts.get(k);
					LatLon point2 = (LatLon)pts.get(k + 1);
					int pointDistance = (int)caculateDistance(point1, point2);
					distance += pointDistance;
					totalDistance+=pointDistance;
					
					if(totalDistance>maxDistance)
					{
						reachMaxDistance=true;
					}
					
					if(pointDistance > distanceApart){
						points.add(new ApontadorPoint(point1, pointDistance));
						distance = 0.0;
					}else{
						if (distance == distanceApart)
						{
							points.add(new ApontadorPoint(point1, radius));
							distance=0.0;
						}else if(distance>distanceApart && k>0){
							LatLon prevPoint = (LatLon)pts.get(k-1);
							points.add(new ApontadorPoint(prevPoint, radius));
							distance=0.0;
						}
					}
				}
			}
		}
		
		logger.debug("Print shape points:" + points.size());
		for (int i = 0; i < points.size(); i++)
		{
			LatLon point = (LatLon) points.get(i).getPoint();
			logger.debug(point.lat() + "," + point.lon());
		}
		logger.debug("-------------");
		return points;
	}
    
    public static List<ApontadorPoint> getReverseRoutePointsDistnaceApart(TnContext tc,PoiSearchRequest poiReq, int routePathId, int range,int radius,int maxDistance) throws ThrottlingException
	{
		List<ApontadorPoint> points = new ArrayList<ApontadorPoint>();
	
		NavResult res = NavstarStandaloneOperator.retrieveLastResult(tc, 0, routePathId);
		if(res.getStatus().getStatusCode().getStatusId() == NavStarStatusCode.UNSYNC_CLIENT_SERVER_STATE.getStatusId())
		{
			logger.error("client and server route state unsync:" + routePathId);
			return null;
		}
		NavRoutePath routePath = NSNavStarUtil.fetchActiveRoutePath(res, routePathId);
		if (routePath == null)
		{
			logger.error("route not found in session:" + routePathId);
			return null;
		}
		
		Address anchor = new Address();
		if(routePath.getEndLocation() != null)
		{
			double dLat = routePath.getEndLocation().lat();
			double dLon = routePath.getEndLocation().lon();
			anchor.setLatitude(dLat);
			anchor.setLongitude(dLon);
			poiReq.setAnchor(anchor);
			
			points.add(new ApontadorPoint(routePath.getEndLocation(), radius));
		}

		double distanceApart = radius*2;
		double distance = 0;
		double totalDistance = 0;
		boolean reachMaxDistance=false;
		for(int i = routePath.getSegments().size()-1; i >=0&&!reachMaxDistance; i--)
		{
			NavSegment segment = (NavSegment)routePath.getSegments().get(i);
			for(int j=0; j < segment.getEdges().size()&&!reachMaxDistance; j++)
			{
				NavEdge edge = (NavEdge)segment.getEdges().get(j);
                List pts = edge.getPoints();
				for(int k=0; k < pts.size() -1&&!reachMaxDistance; k++)
				{
					LatLon point1 = (LatLon)pts.get(k);
					LatLon point2 = (LatLon)pts.get(k + 1);
					int pointDistance = (int)caculateDistance(point1, point2);
					distance += pointDistance;
					totalDistance+=pointDistance;
					
					if(totalDistance>maxDistance)
					{
						reachMaxDistance=true;
					}
					
					if(pointDistance > distanceApart){
						points.add(new ApontadorPoint(point1, pointDistance));
						distance = 0.0;
					}else{
						if (distance == distanceApart)
						{
							points.add(new ApontadorPoint(point1, radius));
							distance=0.0;
						}else if(distance>distanceApart && k>0){
							LatLon prevPoint = (LatLon)pts.get(k-1);
							points.add(new ApontadorPoint(prevPoint, radius));
							distance=0.0;
						}
					}
				}
			}
		}
		
		logger.debug("Print shape points:" + points.size());
		for (int i = 0; i < points.size(); i++)
		{
			LatLon point = (LatLon) points.get(i).getPoint();
			logger.debug(point.lat() + "," + point.lon());
		}
		logger.debug("-------------");
		return points;
	}
    
    /**
	 * Get shape points for the route, search MAX_SEARCH_DIST ahead of the route
	 */
	private static List<GeoCode> getRoutePoints(TnContext tc, int routePathId, int segmentId, int edgeId, 
			int shapePointId, int range, int lat, int lon,int maxDistance) throws ThrottlingException
	{
	    CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getRoutePoints");

        List<GeoCode> points = new ArrayList<GeoCode>();
        
        try
        {
    		GeoCode p = new GeoCode();
    		p.setLatitude(convertToDegree(lat));
    		p.setLongitude(convertToDegree(lon));
    		points.add(p);
            
    
    		NavResult res = NavstarStandaloneOperator.retrieveLastResult(tc, 0, routePathId);
    		
            if (res != null && res.getStatus()!=null)
            {
                String navMsg = res.getStatus().getMessage() + "_" + res.getStatus().getStatusID()+ "_" + res.getStatus().getCecCode();
                cli.addData("NavResult", navMsg);
            }
    		
    		if(res == null || res.getRoutes() == null || res.getRoutes().size() == 0)
    		{
    			logger.error("NavResult is null");
    			return points;
    		}
    			
    		if(!res.isSuccess() || res.getActiveRoutePaths() == null || res.getActiveRoutePaths().size() == 0)
    		{
    			logger.error("navstar return null routes.");
    			if(res.getStatus().getStatusCode().equals(NavStarStatusCode.UNSYNC_CLIENT_SERVER_STATE))
    			{
    				logger.error("client and server route state unsync:" + routePathId);
    			}
    			return points;
    		}
    		
    		NavRoutePath routePath = NSNavStarUtil.fetchActiveRoutePath(res, routePathId);
    		if (routePath == null)
    		{
    		    cli.addData("NavResult", "route not found in session:" + routePathId);
    			logger.error("route not found in session:" + routePathId);
    			return points;
    		}
    
    		double distance = -range;
    		boolean flag = false;
    //		logger.debug("vSegments.size:" + mapRoute.vSegments.size());
    		for(int i = segmentId; i < routePath.getSegments().size(); i++)
    		{
    			NavSegment segment = (NavSegment)routePath.getSegments().get(i);
    			int j = (i == segmentId ? edgeId : 0);
    			for(; j < segment.getEdges().size(); j++)
    			{
    				NavEdge edge = (NavEdge)segment.getEdges().get(j);
    				List pts = edge.getPoints();
    				
    //				Use decimated points for non first edges.
    				//[hb]:  no decimated or non-decimated diff points in current navstar.	
    				if(flag)
    				{
    //					pts = edge.decimatedPoints;
    				}
    				
    				int k = flag ? 0 : shapePointId;
    				
    				for(; k < pts.size() -1; k++)
    				{
    					if(!flag)
    					{
    						flag = true;
    					}
    					LatLon point1 = (LatLon)pts.get(k);
    					LatLon point2 = (LatLon)pts.get(k + 1);
    					distance += caculateDistance(point1, point2);
    					//logger.debug("dist:" + distance);
    					if (distance > maxDistance)
    					{
    						break;
    					}
    					GeoCode point = new GeoCode(point2.lat(), point2.lon());
    					points.add(point);
    				}
    			}
    		}
    		
    		cli.addData("points", "size="+points.size());
    		
    		logger.debug("Print shape points:" + points.size());
    		if(logger.isDebugEnabled())
    		{
    			for (int i = 0; i < points.size(); i++)
    			{
    				GeoCode point = (GeoCode) points.get(i);
    
    				logger.debug(point.getLatitude() + "," +point.getLongitude());
    			}
    		}
        }
        finally
        {
            cli.complete();
        }
        
		return points;
	}
	
    /**
	 * Get shape points for the route, search MAX_SEARCH_DIST ahead of the route
	 */
	public static List<GeoCode> getRoutePoints(TnContext tc, int routePathId, int segmentId, int edgeId, 
			int shapePointId, int range, int lat, int lon) throws ThrottlingException
	{
		return getRoutePoints(tc,routePathId,segmentId,edgeId,shapePointId,range,lat,lon,MAX_SEARCH_DIST);
	}

    /**
	 * Get shape points for the route, search MAX_SEARCH_DIST ahead of the route
	 */
	public static List<GeoCode> getRoutePointsForOneBox(TnContext tc, int routePathId, int segmentId, int edgeId, 
			int shapePointId, int range, int lat, int lon) throws ThrottlingException
	{
		return getRoutePoints(tc,routePathId,segmentId,edgeId,shapePointId,range,lat,lon,MAX_SEARCH_DIST_ONEBOX);
	}
	
	private void parseList(List list)
	{
		List reverseList = new ArrayList();
		int index = 0;
		double distance = 0;
		for(int i =list.size() - 1; i >= 0; i--)
		{
			Object obj = list.get(i);
			List subList;
			if(obj instanceof NavSegment)
			{ 
				NavSegment segment = (NavSegment)obj;
				subList = segment.getEdges();
				parseList(subList);
			}
			else if(obj instanceof NavEdge)
			{
				NavEdge edge = (NavEdge)obj;
				subList = edge.getPoints();
				parseList(subList);
			}
			else if(obj instanceof LatLon)
			{
				LatLon latLon = (LatLon)obj;
				LatLon lastLatLon = (LatLon)reverseList.get(index);
				distance += caculateDistance(latLon, lastLatLon);
				if (distance > MAX_SEARCH_DIST)
				{
					break;
				}
				index++;
				reverseList.add(latLon);
			}
		}
	}
	
	private static void collectReverseRoutePoints(PoiSearchRequest poiReq, NavRoutePath routePath, List<GeoCode> points, double distance)
	{
		if(routePath.getSegments() != null)
		{
			int segSize = routePath.getSegments().size();
			for(int i = segSize-1; i >= 0; i--)
			{
				NavSegment seg = (NavSegment)routePath.getSegments().get(i);
				if(seg.getEdges() != null)
				{
					int edgeSize = seg.getEdges().size();
					for(int j = edgeSize-1; j >= 0; j--)
					{
						NavEdge edge = (NavEdge)seg.getEdges().get(j);
						if(edge.getPoints() != null)
						{
							int pointSize = edge.getPoints().size();
							for(int k = pointSize-1; k > 0; k--)
							{
								LatLon point1 = (LatLon)edge.getPoints().get(k);
								LatLon point2 = (LatLon)edge.getPoints().get(k-1);
								distance += caculateDistance(point1, point2);
								if (distance > MAX_SEARCH_DIST)
								{
									break;
								}
								GeoCode point = new GeoCode(point2.lat(),point2.lon());
								points.add(point);
							}
						}
					}
				}
			}
			poiReq.setRoutePoints(points);
		}
	}
	public static void setReverseRoutePointsAndAnchor(TnContext tc, PoiSearchRequest poiReq, int routePathId, int range) throws ThrottlingException
	{
		List<GeoCode> points = new ArrayList<GeoCode>();
		
		NavResult res = NavstarStandaloneOperator.retrieveLastResult(tc, 0, routePathId);
		if(res == null || res.getRoutes() == null || res.getRoutes().size() == 0)
		{
			logger.error("NavResult is null");
			return;
		}
			
		if(!res.isSuccess() || res.getActiveRoutePaths() == null || res.getActiveRoutePaths().size() == 0)
		{
			logger.error("navstar return null routes.");
			if(res.getStatus().getStatusCode().equals(NavStarStatusCode.UNSYNC_CLIENT_SERVER_STATE))
			{
				logger.error("client and server route state unsync:" + routePathId);
			}
			return;
		}
		NavRoute route = (NavRoute)res.getRoutes().get(0);
		if(route != null)
		{
			Address anchor = new Address();
			if(route.getDestination() != null)
			{
				double dLat = route.getDestination().lat();
				double dLon = route.getDestination().lon();
				anchor.setLatitude(dLat);
				anchor.setLongitude(dLon);
				poiReq.setAnchor(anchor);
				
				GeoCode p = new GeoCode();
				p.setLatitude(dLat);
				p.setLongitude(dLon);
				points.add(p);
			}
			
		}
		NavRoutePath routePath = NSNavStarUtil.fetchActiveRoutePath(res, routePathId);
		if (routePath == null)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("route not found in session:" + routePathId);
			}
			return;
		}
		double distance = -range;
		collectReverseRoutePoints(poiReq,routePath, points, distance);
		
	}
    public static Vector getRoutePoints(int routePathId, int segmentId, int edgeId, 
            int shapePointId, int range, int lat, int lon) throws ThrottlingException
    {
        Vector points = new Vector();
        Point p = new Point(lat, lon);
        if(segmentId != 0 || edgeId != 0 || shapePointId != 0)
        {
            points.add(p);
        }
        TnContext tc = new TnContext();
        tc.addProperty(TnContext.PROP_LOGIN_NAME , "4085551234");
//        tc.addProperty(TnContext.PROP_LOGIN_NAME , "306029444");
        NavResult res = NavstarStandaloneOperator.retrieveLastResult(tc, 0, routePathId);
        if(res.getStatus().getStatusCode().equals(NavStarStatusCode.UNSYNC_CLIENT_SERVER_STATE))
        {
            return null;
        }
        NavRoutePath routePath = NSNavStarUtil.fetchActiveRoutePath(res, routePathId);
        if (routePath == null)
        {
            return null;
        }

        double distance = -range;
        boolean flag = false;
        for(int i = segmentId; i < routePath.getSegments().size(); i++)
        {
            NavSegment segment = (NavSegment)routePath.getSegments().get(i);
            int j = (i == segmentId ? edgeId : 0);
            for(; j < segment.getEdges().size(); j++)
            {
                NavEdge edge = (NavEdge)segment.getEdges().get(j);
                List pts = edge.getPoints();
                
                int k = flag ? 0 : shapePointId;
                
                for(; k < pts.size() -1; k++)
                {
                    if(!flag)
                    {
                        flag = true;
                    }
                    LatLon point1 = (LatLon)pts.get(k);
                    LatLon point2 = (LatLon)pts.get(k + 1);
                    distance += caculateDistance(point1, point2);
                    if (distance > MAX_SEARCH_DIST)
                    {
                        break;
                    }
                    Point point = new Point((int)(point2.lat() * DEGREE_MULTIPLIER), (int)(point2.lon() * DEGREE_MULTIPLIER));
                    points.add(point);
                }
            }
        }
        return points;
    }
    
    public static double convertToDegree(int dm5) {
        return dm5 / DEGREE_MULTIPLIER;
    }
    
    private static double caculateDistance(LatLon p1, LatLon p2)
    {
        double lat1 = p1.lat();
        double lon1 = p1.lon();
        double lat2 = p2.lat();
        double lon2 = p2.lon();
        return 100000 * Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (lon1 - lon2) * (lon1 - lon2));
    }
        
}
