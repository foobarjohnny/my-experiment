/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.navstar;

import com.telenav.cserver.backend.navstar.NavstarDataConverter;

/**
 * a encapsulate class for
 * {@link com.telenav.navstar.proxy.traffic.TrafficIncidentFilter}<br>
 * use
 * {@link NavstarDataConverter#convertTrafficIncidentFilter(TrafficIncidentFilter)}
 * to get a navstar filter
 * 
 * @author mmwang
 * @version 1.0 2010-7-14
 * 
 */
public class TrafficIncidentFilter
{

	private boolean showSpeedTraps;

	private boolean showTrafficCameras;

	/**
	 * @param showSpeedTraps
	 *            the showSpeedTraps to set
	 */
	public void setShowSpeedTraps(boolean showSpeedTraps)
	{
		this.showSpeedTraps = showSpeedTraps;
	}

	/**
	 * @param showTrafficCameras
	 *            the showTrafficCameras to set
	 */
	public void setShowTrafficCameras(boolean showTrafficCameras)
	{
		this.showTrafficCameras = showTrafficCameras;
	}

	/**
	 * @return the showSpeedTraps
	 */
	public boolean isShowSpeedTraps()
	{
		return showSpeedTraps;
	}

	/**
	 * @return the showTrafficCameras
	 */
	public boolean isShowTrafficCameras()
	{
		return showTrafficCameras;
	}
}
