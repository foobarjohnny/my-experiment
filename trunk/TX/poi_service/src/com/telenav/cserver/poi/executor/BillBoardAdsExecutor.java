/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.adservice.AdServiceProxy;
import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.util.JSONUtil;
import com.telenav.cserver.framework.util.UrlUtil;
import com.telenav.cserver.poi.struts.util.NSPoiUtil;
import com.telenav.cserver.resource.common.ServiceLocatorHolder;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.cserver.resource.manager.ServiceMappingManager;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * POISearchExecutor_WS.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-3-15
 */
public class BillBoardAdsExecutor extends AbstractExecutor
{
    
    static Logger logger = Logger.getLogger(BillBoardAdsExecutor.class);
    
    private static final int MAX_SEARCH_DIST = 25000;
    
    static ServiceLocatorHolder holder=(ServiceLocatorHolder)ResourceHolderManager.getResourceHolder("service_locator");
    
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("BillBoardAdsExecutor");

		BillBoardAdsRequest request = (BillBoardAdsRequest) req;
		BillBoardAdsResponse response = (BillBoardAdsResponse) resp;
		
        TnContext tc = context.getTnContext();

        UserProfile userProfile = req.getUserProfile();
        req.getGpsData();
        TnUtil.getDSMDataFromCServer(tc, userProfile);

        
        long routeId = request.getRouteId();
        
        

        int segmentId = 0;
        int edgeId = 0;
        int shapePointId = 0;
        int range = MAX_SEARCH_DIST;
        
        List<GeoCode> routePoints;
        try
        {
            List<GpsData> gpsDataList = request.getGpsData();
            if (gpsDataList == null || gpsDataList.size() < 1)
            {
                cli.addData("gps", "no gps data");
                response.setStatus(ExecutorResponse.STATUS_FAIL);
                return;
            }
            
            GpsData gpsData = gpsDataList.get(0);

            //cli.addData("parm", "lat=" + gpsData.lat + ", lon="+gpsData.lon + ",routeId=" + routeId);
            if(logger.isDebugEnabled())
            {
                logger.debug("gps=" + gpsData + "&routeId=" + routeId);
            }
            
            routePoints = NSPoiUtil.getRoutePoints(tc, (int)routeId, 
                            segmentId, edgeId, shapePointId, range,  gpsData.lat, gpsData.lon);
            
            //if routePoints is empty, WS will throws exception
            if (routePoints == null || routePoints.size() == 0)
            {
                cli.addData("route points", "NULL");
                GeoCode geoCode = new GeoCode();
                geoCode.setLatitude(((double)gpsData.lat)/1.0e5);
                geoCode.setLongitude( ((double)gpsData.lon)/1.0e5 );
                routePoints.add(geoCode);
            }
            
            //cli.addData("route points", String.valueOf(routePoints.size()));
            
            List<BillBoardAds> billBoardAdsList = AdServiceProxy.getInstance().getBillBoardAds(routeId, gpsData, routePoints, tc);
            
            for(BillBoardAds ads : billBoardAdsList){
                ResourceContent rs = holder.getResourceContent(userProfile, context.getTnContext());
                ServiceMapping serviceMapping =(ServiceMapping)rs.getObject();
                String url = ServiceMappingManager.getUrlByKey(serviceMapping, request.getUserProfile(), "poi");
                String[] hostAndPort = UrlUtil.getHostAndPort(url);
                ads.setAdsUrl(makeAdsUrl(hostAndPort[0], hostAndPort[1], ads, userProfile));
                if( logger.isDebugEnabled() ){
                    logger.debug("adsUrl = " + ads.getAdsUrl());
                }
                
            }
            
            response.setBillBoardAdsList(billBoardAdsList);
            
        }
        catch (Exception e)// handle the exception
        {
            logger.fatal("BillBoardAdsExecutor", e);
            cli.setStatus(e);
            response.setStatus(ExecutorResponse.STATUS_FAIL);
            //response.setErrorMessage(messages.get("ERROR_FEEDBACK_SERVICE"));
        }
        finally
        {
            cli.complete();
        }
	}
	
	
	private String makeAdsUrl(String host, String port, BillBoardAds ads, UserProfile userProfile){
	    StringBuffer url = new StringBuffer("http://");
	    url.append(host).append(":").append(port).append("/poi_service/html/adsinfo.do?adsId=").append(ads.getAdsId());
	    return addUserProfileToUrl(url.toString()
	                        ,userProfile);
	}
	
	private String addUserProfileToUrl(String url, UserProfile userProfile){
	    return UrlUtil.processUrl(url, userProfile);
	}

	
}

