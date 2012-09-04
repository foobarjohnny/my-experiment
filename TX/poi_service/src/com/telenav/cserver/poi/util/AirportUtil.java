/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.poi.util;

import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.backend.cose.AirportSearchRequest;

/**
 * AirportUtil
 * 
 * @author kwwang
 * 
 */
public class AirportUtil {

	public static AirportSearchRequest makeAirportSearchRequest(
			ValidateAirportRequest request) {
		AirportSearchRequest airportSearchRequest = new AirportSearchRequest();
		airportSearchRequest.setTransactionId("");
		airportSearchRequest.setAirportQuery(request.getAirportName());
		airportSearchRequest.setCountryList("");
		return airportSearchRequest;
	}
	
	public static AirportSearchRequest makeAirportSearchRequest(
			com.telenav.cserver.ac.executor.v20.ValidateAirportRequest request) {
		AirportSearchRequest airportSearchRequest = new AirportSearchRequest();
		airportSearchRequest.setTransactionId("");
		airportSearchRequest.setAirportQuery(request.getAirportName());
		airportSearchRequest.setCountryList("");
		return airportSearchRequest;
	}
}
