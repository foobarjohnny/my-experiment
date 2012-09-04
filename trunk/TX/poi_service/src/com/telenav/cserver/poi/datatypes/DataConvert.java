package com.telenav.cserver.poi.datatypes;

import com.telenav.j2me.framework.protocol.ProtoStop;

public class DataConvert
{
    public static Stop convertToStop(com.telenav.j2me.datatypes.Stop stop)
    {
        Stop resultStop = new Stop();
        resultStop.city = stop.city;
        resultStop.country = stop.country;
        resultStop.county = stop.county;
        resultStop.firstLine = stop.firstLine;
        resultStop.isGeocoded = stop.isGeocoded;
        resultStop.isShareAddress = stop.isShareAddress;
        resultStop.label = stop.label;
        resultStop.lat = stop.lat;
        resultStop.lon = stop.lon;
        resultStop.state = stop.state;
        resultStop.stopId = stop.stopId;
        //resultStop.street
        //resultStop.type = stop.
        //resultStop.street2
        resultStop.zip = stop.zip;
        
        
        
        return resultStop;
        
    }
    
    public static Stop convertToStop(ProtoStop stop)
    {
        Stop resultStop = new Stop();
        resultStop.city = stop.getCity();
        resultStop.country = stop.getCountry();
        resultStop.county = stop.getCounty();
        resultStop.firstLine = stop.getFirstLine();
        resultStop.isGeocoded = stop.getIsGeocoded();
        resultStop.isShareAddress = stop.getIsShareAddress();
        resultStop.label = stop.getLabel();
        resultStop.lat = stop.getLat();
        resultStop.lon = stop.getLon();
        resultStop.state = stop.getState();
        resultStop.stopId = stop.getStopId();
        resultStop.zip = stop.getZip();
        return resultStop;
        
    }
}
