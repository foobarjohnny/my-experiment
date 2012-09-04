/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.util.List;

import com.telenav.navstar.proxy.facade.NavStatus;

/**
 * a encapsulate class for
 * {@link com.telenav.navstar.proxy.traffic.TrafficAlertResponse}
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class TrafficAlertResponse
{

	private com.telenav.navstar.proxy.traffic.TrafficAlertResponse response;

	public TrafficAlertResponse(
			com.telenav.navstar.proxy.traffic.TrafficAlertResponse response)
	{
		if (response == null)
		{
			throw new IllegalArgumentException("response could not be null");
		}
		this.response = response;
	}

	public List getAlerts()
	{

		return response.getAlerts();
	}

	public long getTimestamp()
	{

		return response.getTimestamp();
	}

	public long getTotalDelay()
	{

		return response.getTotalDelay();
	}

	public boolean isAvoidPrompt()
	{

		return response.isAvoidPrompt();
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
