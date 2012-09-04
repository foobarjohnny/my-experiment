 /**
  * (c) Copyright 2010 TeleNav.
  *  All Rights Reserved.
  */

package com.telenav.cserver.backend.datatypes;

import com.telenav.j2me.datatypes.Stop;


/**
 * data type converter
 * 
 * @author jzhu 2010-12-01
 */
public class DataConvert
{
    private static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
    
    public static int convertToDM5(double degree)
    {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
    
    public static Stop convertToStop(TnPoi tnPoi)
    {
        if (tnPoi == null)
            return null;
        
        Stop stop = new Stop();
        
        Address addr = tnPoi.getAddress();
        stop.firstLine = tnPoi.getBrandName();
        if (addr != null)
        {
            stop.city = addr.getCityName();
            stop.state = addr.getState();
            stop.zip = addr.getPostalCode();
            stop.lat = convertToDM5(addr.getLatitude());
            stop.lon = convertToDM5(addr.getLongitude());
            stop.country=addr.getCountry();
            stop.county=addr.getCounty();
            stop.zip=addr.getPostalCode();
            stop.isGeocoded = true;
        }
        
        return stop;
    }
}
