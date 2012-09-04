package com.telenav.cserver.entity;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.Address;

/**
 * 
 *AirportPoi.java
 * 
 * @author
 */
@SuppressWarnings("unchecked")
public class AirportPoi implements Comparable
{
    private TnPoi poi;

    private double lat;

    private double lon;

    private double refLat;

    private double refLon;

    public AirportPoi(TnPoi poi)
    {
        this.poi = poi;
        latLon();
        refLat = 0d;
        refLon = 0d;
    }

    public AirportPoi(TnPoi poi, double refLat, double refLon)
    {
        this.poi = poi;
        latLon();
        this.refLat = refLat;
        this.refLon = refLon;
    }

    public int compareTo(Object o)
    {
        if (o instanceof AirportPoi)
        {
            return compareTo((AirportPoi) o);
        }
        throw new ClassCastException();
    }

    public int compareTo(AirportPoi ap)
    {
        double result = calculate() - ap.calculate();
        if (result > 0)
        {
            return 1;
        } else if (result < 0)
        {
            return -1;
        }
        return 0;
    }

    public TnPoi getPoi()
    {
        return poi;
    }

    public Address getAddress()
    {
        return poi.getAddress();
    }

    private void latLon()
    {
        Address addr = poi.getAddress();
        lat = addr.getLatitude();
        lon = addr.getLongitude();
    }

    private double calculate()
    {
        return (lat - refLat) * (lat - refLat) + (lon - refLon) * (lon - refLon);
    }

}
