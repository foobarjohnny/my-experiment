/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.ace;

import com.telenav.cserver.backend.datatypes.Address;

/**
 * GeoCodedAddress.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class GeoCodedAddress extends Address
{
	private GeoCodeSubStatus subStatus;

	/**
	 * Total score based on Address matching similarity
	 */
	private float addressScore;

	public float getAddressScore()
	{
		return addressScore;
	}

	public void setAddressScore(float addressScore)
	{
		this.addressScore = addressScore;
	}

	/**
	 * @param subStatus the subStatus to set
	 */
	public void setSubStatus(GeoCodeSubStatus subStatus)
	{
		this.subStatus = subStatus;
	}

	/**
	 * @return the subStatus
	 */
	public GeoCodeSubStatus getSubStatus()
	{
		return subStatus;
	}
	
	public String toString() {
	    StringBuilder sb=new StringBuilder();
	    sb.append("GeoCodedAddress=[");
	    sb.append(super.toString());
	    if(null != subStatus)
	    {
	    	sb.append(", GeoCodeSubStatus=");
	    	sb.append(this.subStatus.toString());
	    }
	    sb.append("]");
	    return sb.toString();
	}
}

