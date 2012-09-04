/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * @TODO	define some constants.
 * @author  
 * @version 1.0 
 */
public class HtmlConstants {
	//4 means use can search date in next 4 days, plus today user can total search date for 5 days.
	//day0,1,2,3,4
	public static final int MOVIE_SEARCHDATE_MAXRANGE = 2;
	
	public final static String clientVersion = "1.0";
    public final static String clientName = "c-server";
    
    public static final String OPERATE_UNIQUE_NICKNAME = "yes";
    public static final String OPERATE_NOT_UNIQUE_NICKNAME = "no";
    
    public static final String OPERATE_REVIEW_SUBMIT = "submit";
    public static final String OPERATE_REVIEW_VIEW = "view";
    public static final String OPERATE_REVIEW_SHOW_ADDREVIEW = "showaddreview";
    public static final String OPERATE_REVIEW_GET_REVIEWOPTIONS = "getReviewOptions";
    
    
    public static final String OPERATE_POIDETAIL_MAINNEW = "mainnew";
    public static final String OPERATE_POIDETAIL_MAIN = "main";
    public static final String OPERATE_POIDETAIL_DEAL = "deal";
    public static final String OPERATE_POIDETAIL_MENU = "menu";
    public static final String OPERATE_POIDETAIL_EXTRA = "extra";
    public static final String OPERATE_POIDETAIL_ADSPOI = "adsPoi";
    public static final String OPERATE_POIDETAIL_GASPRICE = "gasprice";
    public static final String OPERATE_FETCH_IMAGE = "getMapImage";
    
    
    public static final String OPERATE_ADSVIEW_BASIC = "basic";
    public static final String OPERATE_ADSVIEW_DETAIL = "detail";
    public static final String OPERATE_ADSVIEW_FETCH_DETAIL_DATA = "fetchDetailData";
    
    public static final String OPERATE_NICKNAME_ADD = "addNickName";
    public static final String OPERATE_NICKNAME_GET = "queryNickName";
    public static final String OPERATE_NICKNAME_CHECK = "checkNickName";
    public static final String OPERATE_NICKNAME_CHECKANDUPDATE = "checkAndUpdateNickName";
	
	public static final String KEY_ADJUGGLER_URL = "URL";
    public static final String KEY_ADJUGGLER_BROWSER = "EXTERNAL_URL";
    public static final String KEY_ADJUGGLER_SEARCH = "SEARCH";
    public static final String KEY_ADJUGGLER_WEATHER = "WEATHER";
    public static final String KEY_ADJUGGLER_MOVIE = "MOVIE_SEARCH";
    public static final String KEY_ADJUGGLER_POPUP = "POPUP";
    public static final int DEGREE_MULTIPLIER = 100000;

    public static final String VENDOR_CODE_HOTEL = "P_HA";
    public static final String VENDOR_CODE_GASPRICE = "P_GBP";	//TODO:
    public static final String VENDOR_CODE_OPENTABLE = "P_OT";	//TODO:
    
    
    public static final String POITYPE_VBB = "5";
    
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", new Locale("en", "US"));
	
	public static final SimpleDateFormat SHORT_TIME_FORMAT = new SimpleDateFormat(
			"H:mm", new Locale("en", "US"));
	
	public static final SimpleDateFormat RESERVATION_DATE_FORMAT = new SimpleDateFormat(
			"EEEE MMM. d", new Locale("en", "US"));
	
	public static final SimpleDateFormat RESERVATION_TIME_FORMAT = new SimpleDateFormat(
			"h:mm a ", new Locale("en", "US"));
	
		public static class RRKey {
		// ========== Hotel ============
		public static final String H_ID = "id";
		public static final String H_PARTNERPOIID = "partnerPoiId";
		public static final String H_NAME = "name";
		public static final String H_ADDRESS = "address";
		public static final String H_DISTANCE = "distance";
		public static final String H_PHONE_NO = "phoneNo";
		public static final String H_BRAND = "brand";
		public static final String H_HOTEL_TYPE = "hotelType";
		public static final String H_STAR_RATING = "rating";
		public static final String H_LOGO_URL = "logoUrl";
		public static final String H_PHOTOS = "photos";
		public static final String H_DESC = "desc";
		public static final String H_START_PRICE = "startPrice";
		public static final String H_LOCATION_DESC = "locationDesc";
		public static final String H_AMENITIES = "amenities";
		public static final String H_STOP = "stop";
		public static final String H_TOTAL_ROOMS = "totalRooms";
		public static final String H_TOTAL_FLOORS = "totalFloors";
		public static final String H_YEAR_BUILT = "yearBuilt";
		public static final String H_YEAR_OF_LAST_RENOVATION = "lastRenovation";
		public static final String R_ROOMID = "roomId";
		public static final String R_ROOMNAME = "roomName";
		public static final String R_ROOMTYPE = "roomType";
		public static final String R_AVERCOST = "averCost";
		public static final String R_CHARGEDETAILMAP = "chargeDetailMap";
		public static final String R_ROOMSUBTOTAL = "roomSubTotal";
		public static final String R_TAXES = "taxes";
		public static final String R_TOTAL = "total";
		// ===================Stop========================
		public static final String STOP_LAT = "lat";
		public static final String STOP_LON = "lon";
		public static final String STOP_city = "city";
		public static final String STOP_COUNTRY = "country";
		public static final String STOP_FIRST_LINE = "firstLine";
		public static final String STOP_STATE = "state";
		public static final String STOP_ZIP = "zip";
		
