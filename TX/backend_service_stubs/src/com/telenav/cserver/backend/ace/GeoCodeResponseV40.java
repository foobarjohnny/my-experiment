/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.ace;

import java.util.List;

import com.telenav.cserver.backend.ace.GeoCodeResponseStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.ws.datatypes.services.ResponseStatus;

/**
 * GeoCodeResponse.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class GeoCodeResponseV40
{
	public final static GeoCodeResponseV40 NULL_RESPONSE = new GeoCodeResponseV40(null, "");

	private List<GeoCodedAddress> addresses = null;

	private String status;

	private GeoCodeResponseStatus geoCodeStatus = null;
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("GeoCodeResponse==>[");
		sb.append("status=");
		sb.append(geoCodeStatus.isSuccessful());
		sb.append(",addresses=");
		if (this.addresses != null)
		{
			for (int index = 0; index < this.addresses.size(); index++)
			{
				sb.append("\n");
				sb.append(addresses.get(index).toString());
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public GeoCodeResponseV40(List<GeoCodedAddress> addresses, String respStatus)
	{
		this.addresses = addresses;
		this.status = respStatus;
		createResponseStatus();
	}
	
	public GeoCodeResponseStatus getStatus()
	{
		return this.geoCodeStatus;
	}

	public List<GeoCodedAddress> getMatches()
	{
		return addresses;
	}

	/**
	 * Return the number of match results from the geocode
	 * 
	 */
	public int getMatchCount()
	{
		if (addresses != null)
			return addresses.size();
		else
			return 0;
	}

	private void createResponseStatus()
	{
		ResponseStatus respStatus = new ResponseStatus();
		respStatus.setStatusMessage(status);
		if("INVALID_ADDRESS_FORMAT".equals(status))
		{
			respStatus.setStatusCode("100002");
		}
		else if("NO_MATCH".equals(status))
		{
			respStatus.setStatusCode("3");
		}
		else if("EXACT_MATCH".equals(status))
		{
			respStatus.setStatusCode("0");
		}
		else if("ADDRESS_MODIFIED".equals(status))
		{
			respStatus.setStatusCode("12");
		}
		else if("MULTIPLE_MATCHES".equals(status))
		{
			respStatus.setStatusCode("1");
		}
		else if("TOO_MANY_MATCHES".equals(status))
		{
			respStatus.setStatusCode("10002");
		}
		else
		{
			respStatus = null;
		}
		geoCodeStatus =new GeoCodeResponseStatus(respStatus);
	}
}
