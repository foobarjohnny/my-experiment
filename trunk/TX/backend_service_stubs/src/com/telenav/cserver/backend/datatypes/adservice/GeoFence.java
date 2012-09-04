/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.adservice;


/**
 * GeoFence
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-3-16
 */
public class GeoFence
{
    private double distance = 1;
    private int lat = 2;
    private int lon = 3;
    public double getDistance()
    {
        return distance;
    }
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    public int getLat()
    {
        return lat;
    }
    public void setLat(int lat)
    {
        this.lat = lat;
    }
    public int getLon()
    {
        return lon;
    }
    public void setLon(int lon)
    {
        this.lon = lon;
    }
    
}
