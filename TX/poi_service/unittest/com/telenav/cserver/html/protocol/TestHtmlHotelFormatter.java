package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.datatypes.HotelItem;
import com.telenav.cserver.html.executor.HtmlHotelResponse;

public class TestHtmlHotelFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		HtmlHotelResponse rResponse = getHtmlHotelResponse();
		HtmlHotelFormatter formatter = new HtmlHotelFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlHotelResponse getHtmlHotelResponse(){
		HtmlHotelResponse response = new HtmlHotelResponse();
		HotelItem hotel = new HotelItem();
		hotel.setPoiId(0);
		hotel.setPartnerPoiId(0);
		hotel.setName("KFC");
		hotel.setAddress("1130 kifer rd");
		hotel.setDistance("11");
		hotel.setPhoneNumber("4085055555");
		hotel.setBrandName("KFC");
		hotel.setHotelType("xx");
		hotel.setStarRating(3);
		hotel.setLogo("TN");
		
		hotel.setDescription("");
		hotel.setStartPrice(0);
		hotel.setLocationDesc("");
		hotel.setAmenities(null);
		hotel.setTotalRooms(1);
		hotel.setTotalFloors(1);
		hotel.setYearBuilt("");
		hotel.setYearOfLastRenovation("");
		hotel.setStop(TestUtil.getStop());
		return response;
	}
}
