/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.map.NearbyCross;
import com.televigation.mapproxy.ImageSettings;
import com.televigation.mapproxy.NearbyCrossStatus;
import com.televigation.mapproxy.datatypes.NearbyCrossItem;
import com.televigation.mapproxy.datatypes.mapserverstatus.RGCStatus;
import com.televigation.proxycommon.LatLonPoint;
import com.televigation.proxycommon.RGCAddress;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class DataConverter
{

	/**
	 * convert a backend type {@link QueryMapImageRequest} to {@link ImageSettings}
	 * @param setting
	 * @return
	 */
	public static ImageSettings convertImageSetting(QueryMapImageRequest setting)
	{
		ImageSettings wsSetting = null;
		if (setting != null)
		{
			String viewType = setting.getViewType() == null ? QueryMapImageRequest.IMGSETTINGS_DEFAULT : setting.getViewType();
			String format = setting.getFormat() == null ? QueryMapImageRequest.FORMAT_DEFAULT : setting.getFormat();
			wsSetting = new ImageSettings(convertLatLonPoint(setting.getMinCorner()), convertLatLonPoint(setting.getMaxCorner())
					, setting.getWidth(), setting.getHeight()
					, setting.getRoutePoints(), setting.getAlterRoutePoints()
					, 0, false, false // FIXME don't know this 3 args's usage;
					, viewType, format);
		}
		
		return wsSetting;
	}
	
	/**
	 * convert a backend type {@link com.telenav.cserver.backend.datatypes.map.LatLonPoint} to {@link LatLonPoint}
	 * @param point
	 * @return
	 */
	public static LatLonPoint convertLatLonPoint(com.telenav.cserver.backend.datatypes.map.LatLonPoint point)
	{
		LatLonPoint wsPoint = null;
		if (point != null)
		{
			return new LatLonPoint(point.getLat(), point.getLon());
		}
		
		return wsPoint;
	}
	
	public static RGCStatusResponse convertRGCStatus(RGCStatus status)
	{
		RGCStatusResponse res = null;
		if (status != null)
		{
			res = new RGCStatusResponse();
			res.setStatus(status.status);
			if (status.addresses != null && status.addresses.size() > 0)
			{
				List<Address> addresses = new ArrayList<Address>();
				for (Object rgcAdd : status.addresses)
				{
					RGCAddress rgcAddress = (RGCAddress) rgcAdd;
					Address address = new Address();
					// TODO
					address.setCityName(rgcAddress.getCity());
					address.setCountry(rgcAddress.getCountry().getISO2Code()); // how to do this?
					address.setCounty(rgcAddress.getCounty());
					address.setCrossStreetName(rgcAddress.getXstreetName());
					address.setFirstLine(rgcAddress.getFirstLine());
					address.setLabel(rgcAddress.getLabel());
					address.setLastLine(rgcAddress.getLastLine());
					address.setLatitude(rgcAddress.getLat());
					address.setLongitude(rgcAddress.getLon());
					address.setPostalCode(rgcAddress.getZip());
					address.setState(rgcAddress.getState());
					address.setStreetName(rgcAddress.getStreetName());
					address.setCityName(rgcAddress.getCity());
					addresses.add(address);
				}
				res.setAddresses(addresses);
			}
		}
		return res;
	}

	/**
	 * @param crossStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static NearbyCrossStatusResponse convertNearbyCrossStatus(
			NearbyCrossStatus crossStatus)
	{
		NearbyCrossStatusResponse res = null;
		if (crossStatus != null)
		{
			res = new NearbyCrossStatusResponse();
			Vector<NearbyCrossItem> items = crossStatus.getNearbyCrossItems();
			if (items != null && items.size() > 0)
			{
				List<NearbyCross> crosses = new ArrayList<NearbyCross>();
				for (NearbyCrossItem item : items)
				{
					NearbyCross cross = new NearbyCross();
					cross.setToCrossStreetName(item.getToXStreetName());
					cross.setFromCrossStreetName(item.getFromXStreetName());
				}
				res.setNearbyCrossItems(crosses);
			}
		}
		return res;
	}
}
