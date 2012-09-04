/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.traffic;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class TrafficIncidentReport
{

	private TrafficIncidentSource source;
	
	private TrafficIncident incident;

	/**
	 * @return the source
	 */
	public TrafficIncidentSource getSource()
	{
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(TrafficIncidentSource source)
	{
		this.source = source;
	}

	/**
	 * @return the incident
	 */
	public TrafficIncident getIncident()
	{
		return incident;
	}

	/**
	 * @param incident the incident to set
	 */
	public void setIncident(TrafficIncident incident)
	{
		this.incident = incident;
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
