package com.telenav.cserver.misc.struts.action;

import com.telenav.datatypes.traffic.common.v10.AlertDTO;


public class TrafficAlert
{
    private int lat;
    private int lon;
    private AlertDTO alertDTO;

    TrafficAlert(int lat, int lon, AlertDTO alertDTO)
    {
        this.lat = lat;
        this.lon = lon;
        this.alertDTO = alertDTO;
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
    public AlertDTO getAlertDTO()
    {
        return alertDTO;
    }
    public void setAlertDTO(AlertDTO alertDTO)
    {
        this.alertDTO = alertDTO;
    }
    
}
