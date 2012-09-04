package com.telenav.cserver.html.protocol;

import java.util.Calendar;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.executor.GetTableDetailResponse;
import com.telenav.datatypes.restaurant.v10.RestaurantAvailOffer;
import com.telenav.datatypes.restaurant.v10.TableOffer;

public class TestGetTableDetailResponseFormatter extends TestResponseFormatter{

	public void testParseBrowserResponse() {
		GetTableDetailResponse rResponse = new GetTableDetailResponse();
		rResponse.setAvailOffer(getRestaurantAvailOffer());
		GetTableDetailResponseFormatter formatter = new GetTableDetailResponseFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private RestaurantAvailOffer getRestaurantAvailOffer(){
		RestaurantAvailOffer offer = new RestaurantAvailOffer();
		TableOffer[] tbOffers = new TableOffer[2];
		for (int i=0;i<2;i++) {
			TableOffer tbOffer = new TableOffer();
			tbOffer.setDateTime(Calendar.getInstance());
			tbOffers[i] = tbOffer;
		}
		
		offer.setTableOffers(tbOffers);
		return offer;
	}

}
