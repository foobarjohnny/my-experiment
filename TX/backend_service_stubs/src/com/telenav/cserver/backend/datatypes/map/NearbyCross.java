/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.televigation.mapproxy.datatypes.NearbyCrossItem;

/**
 * proxy class for {@link NearbyCrossItem}
 * 
 * just port from and to crossStreetName now
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class NearbyCross
{
	
	private String fromCrossStreetName;
	
	private String toCrossStreetName;

	/**
	 * @return the fromCrossStreetName
	 */
	public String getFromCrossStreetName()
	{
		return fromCrossStreetName;
	}

	/**
	 * @param fromCrossStreetName the fromCrossStreetName to set
	 */
	public void setFromCrossStreetName(String fromCrossStreetName)
	{
		this.fromCrossStreetName = fromCrossStreetName;
	}

	/**
	 * @return the toCrossStreetName
	 */
	public String getToCrossStreetName()
	{
		return toCrossStreetName;
	}

	/**
	 * @param toCrossStreetName the toCrossStreetName to set
	 */
	public void setToCrossStreetName(String toCrossStreetName)
	{
		this.toCrossStreetName = toCrossStreetName;
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
