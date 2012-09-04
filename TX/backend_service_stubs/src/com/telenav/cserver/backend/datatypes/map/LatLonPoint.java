/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * proxy class for {@link com.televigation.proxycommon.LatLonPoint}
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class LatLonPoint
{
	private double lat;
	
	private double lon;
	
	

	/**
	 * default constructor
	 */
	public LatLonPoint()
	{
	}

	/**
	 * @param lat2
	 * @param lon2
	 */
	public LatLonPoint(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * @return the lat
	 */
	public double getLat()
	{
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat)
	{
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon()
	{
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon)
	{
		this.lon = lon;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
	
	
}
