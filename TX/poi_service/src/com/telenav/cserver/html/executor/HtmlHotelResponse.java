/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;


import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.datatypes.HotelItem;
/**
 * @TODO	Define the response Object
 * @author  
 * @version 1.0 
 */
public class HtmlHotelResponse extends ExecutorResponse {


	HotelItem hotel = new HotelItem();
	
	public HotelItem getHotel() {
		return hotel;
	}

	public void setHotel(HotelItem hotel) {
		this.hotel = hotel;
	}
}
