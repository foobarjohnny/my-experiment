/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.commutealert;


/**
 * a wrapper type for {@link com.telenav.ws.datatypes.traffic.TrafficSummaryReport}
 *
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class TrafficSummaryReport
{
	
	private int incidentCount;
	
	private TrafficSegment[] segment;
	
	private long timestamp;
	
	private int delay;

	/**
	 * @return the incidentCount
	 */
	public int getIncidentCount()
	{
		return incidentCount;
	}

	/**
	 * @param incidentCount the incidentCount to set
	 */
	public void setIncidentCount(int incidentCount)
	{
		this.incidentCount = incidentCount;
	}

	/**
	 * @return the segment
	 */
	public TrafficSegment[] getSegment()
	{
		return segment;
	}

	/**
	 * @param segment the segment to set
	 */
	public void setSegment(TrafficSegment[] segment)
	{
		this.segment = segment;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return the delay
	 */
	public int getDelay()
	{
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
	
	
}
