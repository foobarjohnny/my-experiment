package com.telenav.cserver.poi.struts;

import com.telenav.datatypes.locale.v10.Country;

/**
 * 
 * @author chbzhang
 * 
 */
public interface Constant {

	public static class CountryForAC
	{
		public static String CA = Country.CA.getValue();
		public static String MX = Country.MX.getValue();
		public static String BR = Country.BR.getValue();	
	}
	
	public static class UGC {
		public static String UGC_CATEGORY = "UGC_CATEGORY";
		public static String UGC_POI = "UGC_POI";
	}

	public static final class CurrentLocation {
		public static final int GPS = 0;
		public static final int CURRENT_LOCATION = 1;
		public static final int LAST_KNOWN = 2;

	}

	public static final class OneBox {
		public static final int POI_RESULT = 0;
		public static final int ADDRESS_RESULT = 1;
		public static final int DID_YOU_MEAN = 2;

	}

	public static class StorageKey {
		public static final String MAIN_PAGE_POI_PAGE_INDEX = "MAIN_PAGE_POI_PAGE_INDEX";
		public static final String MAIN_PAGE_POI_SORT_TYPE = "MAIN_PAGE_POI_SORT_TYPE";
		public static final String POI_SORT_TYPE_SPARE = "POI_SORT_TYPE_SPARE";

		public static final String ADDRESS_DETAIL_LIST = "ADDRESS_DETAIL_LIST";
		public static final String ADDRESS_DETAIL_LIST_INDEX = "ADDRESS_DETAIL_LIST_INDEX";
		public static final String POI_AUDIO_SAVE = "POI_AUDIO_SAVE";
		public static final String STOP_AUDIO_FLAG = "STOP_AUDIO_FLAG";
		public static final String POI_SHOW_ERRORMSG = "POI_SHOW_ERRORMSG";
		public static final String DRIVE_NO_COVERAGE_FLAG = "DRIVE_NO_COVERAGE_FLAG";

		public static final String SEARCH_POI_INPUTSTRING = "SEARCH_POI_INPUTSTRING";
		public static final String SEARCH_POI_CATEGORY_NAME = "SEARCH_POI_CATEGORY_NAME";
		public static final String SEARCH_POI_PARAMETER = "SEARCH_POI_PARAMETER";
		/**
		 * 0=means do nothing
		 * 1=means back from search category
		 * 2=means back from choose location
		 */
		public static final String SEARCH_POI_FLOW_FLAG = "SEARCH_POI_FLOW_FLAG";
		public static final String SEARCH_POI_SUBCATEGORY_NAME = "SEARCH_POI_SUBCATEGORY_NAME";
		public static final String IS_MOST_POPULAR = "IS_MOST_POPULAR";
		public static final String SEARCH_POI_CATEGORY_ID = "SEARCH_POI_CATEGORY_ID";
		public static final String SEARCH_POI_ADDESS = "SEARCH_POI_ADDESS";
		public static final String CURRENT_PAGE = "CURRENT_PAGE";
		public static final String POI_LIST_START_INDEX = "POI_LIST_START_INDEX";

		public static final String ADDRESS_CAPTURE_PARAMETER = "ADDRESS_CAPTURE_PARAMETER";
		public static final String ADDRESS_CAPTURE_INVOKER = "ADDRESS_CAPTURE_INVOKER";
		public static final String ADDRESS_CAPTURE_CALLBACK_FUNC = "ADDRESS_CAPTURE_CALLBACK_FUNC";
		public static final String ADDRESS_CAPTURE_TYPE = "ADDRESS_CAPTURE_TYPE";
		public static final String ADDRESS_CAPTURE_COUNTRY = "ADDRESS_CAPTURE_COUNTRY";
		public static final String ADDRESS_CAPTURE_STREET1 = "ADDRESS_CAPTURE_STREET1";
		public static final String ADDRESS_CAPTURE_STREET2 = "ADDRESS_CAPTURE_STREET2";
		public static final String ADDRESS_CAPTURE_CITY = "ADDRESS_CAPTURE_CITY";
		public static final String ADDRESS_CAPTURE_STATE = "ADDRESS_CAPTURE_STATE";
		public static final String ADDRESS_CAPTURE_ZIP = "ADDRESS_CAPTURE_ZIP";
		public static final String ADDRESS_CAPTURE_NOT_FOUND = "ADDRESS_CAPTURE_NOT_FOUND";
		public static final String ADDRESS_CAPTURE_LOCATION = "INPUT_ADDRESS_LOCATION";
		public static final String ADDRESS_CAPTURE_DEFAULT_ADDRESS = "ADDRESS_CAPTURE_DEFAULT_ADDRESS";
		public static final String ADDRESS_CAPTURE_FAVORITE_TYPE = "ADDRESS_CAPTURE_FAVORITE_TYPE";

