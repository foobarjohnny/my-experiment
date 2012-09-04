/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.traffic;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.telenav.datatypes.traffic.tims.v10.SourceType;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class TrafficIncidentSource
{
	public final static String TYPE_TELENAV_USER = SourceType._TELENAV_USER;
	
	public final static String TYPE_TELENAV_WEBSITE = SourceType._TELENAV_WEBSITE;
	
	public final static String TYPE_TELENAV_PARTNER = SourceType._TELENAV_PARTNER;
	
	public final static String TYPE_TELENAV_ADMIN = SourceType._TELENAV_ADMIN;
	
	private String reporterId;
	
	private String sourceReportId;
	
	private String type;
	
	/**
	 * @return the carrier
	 */
	public String getCarrier()
	{
		return carrier;
	}

	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier)
	{
		this.carrier = carrier;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @return the device
	 */
	public String getDevice()
	{
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device)
	{
		this.device = device;
	}

	private String carrier;
	
	private String version;
	
	private String device;

	/**
	 * @return the reporterId
	 */
	public String getReporterId()
	{
		return reporterId;
	}

	/**
	 * @param reporterId the reporterId to set
	 */
	public void setReporterId(String reporterId)
	{
		this.reporterId = reporterId;
	}

	/**
	 * @return the sourceReportId
	 */
	public String getSourceReportId()
	{
		return sourceReportId;
	}

	/**
	 * @param sourceReportId the sourceReportId to set
	 */
	public void setSourceReportId(String sourceReportId)
	{
		this.sourceReportId = sourceReportId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
	
}
