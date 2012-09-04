package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.executor.GetRestaurantDetailResponse;
import com.telenav.datatypes.restaurant.v10.Restaurant;

public class TestGetRestaurantDetailResponseFormatter extends TestResponseFormatter {
	public void testParseBrowserResponse() {
		GetRestaurantDetailResponseFormatter formatter = new GetRestaurantDetailResponseFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, getGetRestaurantDetailResponse());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GetRestaurantDetailResponse getGetRestaurantDetailResponse(){
		GetRestaurantDetailResponse response = new GetRestaurantDetailResponse();
		Restaurant restaurant = new Restaurant();
		restaurant.setBrandName("restaurant");
		restaurant.setUrl("testUrl");
		response.setRestaurant(restaurant);
		return response;
	}
}