		public static final String ADDRESS_CAPTURE_TEMPLATES = "ADDRESS_CAPTURE_TEMPLATES";
		public static final String MUTLI_MATCHES_ADDRESS_LIST = "MUTLI_MATCHES_ADDRESS_LIST";
		public static final String AUDIO_MULTIPLE_RESULT_INDEX = "AUDIO_MULTIPLE_RESULT_INDEX";
		public static final String POI_REVIEWS_INDEX = "POI_REVIEWS_INDEX";
		public static final String RESENT_SEARCH_STRING = "RESENT_SEARCH_STRING";
		public static final String HOT_BRAND_NODE = "HOT_BRAND_NODE";
		public static final String NEW_POI_BRAND_NAMES = "JSON_OBJECT_NEW_POI_BRAND_NAMES";

		public static final String POI_MODULE_FROM = "POI_MODULE_FROM";
		public static final String POI_MODULE_FROM_SPEAK = "speak";
		public static final String POI_MODULE_FROM_TYPE = "type";
		public static final String SELECT_ADDRESS_PARAMETER = "SELECT_ADDRESS_PARAMETER";
		public static final String MASK_FOR_FAVORITE = "MASK_FOR_FAVORITE";
		public static final String SELECT_ADDRESS_RESUMETRIP = "LAST_TRIP_STOP";
		public static final String LAST_TRIP_ROUTE_SET = "LAST_TRIP_ROUTE_SET";

		public static final String AIRPORT_MESSAGE = "AIRPORT_MESSAGE";
		public static final String MUTLI_MATCHES_AIRPORT_LIST = "MUTLI_MATCHES_AIRPORT_LIST";
		public static final String SEARCH_FROM_TYPE = "SEARCH_FROM_TYPE";
		public static final String SEARCH_TYPE = "SEARCH_TYPE";

		public static final String SHARE_ADDRESS_PARAMETER = "SHARE_ADDRESS_PARAMETER";
		public static final String SHARE_ADDRESS_RECIPIENT = "SHARE_ADDRESS_RECIPIENT";
		public static final String SHARE_ADDRESS_CONTACT = "SHARE_ADDRESS_CONTACT";
		public static final String SHARE_SELECT_CONTACT_DEFAULT = "SHARE_SELECT_CONTACT_DEFAULT";

		public static final String ADDRESS_HOME_PARAMETER = "ADDRESS_HOME_PARAMETER";
		public static final String ADDRESS_HOME_DATA = "ADDRESS_HOME_DATA";
		public static final String ADDRESS_HOME_LABEL = "ADDRESS_HOME_LABEL";
		public static final String ADDRESS_HOME_LABEL_INDEX = "ADDRESS_HOME_LABEL_INDEX";
		public static final String ADDRESS_HOME_LABEL_NODE = "ADDRESS_HOME_LABEL_NODE";
		public static final String SEARCH_ALONG_INFORMATION = "SEARCH_ALONG_INFORMATION";
		public static final String RATE_POI_INDEX = "RATE_POI_INDEX";
		public static final String SEND_UPDATE_PARAMETER = "SEND_UPDATE_PARAMETER";
		public static final String RETURN_TO_LOCALSERVICE = "RETURN_TO_LOCALSERVICE";

		public static final String RECENTPLACE_LOCALSERVICE_STATUS = "RECENTPLACE_LOCALSERVICE_STATUS";
		public static final String FAVORITES_LOCALSERVICE_STATUS = "FAVORITES_LOCALSERVICE_STATUS";
		public static final String SELECT_CONTACT_PARAMETER = "SELECT_CONTACT_PARAMETER";
		public static final String REFERFRIEND_CONTACT_LIST = "REFERFRIEND_CONTACT_LIST";
		public static final String FAVORITE_BACK_URL = "FAVORITE_BACK_URL";
		public static final String SEARCH_CALL_BACK_PAGEURL = "SEARCH_CALL_BACK_PAGEURL";
		public static final String SEARCH_CALL_BACK_FUNCTION = "SEARCH_CALL_BACK_FUNCTION";

