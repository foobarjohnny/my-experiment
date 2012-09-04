/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import java.util.List;

import com.telenav.cserver.backend.datatypes.map.NearbyCross;
import com.televigation.mapproxy.NearbyCrossStatus;

/**
 * proxy class for {@link NearbyCrossStatus}
 *
 * just port NearbyCross list now
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class NearbyCrossStatusResponse
{

	private List<NearbyCross> nearbyCrossItems;

	/**
	 * @return the nearbyCrossItems
	 */
	public List<NearbyCross> getNearbyCrossItems()
	{
		return nearbyCrossItems;
	}

	/**
	 * @param nearbyCrossItems the nearbyCrossItems to set
	 */
	public void setNearbyCrossItems(List<NearbyCross> nearbyCrossItems)
	{
		this.nearbyCrossItems = nearbyCrossItems;
	}
}
