/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.struts.util;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.televigation.mapproxy.MapserverProxy;
import com.televigation.mapproxy.datatypes.mapserverstatus.RGCStatus;
import com.televigation.proxycommon.LatLonPoint;
import com.televigation.proxycommon.RGCAddress;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-9-11
 */
public class RGCUtil {
    
	private Logger logger = Logger.getLogger(RGCUtil.class);
    public final static String SERVICE_MAPSERVER = "MAPSERVER";
    public static final double DEGREE_MULTIPLIER = 1.e5;
    
    /**
     * 
     * @param slat
     * @param slon
     * @param tc
     * @return
     */
    public Stop getCurrentLocation(int slat, int slon, TnContext tc)
    {
        double lat = PoiUtil.convertToDegree(slat);
        double lon = PoiUtil.convertToDegree(slon);
        LatLonPoint latLonPoint = new LatLonPoint(lat,lon);
        Stop currentLoc = new Stop();
        double radius = 0.2;
        try
        {
            RGCStatus rgcStatus = queryRGC(latLonPoint, radius, tc);
            if(rgcStatus != null && rgcStatus.status == RGCStatus.S_OK 
                    && rgcStatus.addresses != null && rgcStatus.addresses.size() > 0)
            {
                RGCAddress rgcAddress = (RGCAddress)rgcStatus.addresses.get(0);
                currentLoc.firstLine = rgcAddress.getFirstLine();
                currentLoc.city = rgcAddress.getCity();
                currentLoc.state = rgcAddress.getState();
                currentLoc.country = rgcAddress.getCounty();
                currentLoc.zip = rgcAddress.getZip();
                currentLoc.lat = convertToDM5(rgcAddress.getLat());
                currentLoc.lon = convertToDM5(rgcAddress.getLon());
            }
            else
            {
                currentLoc = new Stop();
                currentLoc.firstLine = "";
                currentLoc.city = "";
                currentLoc.state = "";
                currentLoc.zip = "";
                currentLoc.lat = slat;
                currentLoc.lon = slon;
                currentLoc.isGeocoded = true;
                currentLoc.country = "";
                currentLoc.label = "unknow";
            }
            
        }
        catch(Exception e)
        {
        	if(e instanceof ThrottlingException){
        		logger.error("failed to queryRGC from RGC, stoplat:"+slat+",stoplon:"+slon);
        	}
            e.printStackTrace();
        }
        
        return currentLoc;
    }
    
    /**
     * 
     * @param latLonPoint
     * @param radius
     * @param tnContext
     * @return
     * @throws ThrottlingException
     */
    public static RGCStatus queryRGC(LatLonPoint latLonPoint, double radius, TnContext tnContext)throws ThrottlingException
    {
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_MAPSERVER, tnContext);
            if(!startAPICall)
            {
                throw new ThrottlingException();
            }
            RGCStatus rgcStatus = new MapserverProxy(tnContext, null, null).queryRGC(latLonPoint, radius);
            return rgcStatus;
        }
        finally
        {
            if(startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_MAPSERVER, tnContext);
            }
        }
    }
    
    /**
     * 
     * @param degree
     * @return
     */
    public static int convertToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
}