		public static final String NAMEANDEMAIL_CALLBACK_URL = "NAMEANDEMAIL_CALLBACK_URL";
		public static final String NAMEANDEMAIL_CALLBACK_FUNC = "NAMEANDEMAIL_CALLBACK_FUNC";
		public static final String REFERFRIEND_PHONE_INFOR = "REFERFRIEND_PHONE_INFOR";
		public static final String BACK_URL_FOR_SELECT_ADDRESS = "BACK_URL_FOR_SELECT_ADDRESS";
		public static final String BACK_URL_FOR_SELECT_COUNTRY = "BACK_URL_FOR_SELECT_COUNTRY";

		public static final String CONTACT_NODE_FOR_SENDUPDATE = "CONTACT_NODE_FOR_SENDUPDATE";
		public static final String POI_SHOW_PROGRESS_BAR = "POI_SHOW_PROGRESS_BAR";
		public static final String PRODUCT_TOUR_PAGE_INDEX = "PRODUCT_TOUR_PAGE_INDEX";

		public static final String BACKURL_WHEN_NOFOUND = "BACKURL_WHEN_NOFOUND";
		public static final String WAIT_AUDIO_FINISH_FLAG = "WAIT_AUDIO_FINISH_FLAG";
		public static final String AUDIO_ALREADY_FINISH_FLAG = "AUDIO_ALREADY_FINISH_FLAG";
		public static final String SEARCH_POI_SUCCESS_FLAG = "SEARCH_POI_SUCCESS_FLAG";
		public static final String RATE_POI_BACK_URL = "RATE_POI_BACK_URL";
		public static final String POI_LIST_CURRENT_SIZE = "POI_LIST_CURRENT_SIZE";

		public static final String IS_SPONSOR_POI = "IS_SPONSOR_POI";
		public static final String SPONSOR_SIZE = "SPONSOR_SIZE";
		public static final String TOTAL_INDEX_FOR_DETAIL = "TOTAL_INDEX_FOR_DETAIL";
		public static final String TOTAL_SIZE_FOR_DETAIL = "TOTAL_SIZE_FOR_DETAIL";
		public static final String CREATE_FAVORITE_RETURN_URL = "CREATE_FAVORITE_RETURN_URL";
		public static final String DONOT_ASK_AGAIN = "DONOT_ASK_AGAIN_FOR";
		public static final String SOURCE_FOR_NAMEANDEMAIL = "SOURCE_FOR_NAMEANDEMAIL";

		public static final String BACK_FUNCTION_FOR_SELECT_ADDRESS = "BACK_FUNCTION_FOR_SELECT_ADDRESS";
		public static final String CITY_NAME_NODE = "CITY_NAME_NODE";
		public static final String CACHE_CITY_NODE = "CACHE_CITY_NODE";
		public static final String NAV_ADDRESS_NODE = "NAV_ADDRESS_NODE";
		public static final String CREATE_OR_EDIT_FAVORITE = "CREATE_OR_EDIT_FAVORITE";
		public static final String CREATE_FAVORITES_LOCALSERVICE_STATUS = "CREATE_FAVORITES_LOCALSERVICE_STATUS";
		public static final String STOP_TYPE = "STOP_TYPE";

		public static final String SEARCH_ALONG_TYPE = "SEARCH_ALONG_TYPE";
		public static final String DRIVETO_DOING_STATUS = "DRIVETO_DOING_STATUS";
		public static final String DRIVETO_FROM_DSR = "DRIVETO_FROM_DSR";
		public static final String DRIVETO_ADDRESS = "DRIVETO_ADDRESS";
		public static final String RECORD_LOCATION_ADDRESS = "RECORD_LOCATION_ADDRESS";
		public static final String NAV_TYPE = "NAV_TYPE";
		public static final String DRIVETO_AUDIO = "DRIVETO_AUDIO";
		public static final String SPONSOR_POI_INDEX = "SPONSOR_POI_INDEX";
		public static final String CREATE_FAV_DOING_STATUS = "CREATE_FAV_DOING_STATUS";
		public static final String CAN_RETURN_TO_EDITFAVORITE = "CAN_RETURN_TO_EDITFAVORITE";
		public static final String DETAIL_TAB_INDEX = "DETAIL_TAB_INDEX";
		public static final String DETAIL_FROM_FLAG = "DETAIL_FROM_FLAG";

