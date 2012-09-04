/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.commutealert;

/**
 * a wrapper type for {@link com.telenav.ws.datatypes.traffic.TrafficSegment}
 *
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class TrafficSegment
{

	private int averageSpeed;
	
	private int postedSpeed;
	
	private int slowestSpeed;
	
	private int length;
	
	private String name;
	
	private int delay;
	
	private int travelTime;
	
	private float congestionTrend;
	
	private String[] tmcId;
	
	private String[] mapId;
	
	private int incidentCount;
	
	private TrafficAlert[] trafficAlert;

	/**
	 * @return the averageSpeed
	 */
	public int getAverageSpeed()
	{
		return averageSpeed;
	}

	/**
	 * @param averageSpeed the averageSpeed to set
	 */
	public void setAverageSpeed(int averageSpeed)
	{
		this.averageSpeed = averageSpeed;
	}

	/**
	 * @return the postedSpeed
	 */
	public int getPostedSpeed()
	{
		return postedSpeed;
	}

	/**
	 * @param postedSpeed the postedSpeed to set
	 */
	public void setPostedSpeed(int postedSpeed)
	{
		this.postedSpeed = postedSpeed;
	}

	/**
	 * @return the slowestSpeed
	 */
	public int getSlowestSpeed()
	{
		return slowestSpeed;
	}

	/**
	 * @param slowestSpeed the slowestSpeed to set
	 */
	public void setSlowestSpeed(int slowestSpeed)
	{
		this.slowestSpeed = slowestSpeed;
	}

	/**
	 * @return the length
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
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

	/**
	 * @return the travelTime
	 */
	public int getTravelTime()
	{
		return travelTime;
	}

	/**
	 * @param travelTime the travelTime to set
	 */
	public void setTravelTime(int travelTime)
	{
		this.travelTime = travelTime;
	}

	/**
	 * @return the congestionTrend
	 */
	public float getCongestionTrend()
	{
		return congestionTrend;
	}

	/**
	 * @param congestionTrend the congestionTrend to set
	 */
	public void setCongestionTrend(float congestionTrend)
	{
		this.congestionTrend = congestionTrend;
	}

	/**
	 * @return the tmcId
	 */
	public String[] getTmcId()
	{
		return tmcId;
	}

	/**
	 * @param tmcId the tmcId to set
	 */
	public void setTmcId(String[] tmcId)
	{
		this.tmcId = tmcId;
	}

	/**
	 * @return the mapId
	 */
	public String[] getMapId()
	{
		return mapId;
	}

	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(String[] mapId)
	{
		this.mapId = mapId;
	}

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
	 * @return the trafficAlert
	 */
	public TrafficAlert[] getTrafficAlert()
	{
		return trafficAlert;
	}

	/**
	 * @param trafficAlert the trafficAlert to set
	 */
	public void setTrafficAlert(TrafficAlert[] trafficAlert)
	{
		this.trafficAlert = trafficAlert;
	}
}
