/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes;

import java.util.ArrayList;
import java.util.HashMap;

// keep consistent with ACE GeoCoding
import com.telenav.datatypes.locale.v10.Country;  

/**
 * Address.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class Address
{
	private String firstLine;

	private String lastLine;

	private String cityName;

	private String streetName;

	private String crossStreetName;

	private String county;

	private String state;

	private String country;

	private String postalCode;

	private String door;

	private String label;

	private double latitude;

	private double longitude;

	//ACE4.0
	private String suite;

	private String sublocality;
	
	private String locality;
	
	private String locale;
	
	private String subStreet;
	
	private String buildingName;
	
	private String addressId;
	
	private ArrayList<Country> geoCodingCountryList;
	
	/**
	 * store data gotten from ACE templates by line
	 */
	private HashMap<String, String> lines;

	public HashMap<String, String> getLines()
	{
		return lines;
	}

	public void setLines(HashMap<String, String> lines)
	{
		this.lines = lines;
	}
	

	public ArrayList<Country> getGeoCodingCountryList()
	{
		return geoCodingCountryList;
	}

	public void setGeoCodingCountryList(ArrayList<Country> geoCodingCountryList)
	{
		this.geoCodingCountryList = geoCodingCountryList;
	}
	
	public void addGeoCodingCountryList(Country country)
	{
		if(this.geoCodingCountryList == null)
		{
			this.geoCodingCountryList = new ArrayList<Country>();
		}
		this.geoCodingCountryList.add(country);
	}


	public String getAddressId()
	{
		return addressId;
	}

	public void setAddressId(String addressId)
	{
		this.addressId = addressId;
	}

	public String getBuildingName()
	{
		return buildingName;
	}

	public void setBuildingName(String buildingName)
	{
		this.buildingName = buildingName;
	}

	
	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getLocality()
	{
		return locality;
	}

	public void setLocality(String locality)
	{
		this.locality = locality;
	}

	public String getSublocality()
	{
		return sublocality;
	}

	public void setSublocality(String sublocality)
	{
		this.sublocality = sublocality;
	}

	public String getSubStreet()
	{
		return subStreet;
	}

	public void setSubStreet(String subStreet)
	{
		this.subStreet = subStreet;
	}

	public String getSuite()
	{
		return suite;
	}

	public void setSuite(String suite)
	{
		this.suite = suite;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @param firstLine
	 *            the firstLine to set
	 */
	public void setFirstLine(String firstLine)
	{
		this.firstLine = firstLine;
	}

	/**
	 * @return the firstLine
	 */
	public String getFirstLine()
	{
		return firstLine;
	}

	/**
	 * @param lastLine
	 *            the lastLine to set
	 */
	public void setLastLine(String lastLine)
	{
		this.lastLine = lastLine;
	}

	/**
	 * @return the lastLine
	 */
	public String getLastLine()
	{
		return lastLine;
	}

	/**
	 * @param cityName
	 *            the cityName to set
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName()
	{
		return cityName;
	}
	
	
	/*
	 * @param city
	 * 		method to set city
	 */
	public void setCity(String city){
		this.cityName = city;
	}
	
	
	/*
	 * @return city  
	 * 		
	 */
	public String getCity(){
		return this.cityName == null ? "" : this.cityName;
	}
	
	
	/**
	 * @param streetName
	 *            the streetName to set
	 */
	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}

	/**
	 * @return the streetName
	 */
	public String getStreetName()
	{
		return streetName;
	}

	/**
	 * @param crossStreetName
	 *            the crossStreetName to set
	 */
	public void setCrossStreetName(String crossStreetName)
	{
		this.crossStreetName = crossStreetName;
	}

	/**
	 * @return the crossStreetName
	 */
	public String getCrossStreetName()
	{
		return crossStreetName;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county)
	{
		this.county = county;
	}

	/**
	 * @return the county
	 */
	public String getCounty()
	{
		return county;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param door
	 *            the door to set
	 */
	public void setDoor(String door)
	{
		this.door = door;
	}

	/**
	 * @return the door
	 */
	public String getDoor()
	{
		return door;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}
	
	public String toString()
	{
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("Address[");
		strBuffer.append("firstLine=" + firstLine);
		strBuffer.append(", lastLine=" + lastLine);
		strBuffer.append(", city=" + cityName);
		strBuffer.append(", street=" + streetName);
		strBuffer.append(", crossStreet=" + crossStreetName);
		strBuffer.append(", county=" + county);
		strBuffer.append(", state=" + state);
		strBuffer.append(", country=" + country);
		strBuffer.append(", postalCode=" + postalCode);
		strBuffer.append(", door/house Number=" + door);
		strBuffer.append(", latitude=" + String.valueOf(getLatitude()));
		strBuffer.append(", lontitude=" + String.valueOf(getLongitude()));
		strBuffer.append(", suite/subDoor=" + suite);
		strBuffer.append(", subLocality=" + sublocality);
		strBuffer.append(", locality=" + locality);
		strBuffer.append(", locale=" + locale);
		strBuffer.append(", subStreet=" + subStreet);
		strBuffer.append(", buildingName=" + buildingName);
		strBuffer.append(", addressId=" + addressId);
		strBuffer.append("]");
		if(null != lines)
			strBuffer.append(" Lines[")
					.append(lines.toString())
					.append("]");
		
		return strBuffer.toString();
	}
}
