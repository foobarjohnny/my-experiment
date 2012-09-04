/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.GetRestaurantDetailResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.datatypes.restaurant.v10.Restaurant;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class GetRestaurantDetailResponseFormatter extends BrowserProtocolResponseFormatter {

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest, ExecutorResponse executorResponse)
			throws Exception {

		GetRestaurantDetailResponse rResponse = (GetRestaurantDetailResponse) executorResponse;
		Restaurant restaurant = rResponse.getRestaurant();

		if (restaurant != null) {
			JSONObject jRestaurant = new JSONObject();

			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_NAME, restaurant.getBrandName());

			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_DESCRIPTION,
					restaurant.getRestaurantDescription());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PHONE, restaurant.getPhone());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_STREET1_ADDRESS,
					restaurant.getStreet());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_STREET2_ADDRESS,
					restaurant.getStreet2());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_CITY, restaurant.getCity());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PROVINCE,
					restaurant.getProvince());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_POSTAL_CODE,
					restaurant.getPostalCode());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PARKING, restaurant.getParking());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PRICE_RANGE,
					restaurant.getPriceRange());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PRICE_SIGN,
					restaurant.getPriceSign());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_FOOD_TYPE,
					restaurant.getPrimaryFoodType());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_HOURS_OF_OPERATION,
					restaurant.getHoursOfOperation());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_NEIGHBOURHOOD,
					restaurant.getNeighborhoodName());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_URL, HtmlCommonUtil.useNativeBrowser(restaurant.getUrl()));
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_LOCATION_DESC, restaurant.getSicDesc());
			HtmlPoiUtil.safeJSONPutString(jRestaurant, HtmlConstants.RRKey.RESTAURANT_PAYMENT_TYPE, restaurant.getPriceSign());

			jRestaurant.put(HtmlConstants.RRKey.RESTAURANT_POI_ID, restaurant.getPoiId());
			jRestaurant.put(HtmlConstants.RRKey.RESTAURANT_PARTNER_POI_ID, restaurant.getRestaurantId());

			httpRequest.setAttribute("ajaxResponse", jRestaurant.toString());

		} else {

		}
	}

}