		//=======================OpenTable=============================
		public static final String PARAMS= "searchParams";
		public static final String SEARCH_RESULTS_LIST= "SearchResultsList";
		public static final String TRACKING_KEY= "TrackingKey";
		public static final String RESERVATION_EMAIL= "ReservationEmail";
		public static final String PARTY_SIZE = "PartySize";
		public static final String RESERVATION_TIME = "ReservationTime";
		public static final String AVAILABLE_TIME = "AvailTime";
		public static final String SEARCH_REQUEST_TIME = "SearchRequestTime";
		public static final String SEARCH_REQUEST_DATE = "SearchRequestDate";
		public static final String SEARCH_PAGE_NUMBER = "batchNumber";
		public static final String SEARCH_PAGE_SIZE= "batchSize";
		public static final String SEARCH_RESULT_KEY= "SearchResultsKey";
		
		public static final String ANCHOR_LAT = "lat";
		public static final String ANCHOR_LON = "lon";
		public static final String ANCHOR_LOCATION = "location";
		
		public static final String SEARCH_OFFER_TIME1 = "SearchOfferTime1";
		public static final String SEARCH_OFFER_TIME2 = "SearchOfferTime2";
		public static final String SEARCH_OFFER_TIME3 = "SearchOfferTime3";
		public static final String SEARCH_OFFER_TIME = "SearchOfferTime";
		public static final String SEARCH_DATE = "SearchDate";
		public static final String SEARCH_DISTANCE = "SearchDistance";
		public static final String SEARCH_DISTANCE_UNIT = "distanceUnit";
		public static final String FORMATTED_SEARCH_DATE = "FormattedSearchDate";
		
		public static final String RESTAURANT_NAME = "SearchRestaurantName";
		public static final String RESTAURANT_NEIGHBOURHOOD = "SearchNeighbourhood";
		public static final String RESTAURANT_STREET1_ADDRESS = "RestaurantStreet1Address";
		public static final String RESTAURANT_STREET2_ADDRESS = "RestaurantStreet2Address";
		public static final String RESTAURANT_CITY = "RestaurantCity";
		public static final String RESTAURANT_PROVINCE = "RestaurantProvince";
		public static final String RESTAURANT_POSTAL_CODE = "RestaurantPostalCode";
		public static final String RESTAURANT_PHONE = "RestaurantPhone";
		public static final String RESTAURANT_DESCRIPTION="RestaurantDescription";
		public static final String RESTAURANT_FOOD_TYPE = "SearchFoodType";
		public static final String RESTAURANT_PRICE_SIGN = "SearchPriceSign";
		public static final String RESTAURANT_PARTNER_POI_ID= "lafObjectId";
		public static final String RESTAURANT_POI_ID= "RestaurantPoiId";
		public static final String RESTAURANT_URL= "RestaurantUrl";
		public static final String RESTAURANT_LOCATION_DESC= "RestaurantLocationDesc";
		public static final String RESTAURANT_PAYMENT_TYPE= "RestaurantPaymentType";
		
		public static final String MS_DISTANCE_UNIT = "DU";
		public static final String RESTAURANT_PARKING = "Parking";
		public static final String RESTAURANT_PRICE_RANGE = "PriceRange";
		public static final String RESTAURANT_HOURS_OF_OPERATION = "HoursOfOperation";
	}
		
	public static class ReservationStatusKeys {
		public static final String SUCCESS	= "0000";
		public static final String SYSTEM_ERROR	= "1000";
	    public static final String DB_ERROR	= "1001";
	    public static final String TIME_OUT = "1002";
	    public static final String NETWORK_ISSUE = "1003";
		  
	    // Business Errors
	    public static final String COMMON_BUSINESS_ERROR	= "2000";
		public static final String INVALID_PARAMETERS	= "2001";
		public static final String CONTENT_ERROR	= "2002";
	    public static final String NOT_SUPPORT_PARTNER = "2003";
	    public static final String PARTNER_DATA_ERROR = "2004";
	    public static final String ORIGINAL_RESERVATION_NOT_EXISTS	= "2005";
	    public static final String NO_EXACT_TIME_OFFER = "2006";
		public static final String CANCEL_RESERVATION_ERROR = "2007";

	}	

}
