/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.commutealert;

/**
 * a wrapper type for {@link com.telenav.ws.datatypes.traffic.TrafficAlert}
 *
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class TrafficAlert
{
	private int numLanesClosed;
	
	private int severity;
	
	private int latitude;
	
	private int longitude;
	
	private String type;
	
	private String crossStreet;
	
	private String locationName;
	
	private String description;
	
	private String longDescription;

	/**
	 * @return the numLanesClosed
	 */
	public int getNumLanesClosed()
	{
		return numLanesClosed;
	}

	/**
	 * @param numLanesClosed the numLanesClosed to set
	 */
	public void setNumLanesClosed(int numLanesClosed)
	{
		this.numLanesClosed = numLanesClosed;
	}

	/**
	 * @return the severity
	 */
	public int getSeverity()
	{
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(int severity)
	{
		this.severity = severity;
	}

	/**
	 * @return the latitude
	 */
	public int getLatitude()
	{
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(int latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public int getLongitude()
	{
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(int longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the crossStreet
	 */
	public String getCrossStreet()
	{
		return crossStreet;
	}

	/**
	 * @param crossStreet the crossStreet to set
	 */
	public void setCrossStreet(String crossStreet)
	{
		this.crossStreet = crossStreet;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName()
	{
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription()
	{
		return longDescription;
	}

	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription)
	{
		this.longDescription = longDescription;
	}
	
	
}
