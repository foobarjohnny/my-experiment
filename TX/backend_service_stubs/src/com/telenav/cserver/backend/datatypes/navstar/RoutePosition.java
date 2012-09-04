/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.navstar;

/**
 * a encapsulate class for
 * {@link com.telenav.navstar.proxy.facade.RoutePosition}
 * 
 * @author mmwang
 * @version 1.0 2010-7-14
 * 
 */
public class RoutePosition
{

	private com.telenav.navstar.proxy.facade.RoutePosition position;

	public RoutePosition(int routeId, int routePathId, int segIndex,
			int edgeIndex, int pointIndex, float offset)
	{
		this.position = new com.telenav.navstar.proxy.facade.RoutePosition(
				routeId, routePathId, segIndex, edgeIndex, pointIndex, offset);
	}

	/**
	 * @return the position
	 */
	public com.telenav.navstar.proxy.facade.RoutePosition getPosition()
	{
		return position;
	}

}
