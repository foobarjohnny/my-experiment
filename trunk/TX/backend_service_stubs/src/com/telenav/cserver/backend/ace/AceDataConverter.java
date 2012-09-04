/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.ace;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.datatypes.address.v20.NormalizedName;


/**
 * AceDataConverter.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class AceDataConverter
{
	public static com.telenav.datatypes.address.v20.Address addressToWSAddress(Address address)
	{
		com.telenav.datatypes.address.v20.Address wsAddress = new com.telenav.datatypes.address.v20.Address();
		if(address == null)
		{
			return null;
		}
		if(address.getFirstLine() != null)
		{
			NormalizedName normalFirstLine = new NormalizedName();
			normalFirstLine.setOriginalName(address.getFirstLine());
			wsAddress.setFirstLine(normalFirstLine);
		}
		if(address.getLastLine() != null)
		{
			NormalizedName normalLastLine = new NormalizedName();
			normalLastLine.setOriginalName(address.getLastLine());
			wsAddress.setLastLine(normalLastLine);
		}
		if(address.getStreetName() != null)
		{
			NormalizedName normalStreet = new NormalizedName();
			normalStreet.setOriginalName(address.getStreetName());
			wsAddress.setStreetName(normalStreet);
		}
		if(address.getCrossStreetName() != null)
		{
			NormalizedName normalCrossStreet = new NormalizedName();
			normalCrossStreet.setOriginalName(address.getCrossStreetName());
			wsAddress.setCrossStreetName(normalCrossStreet);
		}
		if(address.getCityName() != null)
		{
			NormalizedName normalCity = new NormalizedName();
			normalCity.setOriginalName(address.getCityName());
			wsAddress.setCityName(normalCity);
		}
		com.telenav.datatypes.address.v20.Country wsCountry = CountryUtils.getByIsoName(address.getCountry());
		wsAddress.setCountry(wsCountry);
		wsAddress.setState(address.getState());
		wsAddress.setCounty(address.getCounty());
		wsAddress.setPostalCode(address.getPostalCode());
		wsAddress.setDoor(address.getDoor());
		wsAddress.setLatitude(address.getLatitude());
		wsAddress.setLongitude(address.getLongitude());
		
		return wsAddress;
	}
	public static GeoCodedAddress wsGeoCodedAddressToCSGeoCodedAddress(com.telenav.services.geocoding.v20.GeoCodedAddress address)
	{
		if(address == null)
		{
			return null;
		}
		else
		{
			GeoCodedAddress geoCodedAddress = new GeoCodedAddress();
			if(address.getFirstLine() != null)
			{
				geoCodedAddress.setFirstLine(address.getFirstLine().getOriginalName());
			}
			if(address.getLastLine() != null)
			{
				geoCodedAddress.setLastLine(address.getLastLine().getOriginalName());
			}
			if(address.getCityName() != null)
			{
				geoCodedAddress.setCityName(address.getCityName().getOriginalName());
			}
			if(address.getStreetName() != null)
			{
				geoCodedAddress.setStreetName(address.getStreetName().getOriginalName());
			}
			if(address.getCrossStreetName() != null)
			{
				geoCodedAddress.setCrossStreetName(address.getCrossStreetName().getOriginalName());
			}
			
			if(address.getCountry() != null)
			{
				geoCodedAddress.setCountry(address.getCountry().getIso2Name());
			}
			geoCodedAddress.setState(address.getState());
			geoCodedAddress.setCounty(address.getCounty());
			geoCodedAddress.setPostalCode(address.getPostalCode());
			geoCodedAddress.setLatitude(address.getLatitude());
			geoCodedAddress.setLongitude(address.getLongitude());
			geoCodedAddress.setDoor(address.getDoor());
			
			geoCodedAddress.setSubStatus(convertGeoCodeSubStatus(address.getSubStatus()));
			return geoCodedAddress;
		}
	}
	
	public static GeoCodeSubStatus convertGeoCodeSubStatus(com.telenav.datatypes.address.v20.GeoCodeSubStatus subStatus)
	{
		GeoCodeSubStatus geoCodeSubStatus = new GeoCodeSubStatus();
		geoCodeSubStatus.setSubstatus_DOOR_CHANGED(subStatus.getSUBSTATUS_DOOR_CHANGED());
		geoCodeSubStatus.setSubstatus_STREET_CHANGED(subStatus.getSUBSTATUS_STREET_CHANGED());
		geoCodeSubStatus.setSubstatus_CROSSSTREET_CHANGED(subStatus.getSUBSTATUS_CROSSSTREET_CHANGED());
		geoCodeSubStatus.setSubstatus_CITY_CHANGED(subStatus.getSUBSTATUS_CITY_CHANGED());
		geoCodeSubStatus.setSubstatus_STATE_CHANGED(subStatus.getSUBSTATUS_STATE_CHANGED());
		geoCodeSubStatus.setSubstatus_COUNTRY_CHANGED(subStatus.getSUBSTATUS_COUNTRY_CHANGED());
		geoCodeSubStatus.setSubstatus_ZIP_CHANGED(subStatus.getSUBSTATUS_ZIP_CHANGED());
		
		return geoCodeSubStatus;
	}
}
