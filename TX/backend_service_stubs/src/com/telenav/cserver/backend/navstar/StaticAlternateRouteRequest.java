/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.navstar.proxy.facade.NavPreference;
import com.televigation.proxycommon.ProxyAddress;

/**
 * @see com.telenav.navstar.proxy.route.StaticAlternateRouteRequest
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class StaticAlternateRouteRequest
{

	private com.telenav.navstar.proxy.route.StaticAlternateRouteRequest request;

	/**
	 * @param context
	 * @param proxyAddress
	 * @param preference
	 * @param arg3
	 */
	public StaticAlternateRouteRequest(TnContext context,
			ProxyAddress proxyAddress, NavPreference preference, int arg3)
	{
		this.request = new com.telenav.navstar.proxy.route.StaticAlternateRouteRequest(
				context, proxyAddress, preference, arg3);
	}

	/**
	 * @return the request
	 */
	protected com.telenav.navstar.proxy.route.StaticAlternateRouteRequest getRequest()
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
			return "empty encapsulate class for StaticAlternateRouteRequest";
		}
	}
}
