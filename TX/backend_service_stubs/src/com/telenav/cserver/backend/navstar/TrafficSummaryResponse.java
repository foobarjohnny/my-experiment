/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.util.List;

import com.telenav.navstar.proxy.facade.NavStatus;
import com.telenav.navstar.proxy.traffic.TrafficSegment;

/**
 * a encapsulate class for
 * {@link com.telenav.navstar.proxy.traffic.TrafficSummaryResponse}
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class TrafficSummaryResponse
{

	private com.telenav.navstar.proxy.traffic.TrafficSummaryResponse response;

	/**
	 * @param arg0
	 */
	public TrafficSummaryResponse(
			com.telenav.navstar.proxy.traffic.TrafficSummaryResponse response)
	{
		if (response == null)
		{
			throw new IllegalArgumentException("response could not be null");
		}
		this.response = response;
	}

	public void addSegment(TrafficSegment arg0)
	{

		response.addSegment(arg0);
	}

	public int getNumOfAlerts()
	{

		return response.getNumOfAlerts();
	}

	public List getSegments()
	{

		return response.getSegments();
	}

	public long getTimestamp()
	{

		return response.getTimestamp();
	}

	public long getTotalDelay()
	{

		return response.getTotalDelay();
	}

	public String toString()
	{

		return response.toString();
	}

	public NavStatus getStatus()
	{

		return response.getStatus();
	}

	public int getStatusId()
	{

		return response.getStatusId();
	}

	public boolean isFailure()
	{

		return response.isFailure();
	}

	public boolean isSuccess()
	{

		return response.isSuccess();
	}

}
