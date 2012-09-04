/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.traffic;

import java.util.Calendar;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.telenav.datatypes.traffic.tims.v10.IncidentType;
import com.telenav.datatypes.traffic.tims.v10.SeverityType;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class TrafficIncident
{
	public final static String SEVERITY_TYPE_LOW_IMPACT = SeverityType._LOW_IMPACT;
	
	public final static String SEVERITY_TYPE_MAJOR = SeverityType._MAJOR;
	
	public final static String SEVERITY_TYPE_MINOR = SeverityType._MINOR;
	
	public final static String SEVERITY_TYPE_CRITICAL = SeverityType._CRITICAL;
	
	public final static String SEVERITY_TYPE_BLOCKER = SeverityType._BLOCKER;
	
	public final static String TYPE_ACCIDENT = IncidentType._ACCIDENT;
	
	public final static String TYPE_CONSTRUCTION = IncidentType._CONSTRUCTION;
	
	//public final static String TYPE_CUSTOM_INCIDENT = IncidentType._CUSTOM_INCIDENT;
	
	public final static String TYPE_DISABLED_VEHICLE = IncidentType._DISABLED_VEHICLE;
	
	//public final static String TYPE_PLANNED_EVENT = IncidentType._PLANNED_EVENT;
	
	public final static String TYPE_ROAD_HAZARD = IncidentType._ROAD_HAZARD;
	
	public final static String TYPE_SPEED_CAMERA = IncidentType._SPEED_CAMERA;
	
	public final static String TYPE_SPEED_TRAP = IncidentType._SPEED_TRAP;
	
	public final static String TYPE_TRAFFIC_CAMERA = IncidentType._TRAFFIC_CAMERA;
	
	private String severity;
	
	private Calendar reportTime = Calendar.getInstance();
	
	private Calendar startTime = Calendar.getInstance();
	
	private int latitude;
	
	private int longitude;
	
	private String streetName;
	
	private String crossStreet;
	
	private String marketID = "UNKNOWN";
	
	private String type;
	
	private String description;

	/**
	 * @return the severity
	 */
	public String getSeverity()
	{
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity)
	{
		this.severity = severity;
	}

	/**
	 * @return the reportTime
	 */
	public Calendar getReportTime()
	{
		return reportTime;
	}

	/**
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(Calendar reportTime)
	{
		this.reportTime = reportTime;
	}

	/**
	 * @return the startTime
	 */
	public Calendar getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime)
	{
		this.startTime = startTime;
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
	 * @return the marketID
	 */
	public String getMarketID()
	{
		return marketID;
	}

	/**
	 * @param marketID the marketID to set
	 */
	public void setMarketID(String marketID)
	{
		this.marketID = marketID;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
	
}
