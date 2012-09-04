package com.telenav.cserver.util;

public interface TnConstants {
	public static final String PARAMETER_SCREEN = "parameter_screen";
	public static final String TN_LAYOUT_PROPERTY = "tn.layout.property";
	//sharedata
	public static final String SHAREDATA_WEATHER_LOCATION = "sharedata.weather.location";
	//localservice
	public static final String LOCALSERVICE_GETGPS = "getGPS";
	public static final String LOCALSERVICE_CHANGELOCATION = "getAddrFromAC";
	public static final String LOCALSERVICE_MAPTRAFFIC = "LocalService_mapTraffic";
	public static final String LOCALSERVICE_MYFAVORITES = "myFavorites";
	public static final String LOCALSERVICE_RECENTPLACE = "recentPlace";
	public static final String LOCALSERVICE_HOMEADDRESS = "editHome";
	public static final String LOCALSERVICE_CANCEL_SUBSCRIBE = "LOCAL_SERVICE_ID_CANCEL_SUBSCRIBE";
	public static final String LOCALSERVICE_INVOKEPHONEBROWSER = "LocalService_invokePhoneBrowser";
	public static final String LOCALSERVICE_MAKEPHONECALL = "makePhoneCall";
	public static final String LOCALSERVICE_SENDEMAIL = "sendEmail";
	public static final String LOCALSERVICE_PRODUCTTOUR = "producttour";
	public static final String LOCALSERVICE_REFERRALCODE = "referralcode";
	//
    public static final int INDEX_PARAMETER_USER_MIN = 0;
    public static final int INDEX_PARAMETER_USER_PIN = 1;
    public static final int INDEX_PARAMETER_USER_ID = 2;
    
    //VERSION_FLAG
    public static final int INDEX_PARAMETER_CLIENT_CARRIER = 3;
    public static final int INDEX_PARAMETER_CLIENT_PLATFORM = 4;
    public static final int INDEX_PARAMETER_CLIENT_VERSION = 5;
    public static final int INDEX_PARAMETER_CLIENT_DEVICE = 6;
    
    public static final String CALLER_BROWSERSERVER = "BROWSER-SERVER";
    public static final String SUPPORTED_LOCALE="en_US,en_CA,es_CO,es_MX,es_ES,fr_CA,en_GB,it_IT,de_DE,pt_BR";
    public static final String DEFAULT_LOCALE="en_US";
    public static final String CLIENT_LOCALE_en_US = "en_US";
    public static final String CLIENT_LOCALE_pt_BR = "pt_BR";
    public static final String CLIENT_LOCALE_fr_CA = "fr_CA";
    
    public static final String PRODUCT_SN_21 = "SN21";
    
    //platform
    public static final String PLATFORM_RIM = "RIM";
    
    public static final String VERSION_5503 = "5.5.03";
    public static final String VERSION_5502 = "5.5.02";
    public static final String VERSION_5511 = "5.5.11";
    public static final String VERSION_5512 = "5.5.12";
    public static final String CLIENT_VERSION_1_6_0 = "1.6.0";
    public static final String CLIENT_VERSION_1_6_5 = "1.6.5";
    public static final String CLIENT_VERSION_1_5_1 = "1.5.1";
    
    
    public static final String CARRIER_CINGULAR = "Cingular";
    public static final String ATT_NAVIGATOR = "AT&T Navigator";
	//
	/*Constant for weather*/
	public static final int DEFAULT_CITY_LAT = 3741107;
	public static final int DEFAULT_CITY_LON = -12198344;
    public static final int TEMP_CODE_NORMAL = 0;
    public static final int TEMP_CODE_HOT = 1;
    public static final int TEMP_CODE_CODE = 2;
    
    public static final int WEATHER_CODE_DEFAULT = 13; //Sunny
    
    /*Constant for Commute Alert*/
    public static final int ALERT_MAXSIZE = 20;
    /*Alert Status Code*/
	//status code 
	public static final int STATUS_OK = 0;
    //the route is too long
	public static final int STATUS_ROUTE_OUTOF_LIMIT = 1;
    //the number of alert has exceeded maximum
    public static final int STATUS_MAX_ALERTS_EXCEEDED = 2;
    public static final int STATUS_DUPLICATE_ALERT = 3;
    public static final int STATUS_CANNOT_GENERATE_ROUTE = 4;
    public static final int STATUS_ORIGIN_NULL = 5;
    public static final int STATUS_DESTINATION_NULL = 6;
    public static final int STATUS_OD_TOO_CLOSE = 7;
    public static final int STATUS_CANNOT_CONNECTTO_WS = 90;
    public static final int STATUS_WS_EXCEPTION = 91;
    public static final int STATUS_NEED_SYNC = 92;
    public static final int STATUS_MAP_SERVER_ERROR = 92;
    public static final int STATUS_GENERAL_ERROR = 99;
    public static final int STATUS_AC_ERROR = 80;
	public static final double ROUTE_LENGTH_lIMIT = 322 * 1000; //200 miles
	public static final double SCALING_CONSTANT = 1.113194908;// degrees to
	public static final int ALERTNAME_MAXLENGTH = 15;

    public static final int FREQUENCY_ONCE = 0;
	public static final int FREQUENCY_DAILY = 1;
	public static final int FREQUENCY_WEEKLY = 2;
	public static final int FREQUENCY_WEEKDAYS = 3;
	public static final int FREQUENCY_WEEKENDS = 4;
    
    
    //alert status
    public static final String STATUS_ENABLED = "ENABLED";
    public static final String STATUS_DISABLED = "DISABLED";
    public static final String STATUS_EXPIRED = "EXPIRED";
    
	public static final int AC_MASK_WEATHER = 0x1 | 0x2 | 0x4 | 0x8  | 0x40 | 0x100 | 0x200 | 0x0400 | 0x0800;
	public static final int AC_MASK_ALERT = 0x1 | 0x2 | 0x4 | 0x8 | 0x10 | 0x20 | 0x40 | 0x80 | 0x100;
	
	//AddressInfo.max recipients
	public static final int ADDRESSINFO_MAX_RECIPIENTS = 10;
	public static final int NUM_OF_WEEK = 7;
	
	
	//the four constants is for distinguish poi search input method, 2 and 4 means dsr 
	public final static int TYPE_SEARCH_FROM_TYPEIN = 1;
    public final static int TYPE_SEARCH_FROM_SPEAKIN = 2;
    public final static int TYPE_SEARCH_FROM_TYPEIN_ALONG = 3;
    public final static int TYPE_SEARCH_FROM_SPEAKIN_ALONG = 4;

	//One box search
    public static final String OBS_COME_FROM_PLACES = "Places";
    
    public static class StorageKey {
        //Cookie Key 
        public static final String SENTADDRESS_DATA = "SENTADDRESS_DATA";
        public static final String WEATHER_DATA_LIST = "WEATHER_DATA_LIST";
        public static final String WEATHER_DATA_CURRENT = "WEATHER_DATA_CURRENT";
        public static final String WEATHER_DATA_LOCATION = "WEATHER_DATA_LOCATION";
        public static final String WEATHER_PARAMETER = "WEATHER_PARAMETER";
        
        public static final String COMMON_DATA_INDEX = "COMMON_DATA_INDEX";
        public static final String BACK_ACTION_WEATHER = "BACK_ACTION_WEATHER";
    }
}