		public static final String ATT_MAPS_STATIC_ROUTE = "ATT_MAPS_STATIC_ROUTE";
		public static final String ATT_MAPS_ROUTE_STYLE = "ATT_MAPS_ROUTE_STYLE";
		public static final String SELECTCONTACT_RUNNING = "SELECTCONTACT_RUNNING";

		public static final String BACK_ACTION_SEARCHPOI = "BACK_ACTION_SEARCHPOI";
		public static final String BACK_ACTION_POILIST = "BACK_ACTION_POILIST";
		public static final String BACK_ACTION_DRIVETO = "BACK_ACTION_DRIVETO";
		public static final String BACK_ACTION_MOVIE = "BACK_ACTION_MOVIE";
		public static final String BACK_ACTION_EDITROUT = "BACK_ACTION_EDITROUT";
		public static final String BACK_ACTION_ADDRESS_LIST = "BACK_ACTION_ADDRESS_LIST";
		public static final String ADDRESS_LIST_TITLE_FOR_MAITAI = "ADDRESS_LIST_TITLE_FOR_MAITAI";
		
		public static final String SEARCH_METHOD="POI_SEARCH_METHOD";
		public static final String ONEBOX_DISPLAY_TEXT="ONEBOX_DISPLAY_TEXT";
		public static final String ONEBOX_POI_CALL_BACK_PAGEURL = "ONEBOX_POI_CALL_BACK_PAGEURL";
		public static final String ONEBOX_POI_CALL_BACK_FUNCTION = "ONEBOX_POI_CALL_BACK_FUNCTION";
		
		public static final String GETGPS_CALL_BACK_FUNCTION = "GETGPS_CALL_BACK_FUNCTION";

		public static final String SHOW_NEW_BADGE = "SHOW_NEW_BADGE";
		
		public static final String BROWSER_SHARE_OBJECT_INPUT_CHAR="INPUT_CHAR";
		
		public static final String ONEBOX_TRANSACTION_ID="ONEBOX_TRANSACTION_ID";
		
		public static final String FAVORITES_NAME_LABEL = "FAVORITES_NAME_LABEL";
		
		public static final String ADDRESS_CAPTURE_COUNTRYLIST = "ADDRESS_CAPTURE_COUNTRYLIST";
		
		public static final String ADDRESS_CAPTURE_COUNTRY_FOR_REFRESH_AC_TEMPLATE = "ADDRESS_CAPTURE_COUNTRY_FOR_REFRESH_AC_TEMPLATE";
		
	}

	public static class StorageKeyForJSON {
		public static final String JSON_ARRAY_POI_LIST = "JSON_ARRAY_POI_LIST";
		public static final String JSON_ARRAY_SPONSOR_POI_LIST = "JSON_ARRAY_SPONSOR_POI_LIST";
		public static final String JSON_ARRAY_ADDRESS_DETAIL_LIST = "JSON_ARRAY_ADDRESS_DETAIL_LIST";
		public static final String JSON_OBJECT_SEARCH_POI_ADDESS = "JSON_OBJECT_SEARCH_POI_ADDESS";
		public static final String JSON_OBJECT_NEW_POI_LOCATION = "JSON_OBJECT_NEW_POI_LOCATION";
		public static final String JSON_OBJECT_SHOW_POI_INFORMATION = "JSON_OBJECT_SHOW_POI_INFORMATION";
		public static final String JSON_OBJECT_POI_TODO = "JSON_OBJECT_POI_TODO";
		public static final String ADDRESS_JSON_FOR_SEARCH = "ADDRESS_JSON_FOR_SEARCH";
		public static final String POI_REVIEWS_LIST = "POI_REVIEWS_LIST";
		public static final String CACHE_ADDRESS_JSONOBJECT = "CACHE_ADDRESS_JSONOBJECT";
		public static final String INPUT_ADDRESS_JSONOBJECT = "INPUT_ADDRESS_JSONOBJECT";
		public static final String JSON_OBJECT_MOVIE_ADDESS = "MOVIE_PAGE_JSON_ADDRESS";
	}

	//Badge Frame work Constants
	public static class BadgeFwkConstants
	{
		public static final int MIN_SCREEN_SIZE_TO_SHOW_APPNAME = 480;
		public static final String NEW_APP_LIST_KEY = "BADGE_APP_LIST";
		public static final String BADGE_STRUCTURE_STORAGE_KEY="BADGE_STRUCTURE_COOKIE";
	}
	
