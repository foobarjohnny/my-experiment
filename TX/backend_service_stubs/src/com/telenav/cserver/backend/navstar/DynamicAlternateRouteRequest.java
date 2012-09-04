/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import java.util.List;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.facade.NavPreference;
import com.telenav.cserver.backend.datatypes.navstar.RoutePosition;

/**
 * 
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class DynamicAlternateRouteRequest
{

	private com.telenav.navstar.proxy.route.DynamicAlternateRouteRequest request;

	public DynamicAlternateRouteRequest(TnContext context, List measurements,
			double arg2, NavPreference preference, RoutePosition position)
	{
		this.request = new com.telenav.navstar.proxy.route.DynamicAlternateRouteRequest(
				context, measurements, arg2, preference,
				position == null ? null : position.getPosition());
	}

	/**
	 * @return the request
	 */
	protected com.telenav.navstar.proxy.route.DynamicAlternateRouteRequest getRequest()
	{
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if (this.request != null)
		{
			return "capsulate class for :" + request.toString();
		} else
		{
			return "empty encapsulate class for DynamicAlternateRouteRequest";
		}
	}
}
