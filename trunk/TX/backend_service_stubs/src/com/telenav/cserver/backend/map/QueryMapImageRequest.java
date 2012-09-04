/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import java.util.Vector;

import com.telenav.cserver.backend.datatypes.map.LatLonPoint;
import com.televigation.mapproxy.ImageSettings;

/**
 * proxy class for {@link ImageSettings}
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class QueryMapImageRequest
{
	public final static String IMGSETTINGS_DEFAULT = ImageSettings.IMGSETTINGS_DEFAULT;
	
	public final static String IMGSETTINGS_PHONE_COURSE = ImageSettings.IMGSETTINGS_PHONE_COURSE;
	
	public final static String IMGSETTINGS_PHONE_FINE = ImageSettings.IMGSETTINGS_PHONE_FINE;

	public final static String IMGSETTINGS_PHONE_VIEW = ImageSettings.IMGSETTINGS_PHONE_VIEW;
	
	public final static String IMGSETTINGS_PHONEVIEW = ImageSettings.IMGSETTINGS_PHONEVIEW;
	
	public final static String IMGSETTINGS_TELENAV_MAP = ImageSettings.IMGSETTINGS_TELENAV_MAP;
	
	public final static String IMGSETTINGS_TELENAV_WEB = ImageSettings.IMGSETTINGS_TELENAV_WEB;
	
	public final static String FORMAT_DEFAULT = ImageSettings.FORMAT_DEFAULT;
	
	public final static String FORMAT_GIF = ImageSettings.FORMAT_GIF;
	
	public final static String FORMAT_JPG = ImageSettings.FORMAT_JPG;
	
	public final static String FORMAT_PNG = ImageSettings.FORMAT_PNG;
	
	private LatLonPoint maxCorner;
	
	private LatLonPoint minCorner;
	
	private int width;
	
	private int height;
	
	private Vector routePoints;
	
	private Vector alterRoutePoints;
	
	private String viewType;
	
	private String format;


	/**
	 * @param minCorner
	 * @param maxCorner
	 * @param width
	 * @param height
	 * @param routePoints
	 * @param alterRoutePoints
	 * @param viewType
	 * @param format
	 */
	public QueryMapImageRequest(LatLonPoint minCorner, LatLonPoint maxCorner,
			int width, int height, Vector routePoints, Vector alterRoutePoints,
			String viewType, String format)
	{
		this.minCorner = minCorner;
		this.maxCorner = maxCorner;
		this.width = width;
		this.height = height;
		this.routePoints = routePoints;
		this.viewType = viewType;
		this.format = format;
		this.alterRoutePoints = alterRoutePoints;
	}



	/**
	 * 
	 */
	public QueryMapImageRequest()
	{
	}

	/**
	 * @return the maxCorner
	 */
	public LatLonPoint getMaxCorner()
	{
		return maxCorner;
	}

	/**
	 * @param maxCorner the maxCorner to set
	 */
	public void setMaxCorner(LatLonPoint maxCorner)
	{
		this.maxCorner = maxCorner;
	}

	/**
	 * @return the minCorner
	 */
	public LatLonPoint getMinCorner()
	{
		return minCorner;
	}

	/**
	 * @param minCorner the minCorner to set
	 */
	public void setMinCorner(LatLonPoint minCorner)
	{
		this.minCorner = minCorner;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the routePoints
	 */
	public Vector getRoutePoints()
	{
		return routePoints;
	}

	/**
	 * @param routePoints the routePoints to set
	 */
	public void setRoutePoints(Vector routePoints)
	{
		this.routePoints = routePoints;
	}

	/**
	 * @return the viewType
	 */
	public String getViewType()
	{
		return viewType;
	}

	/**
	 * @param viewType the viewType to set
	 */
	public void setViewType(String viewType)
	{
		this.viewType = viewType;
	}

	/**
	 * @return the format
	 */
	public String getFormat()
	{
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}

	/**
	 * @return the alterRoutePoints
	 */
	public Vector getAlterRoutePoints()
	{
		return alterRoutePoints;
	}

	/**
	 * @param alterRoutePoints the alterRoutePoints to set
	 */
	public void setAlterRoutePoints(Vector alterRoutePoints)
	{
		this.alterRoutePoints = alterRoutePoints;
	}
}
