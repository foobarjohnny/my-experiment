/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.image;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class MapTileRequest
{

	private String mapType;
	
	private long x;
	
	private long y;
	
	private int zoom;
	
	private int pixel;

	/**
	 * @param mapType
	 * @param x
	 * @param y
	 * @param zoom
	 * @param version
	 * @param pixel
	 */
	public MapTileRequest(String mapType, long x, long y, int zoom, int pixel)
	{
		this.mapType = mapType;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.pixel = pixel;
	}
	
	/**
	 * default constructor
	 */
	public MapTileRequest()
	{
	}

	/**
	 * @return the mapType
	 */
	public String getMapType()
	{
		return mapType;
	}

	/**
	 * @param mapType the mapType to set
	 */
	public void setMapType(String mapType)
	{
		this.mapType = mapType;
	}

	/**
	 * @return the x
	 */
	public long getX()
	{
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(long x)
	{
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public long getY()
	{
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(long y)
	{
		this.y = y;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom()
	{
		return zoom;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(int zoom)
	{
		this.zoom = zoom;
	}

	/**
	 * @return the pixel
	 */
	public int getPixel()
	{
		return pixel;
	}

	/**
	 * @param pixel the pixel to set
	 */
	public void setPixel(int pixel)
	{
		this.pixel = pixel;
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
