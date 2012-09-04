/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.ace;

/**
 * GeoCodeSubStatus.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 */
public class GeoCodeSubStatus
{
	  private boolean substatus_DOOR_CHANGED;
	  private boolean substatus_STREET_CHANGED;
	  private boolean substatus_ZIP_CHANGED;
	  private boolean substatus_CITY_CHANGED;
	  private boolean substatus_CROSSSTREET_CHANGED;
	  private boolean substatus_STATE_CHANGED;
	  private boolean substatus_COUNTY_CHANGED;
	  private boolean substatus_COUNTRY_CHANGED;
	/**
	 * @param substatus_DOOR_CHANGED the substatus_DOOR_CHANGED to set
	 */
	public void setSubstatus_DOOR_CHANGED(boolean substatus_DOOR_CHANGED)
	{
		this.substatus_DOOR_CHANGED = substatus_DOOR_CHANGED;
	}
	/**
	 * @return the substatus_DOOR_CHANGED
	 */
	public boolean isSubstatus_DOOR_CHANGED()
	{
		return substatus_DOOR_CHANGED;
	}
	/**
	 * @param substatus_STREET_CHANGED the substatus_STREET_CHANGED to set
	 */
	public void setSubstatus_STREET_CHANGED(boolean substatus_STREET_CHANGED)
	{
		this.substatus_STREET_CHANGED = substatus_STREET_CHANGED;
	}
	/**
	 * @return the substatus_STREET_CHANGED
	 */
	public boolean isSubstatus_STREET_CHANGED()
	{
		return substatus_STREET_CHANGED;
	}
	/**
	 * @param substatus_ZIP_CHANGED the substatus_ZIP_CHANGED to set
	 */
	public void setSubstatus_ZIP_CHANGED(boolean substatus_ZIP_CHANGED)
	{
		this.substatus_ZIP_CHANGED = substatus_ZIP_CHANGED;
	}
	/**
	 * @return the substatus_ZIP_CHANGED
	 */
	public boolean isSubstatus_ZIP_CHANGED()
	{
		return substatus_ZIP_CHANGED;
	}
	/**
	 * @param substatus_CITY_CHANGED the substatus_CITY_CHANGED to set
	 */
	public void setSubstatus_CITY_CHANGED(boolean substatus_CITY_CHANGED)
	{
		this.substatus_CITY_CHANGED = substatus_CITY_CHANGED;
	}
	/**
	 * @return the substatus_CITY_CHANGED
	 */
	public boolean isSubstatus_CITY_CHANGED()
	{
		return substatus_CITY_CHANGED;
	}
	/**
	 * @param substatus_CROSSSTREET_CHANGED the substatus_CROSSSTREET_CHANGED to set
	 */
	public void setSubstatus_CROSSSTREET_CHANGED(
			boolean substatus_CROSSSTREET_CHANGED)
	{
		this.substatus_CROSSSTREET_CHANGED = substatus_CROSSSTREET_CHANGED;
	}
	/**
	 * @return the substatus_CROSSSTREET_CHANGED
	 */
	public boolean isSubstatus_CROSSSTREET_CHANGED()
	{
		return substatus_CROSSSTREET_CHANGED;
	}
	/**
	 * @param substatus_STATE_CHANGED the substatus_STATE_CHANGED to set
	 */
	public void setSubstatus_STATE_CHANGED(boolean substatus_STATE_CHANGED)
	{
		this.substatus_STATE_CHANGED = substatus_STATE_CHANGED;
	}
	/**
	 * @return the substatus_STATE_CHANGED
	 */
	public boolean isSubstatus_STATE_CHANGED()
	{
		return substatus_STATE_CHANGED;
	}
	/**
	 * @param substatus_COUNTY_CHANGED the substatus_COUNTY_CHANGED to set
	 */
	public void setSubstatus_COUNTY_CHANGED(boolean substatus_COUNTY_CHANGED)
	{
		this.substatus_COUNTY_CHANGED = substatus_COUNTY_CHANGED;
	}
	/**
	 * @return the substatus_COUNTY_CHANGED
	 */
	public boolean isSubstatus_COUNTY_CHANGED()
	{
		return substatus_COUNTY_CHANGED;
	}
	/**
	 * @param substatus_COUNTRY_CHANGED the substatus_COUNTRY_CHANGED to set
	 */
	public void setSubstatus_COUNTRY_CHANGED(boolean substatus_COUNTRY_CHANGED)
	{
		this.substatus_COUNTRY_CHANGED = substatus_COUNTRY_CHANGED;
	}
	/**
	 * @return the substatus_COUNTRY_CHANGED
	 */
	public boolean isSubstatus_COUNTRY_CHANGED()
	{
		return substatus_COUNTRY_CHANGED;
	}
	
	public String toString() {
	    StringBuilder sb=new StringBuilder();
	    sb.append("GeoCodeSubStatus=[");
	    sb.append("substatus_CITY_CHANGED=");
	    sb.append(this.substatus_CITY_CHANGED);
	    sb.append(", substatus_COUNTRY_CHANGED=");
	    sb.append(this.substatus_COUNTRY_CHANGED);
	    sb.append(", substatus_COUNTY_CHANGED=");
	    sb.append(this.substatus_COUNTY_CHANGED);
	    sb.append(", substatus_CROSSSTREET_CHANGED=");
	    sb.append(this.substatus_CROSSSTREET_CHANGED);
	    sb.append(", substatus_DOOR_CHANGED=");
	    sb.append(this.substatus_DOOR_CHANGED);
	    sb.append(", substatus_STATE_CHANGED=");
	    sb.append(this.substatus_STATE_CHANGED);
	    sb.append(", substatus_STREET_CHANGED=");
	    sb.append(this.substatus_STREET_CHANGED);
	    sb.append(", substatus_ZIP_CHANGED=");
	    sb.append(this.substatus_ZIP_CHANGED);
	    sb.append("]");
	    return sb.toString();
	    
	}
}
