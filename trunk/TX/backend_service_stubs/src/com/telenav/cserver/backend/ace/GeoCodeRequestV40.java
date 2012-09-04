package com.telenav.cserver.backend.ace;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.services.geocoding.v40.SearchArea;

/**
 * GeoCodeRequest for ACE4.0
 * 
 * @author cguo
 * 
 */
public class GeoCodeRequestV40
{
	private Address address;

	private String transactionID;

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public String getTransactionID()
	{
		return transactionID;
	}

	public void setTransactionID(String transactionID)
	{
		this.transactionID = transactionID;
	}
	
	public String getAddressString()
	{
		String str = AddressDataConverter.getAddressString(address);
		return str;
	}
	
	public SearchArea getSearchArea()
	{
		return AddressDataConverter.getSearchArea(address);
	}
}
