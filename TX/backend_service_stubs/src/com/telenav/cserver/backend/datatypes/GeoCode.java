/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes;

/**
 * GeoCode.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-17
 */
public class GeoCode
{
	private double latitude;
	private double longitude;
	private String geoCodeSource;
	
	public String toString() {
	    StringBuilder sb=new StringBuilder();
	    sb.append("GeoCode=[");
	    sb.append("latitude=");
	    sb.append(this.latitude);
	    sb.append(", longitude=");
	    sb.append(this.longitude);
	    sb.append("geoCodeSource=");
	    sb.append(this.geoCodeSource);
	    sb.append("]");
	    return sb.toString();
	}
	
	public GeoCode()
	{
		
	}
	public GeoCode(double lati, double longi)
	{
		this.latitude = lati;
		this.longitude = longi;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}
	/**
	 * @param geoCodeSource the geoCodeSource to set
	 */
	public void setGeoCodeSource(String geoCodeSource)
	{
		this.geoCodeSource = geoCodeSource;
	}
	/**
	 * @return the geoCodeSource
	 */
	public String getGeoCodeSource()
	{
		return geoCodeSource;
	}
}