	public static final String LOCALSERVICE_DRIVETO = "doNav";
	public static final String LOCALSERVICE_MAPIT = "mapIt";
	public static final String LOCALSERVICE_GETGPS = "getGPS";
	public static final String LOCALSERVICE_GETLASTKNOWNGPS = "getLastValidGps";
	public static final String LOCALSERVICE_BROWSER = "LocalService_invokePhoneBrowser";

	public static final int SELECT_CONTACT_MAXSIZE = 200;

	public static final int SORT_BY_DISTANCE = 0;
	public static final int SORT_BY_RATING = 1;
	public static final int SORT_BY_POPULAE = 2;
	public static final int SORT_BY_RELEVANCE = 3;
	public static final int SORT_BY_GASPRICE = 4;
	
	/** Default category id. -1 = all category. */
    public static final int DEFAULT_CATEGORY = -1;
	/** Default radius. 7000 is from old c-server code. */
    public static final int DEFAULT_RADIUS = 7000;
    public static final int SEARCH_RADIUS = (int)(25*1609.344);

	public static final String LOCALSERVICE_SYNCRESOURCE = "SyncResource";
	public static final String LOCALSERVICE_CONTACT = "SelectContact";
	public static final String LOCALSERVICE_CONTACT_ADDRESS = "SelectAddressFromContact";
	public static final String LOCALSERVICE_DRIVETO_BACK = "BackFromSendETA";
	public static final String LOCALSERVICE_MAKEPHONECALL = "makePhoneCall";
	public static final String LOCALSERVICE_SYNCADDRESS = "SyncAddress";
	public static final String LOCALSERVICE_INVOKEPHONEBROWSER = "LocalService_invokePhoneBrowser";
	public static final String LOCALSERVICE_DIAGNOSTIC = "ShowDiagnostic";
	public static final String LOCALSERVICE_SWITCHAUDIOPATH = "SwitchAudioPath";
	public static final String LOCALSERVICE_GETNONBLOCKINGGPS = "getNonBlockingGPS";
	
	public static final String SEARCH_METHOD_ONEBOX="SEARCH_METHOD_ONEBOX";
	public static final String SEARCH_METHOD_POI="SEARCH_METHOD_POI";
	

	public static final String SHORTCUT_KEY_CONV1 = "conv1";
	public static final String SHORTCUT_KEY_CONV2 = "conv2";
	public static final String SHORTCUT_KEY_PHONECALL = "dial";
	public static final String SHORTCUT_KEY_UP = "up";
	public static final String SHORTCUT_KEY_DOWN = "down";
	public static final String SHORTCUT_KEY_SPACE = "talk";

	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

	public static final int PRODUCT_TOUR_SIZE = 2;

	public static final int RECEIPIENT_MAX_SIZE = 3;

	public static final int POI_SEARCH_MAX_SIZE = 9;
	public static final int PAGE_SIZE = 9;
	public static final int ALL_POI_MAX_SIZE = 100;

	public static final int RECENT_SEARCH_SIZE = 30;
	public static final int RECENT_PLACES_SIZE = 10;
	// should be changed if ID for movie theaters is going to be changed in
	// database
	public static final long MOVIE_THEATER_CATEGORY_ID = 181;

	public static final String MYFAVARITE_TYPE_RECEIVEDADDRESS = "Received Addresses";

	public static final String shareAddress = "SHARE_ADDRESS";
	public static final String sendUpdate = "SEND_UPDATE";
	public static final String referFriend = "REFER_FRIEND";
	public static final String commuteAlert = "COMMUTE_ALERT";

	public static final byte STOP_GENERIC = 0;
	public static final byte STOP_FAVORITE = 1;
	public static final byte STOP_RECENT = 2;
	public static final byte STOP_WAYPOINT = 3;
	public static final byte STOP_AIRPORT = 4;
	public static final byte STOP_CITY = 5;
	public static final byte STOP_CURRENT_LOCATION = 6;
	public static final byte STOP_POI = 7;
	public static final byte STOP_CURSOR_ADDRESS = 8;

	public static final int searchAlongType_closeAhead = 0;
	public static final int searchAlongType_nearDestination = 1;
	
	public final static String BACK_ACTION_MAIN_SCREEN = "main_screen"; 
	public final static String BACK_ACTION_EXIT_APP = "exit_app";
	
    public static final int SERVICE_LEVEL_LITE = 100;
    public static final int SERVICE_LEVEL_BOUNDLE = 200;
    public static final int SERVICE_LEVEL_PREMIUM_FREE = 300;
    public static final int SERVICE_LEVEL_PREMIUM_PPD = 400;
    public static final int SERVICE_LEVEL_PREMIUM_OTHER = 1000;
    
