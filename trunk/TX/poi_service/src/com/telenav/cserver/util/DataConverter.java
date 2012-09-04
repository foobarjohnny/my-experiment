/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.util;

import org.apache.log4j.Logger;

import com.telenav.j2me.datatypes.Stop;
import com.televigation.db.poi.Poi;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-6-12
 */
public class DataConverter {
	protected static Logger logger = Logger.getLogger(DataConverter.class);
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
    
    public static Poi convertStopToPoi(Stop stop)
    {
        logger.info(" stop = " + stop.firstLine + ", " + stop.city + ", " + stop.state + ", " + stop.lat + ", " + stop.lon + ", "
                + stop.stopId);
        com.televigation.db.poi.Poi dbPoi = new com.televigation.db.poi.Poi();
        
        dbPoi.setPostalCode(stop.zip);
        dbPoi.setCity(stop.city);
        dbPoi.setProvince(stop.state);
        dbPoi.setStreet(stop.firstLine);
        dbPoi.setLat(convertToDegree(stop.lat));
        dbPoi.setLon(convertToDegree(stop.lon));
        dbPoi.setCountry(stop.country);
        
        return dbPoi;
    }
    
    public static double convertToDegree(int dm5)
    {
        return dm5 / DEGREE_MULTIPLIER;
    }
    
    public static int convertToClientRating(double rating)
    {
        return (int) (rating * 10);
    }   
}
