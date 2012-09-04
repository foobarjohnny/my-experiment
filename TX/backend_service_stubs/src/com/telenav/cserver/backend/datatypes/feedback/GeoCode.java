/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

/**
 * GeoCode 
 * @author zhjdou
 *
 */
public class GeoCode
{
    private double latitude;

    private double longitude;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("GeoCode=[");
        sb.append("latitude=").append(this.latitude);
        sb.append(", longitude=").append(this.longitude);
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * @return the latitude
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

}
