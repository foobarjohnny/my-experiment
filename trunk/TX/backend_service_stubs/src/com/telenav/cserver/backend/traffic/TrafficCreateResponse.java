/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.traffic;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.telenav.services.traffic.tims.v10.CreateResponseDTO;
import com.telenav.ws.datatypes.services.ResponseStatus;

/**
 * wrap the status info of com.telenav.services.traffic.tims.v10.CreateResponseDTO
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class TrafficCreateResponse
{

	private String statusCode;
	
	private String statusMessage;

	/**
	 * @return the statusCode
	 */
	public String getStatusCode()
	{
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage()
	{
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage)
	{
		this.statusMessage = statusMessage;
	}

	/**
	 * @param response
	 * @return
	 */
	static TrafficCreateResponse fromWsResponse(
			CreateResponseDTO response)
	{
		TrafficCreateResponse result = null;
		if (response != null)
		{
			result = new TrafficCreateResponse();
			ResponseStatus status = response.getResponseStatus();
			if (status != null)
			{
				result.setStatusCode(status.getStatusCode());
				result.setStatusMessage(status.getStatusMessage());
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
	
	
}
