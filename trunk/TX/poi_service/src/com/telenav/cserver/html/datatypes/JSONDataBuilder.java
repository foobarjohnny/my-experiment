/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;
/**
 * @TODO	Convert a Hotel Item to json String
 * @author  xljiang@telenav.cn
 * @version 1.0 
 */
public class JSONDataBuilder {
	/**
	 * Convert a Hotel Item to json String
	 * 
	 * @param hotel
	 * @return
	 */
	private static Logger logger = Logger.getLogger(JSONDataBuilder.class);
	
	public static JSONObject HotelToJSON(HotelItem hotel) {
		JSONObject jo = new JSONObject();
		try {
			jo.put(HtmlConstants.RRKey.H_ID, hotel.getPoiId());
			jo.put(HtmlConstants.RRKey.H_PARTNERPOIID, hotel.getPartnerPoiId());
			jo.put(HtmlConstants.RRKey.H_NAME, hotel.getName());
			jo.put(HtmlConstants.RRKey.H_ADDRESS, hotel.getAddress());
			jo.put(HtmlConstants.RRKey.H_DISTANCE, hotel.getDistance());
			jo.put(HtmlConstants.RRKey.H_PHONE_NO, hotel.getPhoneNumber());
			jo.put(HtmlConstants.RRKey.H_BRAND, hotel.getBrandName());
			jo.put(HtmlConstants.RRKey.H_HOTEL_TYPE, hotel.getHotelType());
			jo.put(HtmlConstants.RRKey.H_STAR_RATING, hotel.getStarRating());
			jo.put(HtmlConstants.RRKey.H_LOGO_URL, hotel.getLogo());
			// set the photo List
			if (hotel.getPhotos() != null) {
				JSONArray jArr = new JSONArray();
				List<Entry<String, Integer>> photoList = hotel.getPhotos();
				for (int i=0;i<photoList.size();i++) {
					String url = photoList.get(i).getKey();
					jArr.put(url);
				}
				jo.put(HtmlConstants.RRKey.H_PHOTOS, jArr);
			}

			JSONObject stopJSON = new JSONObject();
			Stop stop = hotel.getStop();
			if(stop != null){
				stopJSON.put(HtmlConstants.RRKey.STOP_city, stop.city);
				stopJSON.put(HtmlConstants.RRKey.STOP_FIRST_LINE, stop.firstLine);
				stopJSON.put(HtmlConstants.RRKey.STOP_STATE, stop.state);
				stopJSON.put(HtmlConstants.RRKey.STOP_ZIP, stop.zip);
				stopJSON.put(HtmlConstants.RRKey.STOP_COUNTRY, stop.country);
				stopJSON.put(HtmlConstants.RRKey.STOP_LAT, stop.lat);
				stopJSON.put(HtmlConstants.RRKey.STOP_LON, stop.lon);
				jo.put(HtmlConstants.RRKey.H_STOP, stopJSON);
			}

			jo.put(HtmlConstants.RRKey.H_DESC, hotel.getDescription());
			jo.put(HtmlConstants.RRKey.H_START_PRICE, hotel.getStartPrice());
			jo
					.put(HtmlConstants.RRKey.H_LOCATION_DESC, hotel
							.getLocationDesc());

			if (hotel.getAmenities() != null) {
				// set the photo List
				JSONArray amenities = new JSONArray();
				for (Integer inte : hotel.getAmenities()) {
					amenities.put(inte);
				}
				jo.put(HtmlConstants.RRKey.H_AMENITIES, amenities);
			}

			jo.put(HtmlConstants.RRKey.H_TOTAL_ROOMS, hotel.getTotalRooms());
			jo.put(HtmlConstants.RRKey.H_TOTAL_FLOORS, hotel.getTotalFloors());
			jo.put(HtmlConstants.RRKey.H_YEAR_BUILT, hotel.getYearBuilt());
			jo.put(HtmlConstants.RRKey.H_YEAR_OF_LAST_RENOVATION, hotel
					.getYearOfLastRenovation());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jo;
	}

}
