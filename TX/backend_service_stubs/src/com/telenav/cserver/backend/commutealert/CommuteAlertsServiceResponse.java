/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.commutealert;

import com.telenav.cserver.backend.datatypes.commutealert.TrafficSummaryReport;
import com.telenav.ws.datatypes.alerts.AlertsServiceResponseDTO;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class CommuteAlertsServiceResponse
{
	
	private AlertsServiceResponseDTO response;
	
	/**
	 * @param response
	 */
	CommuteAlertsServiceResponse(AlertsServiceResponseDTO response)
	{
		if (response == null) {
			throw new IllegalArgumentException("AlertsServiceResponseDTO is required");
		}
		this.response = response;
	}



	/* (non-Javadoc)
	 * @see com.telenav.ws.datatypes.alerts.AlertsServiceResponseDTO#getTrafficSummaryReport()
	 */
	public TrafficSummaryReport getTrafficSummaryReport()
	{
		// TODO Auto-generated method stub
		return DataConverter.convertTrafficSummaryReport(this.response.getTrafficSummaryReport());
	}

	
}
