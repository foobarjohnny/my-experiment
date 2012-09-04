/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.datatypes.restaurant.v10.RestaurantAvailOffer;
import com.telenav.datatypes.services.v20.ResponseStatus;
/**
 * @TODO	Define the response Object
 * @author  
 * @version 1.0 
 */
public class GetTableDetailResponse extends ExecutorResponse {

	private RestaurantAvailOffer availOffer;
	private String dateTime;
	private String date;
	private int partySize;
	private ResponseStatus responseStatus;

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setAvailOffer(RestaurantAvailOffer availOffers) {
		this.availOffer = availOffers;
	}

	public RestaurantAvailOffer getAvailOffer() {
		return availOffer;
	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

}
