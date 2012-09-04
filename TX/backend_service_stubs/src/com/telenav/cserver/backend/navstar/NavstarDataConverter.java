/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.datatypes.navstar.TrafficIncidentFilter;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.navstar.proxy.facade.NavMeasurement;
import com.telenav.navstar.proxy.facade.NavRoadSpeed;
import com.telenav.navstar.proxy.facade.Unit;
import static com.telenav.cserver.backend.datatypes.Constants.*;

/**
 * a util class for convert client data to navstar data
 *
 * @author mmwang
 * @version 1.0 2010-7-14
 * 
 */
public class NavstarDataConverter
{
	private static Logger logger = Logger.getLogger(NavstarDataConverter.class);
	
	public static List<NavMeasurement> convertGPSDataListToNavMeasList(List<GpsData> gpsDataList, boolean needEncrypt)
	{
		List<NavMeasurement> meas = new ArrayList<NavMeasurement>();
		for(GpsData gps: gpsDataList)
		{
			meas.add(convertGPSDataToNavMeasurement(gps, needEncrypt));
		}
		return meas;
	}
	public static NavMeasurement convertGPSDataToNavMeasurement(GpsData gpsData, boolean needEncrypt)
	{
        int encGps [] = new int [2];
        encGps[0] = gpsData.lat;
        encGps[1] = gpsData.lon;
        if(logger.isDebugEnabled())
        {
        	logger.debug("mea:" + gpsData.lat + "," + gpsData.lon + "," + gpsData.speed + "," + gpsData.timeTag + "," + gpsData.heading + "," + gpsData.errSize);
            
        }
        if(needEncrypt)
        {
            encGps = GpsEncryptor.getInstance().encryptGps(encGps[0], encGps[1], (int)(gpsData.timeTag * NavstarServiceProxy.TIME_MULTIPLIER / NavstarServiceProxy.MS_IN_SEC));
            if(logger.isDebugEnabled())
            {
            	 logger.debug("enc mea:" + encGps[0] + "," + encGps[1]);
            }
        }
        double dLat = encGps[0] / DEGREE_MULTIPLIER;
        double dLon = encGps[1] / DEGREE_MULTIPLIER;
        double heading = gpsData.heading;
        double error = gpsData.errSize * NavstarServiceProxy.SCALING_CONSTANT;
        long time = gpsData.timeTag * NavstarServiceProxy.TIME_MULTIPLIER / NavstarServiceProxy.MS_IN_SEC;
        //[hb]: speed unit is meter/sec
        NavRoadSpeed speed = new NavRoadSpeed((gpsData.speed) * NavstarServiceProxy.SCALING_CONSTANT / 10,Unit.Speed.MPS);
        NavMeasurement mea = new NavMeasurement(dLat,dLon,speed,heading,error,time,time);

		return mea;
	}
	
	/**
	 * convert backend's TrafficIncidentFilter to navstar proxy's TrafficIncidentFilter
	 * @param filter
	 * @return
	 */
	public static com.telenav.navstar.proxy.traffic.TrafficIncidentFilter convertTrafficIncidentFilter(TrafficIncidentFilter filter) {
		if (filter != null) {
			com.telenav.navstar.proxy.traffic.TrafficIncidentFilter result = new com.telenav.navstar.proxy.traffic.TrafficIncidentFilter();
			result.setShowSpeedTraps(filter.isShowSpeedTraps());
			result.setShowTrafficCameras(filter.isShowTrafficCameras());

			return result;
		}
		
		return null;
	}
}
