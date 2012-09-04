/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import java.util.Vector;

import com.telenav.cserver.backend.datatypes.map.LatLonPoint;

/**
 * @author  mmwang
 * @version  1.0 2010-7-20
 */
public class FindNearbyCrossRequest
{
	/**
	 * 
	 */
	private LatLonPoint latLonPoint;
	/**
	 * 
	 */
	private String tMCId;
	/**
	 * 
	 */
	private Vector edgeIDs;
	/**
	 * 
	 */
	private String streetName;

	/**
	 * 
	 */
	public FindNearbyCrossRequest(LatLonPoint latLonPoint, String id,
			Vector edgeIDs, String streetName)
	{
		this.latLonPoint = latLonPoint;
		tMCId = id;
		this.edgeIDs = edgeIDs;
		this.streetName = streetName;
	}
	
	/**
	 * default constructor 
	 */
	public FindNearbyCrossRequest()
	{
	}



	/**
	 * @return the latLonPoint
	 */
	public LatLonPoint getLatLonPoint()
	{
		return latLonPoint;
	}

	/**
	 * @param latLonPoint the latLonPoint to set
	 */
	public void setLatLonPoint(LatLonPoint latLonPoint)
	{
		this.latLonPoint = latLonPoint;
	}

	/**
	 * @return the tMCId
	 */
	public String getTMCId()
	{
		return tMCId;
	}

	/**
	 * @param id the tMCId to set
	 */
	public void setTMCId(String id)
	{
		tMCId = id;
	}

	/**
	 * @return the edgeIDs
	 */
	public Vector getEdgeIDs()
	{
		return edgeIDs;
	}

	/**
	 * @param edgeIDs the edgeIDs to set
	 */
	public void setEdgeIDs(Vector edgeIDs)
	{
		this.edgeIDs = edgeIDs;
	}

	/**
	 * @return the streetName
	 */
	public String getStreetName()
	{
		return streetName;
	}

	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}
}