    public static int SERVICE_LEVEL_LITE_WITHOUT_FREE_TMO = 100;
    public static int SERVICE_LEVEL_LITE_WITH_VISUAL_NAV_BAW = 110;
    public static int SERVICE_LEVEL_LITE_WITH_FREE_TMO = 300;
    public static int SERVICE_LEVEL_PREMIUM_TMO = 500;
    public static int SERVICE_LEVEL_PREMIUM_BAW = 450;
    public static int SERVICE_LEVEL_FREETRIAL_BAW = 310;
    
    public static String FREE_TRIAL_OFFER_INTERVAL_MIN = "FREE_TRIAL_OFFER_INTERVAL_MIN";
	public static String FREE_TRIAL_DAYS = "FREE_TRIAL_DAYS";
	public static String FREE_TRIAL_REMINDER_DAY = "FREE_TRIAL_REMINDER_DAY";
	public static String FREE_TRIAL_REMINDER_INTERVAL_MIN = "FREE_TRIAL_REMINDER_INTERVAL_MIN";
	public static String FREE_TRIAL_SHOW_OFFER_DATE = "FREE_TRIAL_SHOW_OFFER_DATE";
	public static String FREE_TRIAL_SHOW_REMINDER_DATE = "FREE_TRIAL_SHOW_REMINDER_DATE";

    public static String ACCOUNT_TYPE_LITE = "SN_lite";
    public static String ACCOUNT_TYPE_BUNDLE = "SN_bundle";
    public static String ACCOUNT_TYPE_PREM = "SN_prem";
    
    public static String KEY_ADJUGGLER_URL = "URL";
    public static String KEY_ADJUGGLER_SEARCH = "SEARCH";
    public static String KEY_ADJUGGLER_WEATHER = "WEATHER";
    public static String KEY_ADJUGGLER_MOVIE = "MOVIE_SEARCH";
    public static String KEY_ADJUGGLER_BROWSER = "EXTERNAL_URL";
    public static String KEY_POPUP = "POPUP";
    public static String KEY_ADJUGGLER_APP_START = "APP_START";
    
    public static String KEY_ADJUGGLER_FROM_MAIN = "MainPage";
    public static String KEY_ADJUGGLER_FROM_POISEARCH = "PoiSearchPage";
    public static String KEY_ADJUGGLER_FROM_TRAFFIC_INCIDENT = "TrafficIncidentPage";
    
    public static final String JSON_KEY_LOCATION = "locationJO";
	public static final String JSON_KEY_FROM = "FROM";
	public static final String JSON_KEY_ISPREMIUMACCOUNT = "isPremiumAccount";
	public static final String JSON_KEY_NEEDPURCHASE = "needPurchase";
	public static final String JSON_KEY_ISFREETRIALELIGIBLE = "isFreeTrialEligible";
	public static final String JSON_KEY_PRODUCTCODE = "productCode";
	public static final String JSON_KEY_SERVICELEVEL = "serviceLevel";
	
	public static final String KEY_STARTUP_PREMIUM = "startup.premium";
	public static final String KEY_STARTUP_NONPREMIUM = "startup.nonPrem";
	public static final String KEY_STARTUP_ISFREETRIALELIGIBLE = "startup.eligibleForFreeTrial";
	public static final String KEY_ADJUGGLER_PREMIUM = "ADJUGGLER_PREMIUM";
	public static final String KEY_ADJUGGLER_NONPREMIUM = "ADJUGGLER_NONPREMIUM";
	
	public static final String KEY_TRAFFIC_PREMIUM = "traffic.premium";
	public static final String KEY_POI_SEARCH_ADJUGGLER = "poi.search.adJuggler";
	
	public static final String SERVER_LOGIN = "{login.http}";
	public static final String SERVER_LOCALAPPFWK = "{localappfwk.http}";
	
	public static final String URL_PURCHASE = "/getInterface.do?jsp=PurchaseInterface";
	public static final String URL_LOCALAPPFWK = "/goToJsp.do?pageRegion=NA&appId=RESTAURANT&jsp=StartUp";
    
    public static String SHARE_ADDRESS_CONTACT_SPLIT = ",";
    
    public static String PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_LITE = "tn_sn_wsip_mrc30";
	public static String PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_BUNDLE = "tn_sn_wsip_mrc_pre30";
    
    
}
