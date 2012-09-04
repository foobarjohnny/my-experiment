/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.cserver.backend.datatypes.navstar.TrafficIncidentFilter;

/**
 * 
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class TrafficSummaryRequest
{

	private com.telenav.navstar.proxy.traffic.TrafficSummaryRequest request;

	public TrafficSummaryRequest(TnContext context, int routePathId,
			int segIndex, int edgeIndex, int pointIndex, float offset,
			int distance)
	{
		request = new com.telenav.navstar.proxy.traffic.TrafficSummaryRequest(
				context, routePathId, segIndex, edgeIndex, pointIndex, offset);
	}

	protected com.telenav.navstar.proxy.traffic.TrafficSummaryRequest getTrafficSummaryRequest()
	{
		return request;
	}

	/**
	 * @param incidentFilter
	 */
	public void setTrafficIncidentFilter(TrafficIncidentFilter incidentFilter)
	{
		if (incidentFilter != null && this.request != null)
		{
			this.request.setTrafficIncidentFilter(NavstarDataConverter
					.convertTrafficIncidentFilter(incidentFilter));
		}
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
			return "empty encapsulate class for TrafficSummaryRequest";
		}
	}

}
