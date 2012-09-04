/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.datatypes.restaurant.v10.Restaurant;
/**
 * @TODO	Define the response Object
 * @author  
 * @version 1.0 
 */
public class GetRestaurantDetailResponse extends ExecutorResponse {

	private Restaurant restaurant;

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
