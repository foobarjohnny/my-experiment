/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.map;

import java.util.List;

import com.telenav.cserver.backend.datatypes.Address;
import com.televigation.mapproxy.datatypes.mapserverstatus.RGCStatus;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class RGCStatusResponse
{

	private int status;
	
	private List<Address> addresses;

	/**
	 * @return the status
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses()
	{
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses)
	{
		this.addresses = addresses;
	}
	
	public boolean isSuccess() {
		return RGCStatus.S_OK == status;
	}
}
