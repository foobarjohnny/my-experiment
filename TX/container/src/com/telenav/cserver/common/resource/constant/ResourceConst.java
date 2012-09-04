/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.constant;


/**
 * This class is migrated from ResourceConst in Tn5.x
 * @author kwwang
 *
 */
public class ResourceConst
{
	public static final int STATUS_INVALID_CARRIER = -2;

	public static final int STATUS_ACCOUNT_NOT_FOUND = -1;
	
	public static final int PREF_GUIDE_TONE = 4;
    public static final int PREF_REGION = 11;
    
    
    public static final int TYPE_USER_FAMILY = 255;
    public static final int TYPE_EXPIRETIME = 257;
    public static final int TYPE_MAP_VERSION_UPDATE = 256;
    
    public static final int TYPE_FAKE_PTN = 258;
    
    public static final int TYPE_SOC_CODE= 259;
    
    public static final int TYPE_ADS_LOG= 262;
    
    public static final int TYPE_POI_ADS_DETAIL = 263;
    public static final int TYPE_POI_ADS_OPTION = 264;
    public static final int TYPE_POI_ADS_COUPON = 280;
    
    public static final int ACT_ADSLOGGING = 700;
    
    public static final String TYPE_ACOUNT_FREE = "0";
    public static final String TYPE_ACOUNT_PAID = "1";
    
    /**
     * a key word append to pref name in preference file, cserver will parse it,
     * if has it, remove it and append "EU region" and "CN region" by user product(isEURoaming())
     */
    public static final String PREF_REGION_ROAMING = "%ROAMING%";
	
	public static final String DEFAULT_GUIDE_TONE_SET = "0,1";//0:english femalel; 1: spanish
	
	// Guide Tones
	public static final int GUIDE_TONE_ENGLISH_FEMALE = 0;
	public static final int GUIDE_TONE_SPANISH = 1;
	public static final int GUIDE_TONE_CONFUCIUS_SAY = 2;
	public static final int GUIDE_TONE_NEW_YORK_CABBY = 3;
	public static final int GUIDE_TONE_RASTA_MAN= 4;
	public static final int GUIDE_TONE_SAMMY_THE_SWAMI = 5;
	public static final int GUIDE_TONE_SANTA_CLAUS = 6;
	
	public static final int GUIDE_TONE_ENGLISH_FEMALE_AUDIO_INDEX = 700;
	public static final int GUIDE_TONE_SPANISH_AUDIO_INDEX = 701;
	public static final int GUIDE_TONE_CONFUCIUS_SAY_AUDIO_INDEX = 702;
	public static final int GUIDE_TONE_NEW_YORK_CABBY_AUDIO_INDEX = 703;
	public static final int GUIDE_TONE_RASTA_MAN_AUDIO_INDEX= 704;
	public static final int GUIDE_TONE_SAMMY_THE_SWAMI_AUDIO_INDEX = 705;
	public static final int GUIDE_TONE_SANTA_CLAUS_AUDIO_INDEX = 706;
	
	public static final int GUIDE_TONE_ENGLISH_FEMALE_IMAGE_INDEX = 250;
	public static final int GUIDE_TONE_SPANISH_IMAGE_INDEX = 251;
	public static final int GUIDE_TONE_CONFUCIUS_SAY_IMAGE_INDEX = 252;
	public static final int GUIDE_TONE_NEW_YORK_CABBY_IMAGE_INDEX = 253;
	public static final int GUIDE_TONE_RASTA_MAN_IMAGE_INDEX= 254;
	public static final int GUIDE_TONE_SAMMY_THE_SWAMI_IMAGE_INDEX = 255;
	public static final int GUIDE_TONE_SANTA_CLAUS_IMAGE_INDEX = 256;
	
	// language
	public static final int LANGUAGE_ENGLISH = 0;
	public static final int LANGUAGE_SPANISH = 1;
	public static final int LANGUAGE_MANDARIN = 2;
	public static final int LANGUAGE_RUSSIAN = 3;
	public static final int LANGUAGE_FRENCH = 4;
	public static final int LANGUAGE_CANTONESE = 5;
	public static final int LANGUAGE_VIETNAMESE = 6;
	public static final int LANGUAGE_PORTUEGESE = 7;
	public static final int LANGUAGE_BART = 8;
	public static final int LANGUAGE_JOAN = 9;
	public static final int LANGUAGE_MARYLIN = 10;
	public static final int LANGUAGE_SHARON = 11;	
	
	// Carrier
	public static final String CARRIER_NEXTEL = "Nextel";
	public static final String CARRIER_VERIZON = "Verizon";
	public static final String CARRIER_SPRINT = "SprintPCS";
    public static final String CARRIER_ALLTEL = "Alltel";
    public static final String CARRIER_CINGULAR = "Cingular";
    public static final String CARRIER_TMOBILE = "T-Mobile";
    public static final String CARRIER_TMOBILE_UK = "T-Mobile-UK";
    public static final String CARRIER_ISUCELL = "Iusacell";
    public static final String CARRIER_BELL_CANADA = "BellMob";
    public static final String CARRIER_ROGERS = "Rogers";
    public static final String CARRIER_FIDO = "Fido";
    public static final String CARRIER_WN = "ALL";
    public static final String CARRIER_BOOST = "Boost";
    public static final String CARRIER_TELCEL = "Telcel";
    public static final String CARRIER_ATT = "ATT";    
    public static final String CARRIER_NII = "NII";
    public static final String CARRIER_CINCINNATIBELL = "CincinnatiBell";
    public static final String CARRIER_TELEFONICA = "Telefonica";
    public static final String CARRIER_NII_BR = "NIIBR";
    //public static final String CARRIER_H3G = "H3G";
    public static final String CARRIER_H3G = "Hutchison3G";
    public static final String CARRIER_VODAFONE = "Vodafone";
    //public static final String CARRIER_VIVO_BR = "Vivo";//VIVOGSM
    public static final String CARRIER_VIVO_BR = "VIVOGSM";
	public static final int INVALID_CARRIER = -1;
	public static final String CARRIER_JASPER = "Jasper";
	public static final String CARRIER_NIIBR = "NIIBR";
	public static final String CARRIER_USCC = "USCC";
	public static final String CARRIER_VMC = "VMC";
	
	//Platform
	public static final String PLATFORM_BREW = "BREW";
	public static final String PLATFORM_J2ME = "J2ME";
	public static final String PLATFORM_PALM = "PALM";
    public static final String PLATFORM_WM = "WM";
    public static final String PLATFORM_RIM = "RIM";
    public static final String PLATFORM_SYMBIAN = "SYMBIAN";
    public static final String PLATFORM_LINUX = "LINUX";
    public static final String PLATFORM_CELLTOP = "CELLTOP";
    public static final String PLATFORM_IPHONE = "IPHONE";
    public static final String PLATFORM_ANDROID = "ANDROID";
    public static final String PLATFORM_PALM_LINUX = "PALM-LINUX";    
    public static final String PLATFORM_DANGER = "DANGER";
	
	//device
    public static final String DEVISE_BELLSAMSUNGM3 = "Bell_Samsung_M3";
	//Version
	public static final String VERSION_50 = "5.0";
	public static final String VERSION_51 = "5.1";
    public static final String VERSION_52 = "5.2";
    public static final String VERSION_53 = "5.3";


    public static final String VERSION_55 = "5.5";
    public static final String VERSION_58 = "5.8";
    
    public static final String VERSION_5501 = "5.5.01";
    public static final String VERSION_5502 = "5.5.02";
    public static final String VERSION_5502_free = "5.5.02_free";

    //RIM 55I18N
    public static final String VERSION_5_5_10 = "5.5.10";
    
    //ATT RIM 55I18N
    public static final String VERSION_5_5_11 = "5.5.11";
    public static final String VERSION_5_5_12 = "5.5.12";
    
    //ATT RIM 17
    public static final String VERSION_5_5_13 = "5.5.13";
    
    //ATT WM 13
    public static final String VERSION_5_2_13 = "5.2.13";
    
    //RIM SN version
    public static final String VERSION_5_1_01 = "5.1.01";
    public static final String VERSION_5_1_02 = "5.1.02";
    
    /**
     * RIM 5.1 for Verizon, NO Authentication  
     */ 
    public static final String VERSION_5_1_04 = "5.1.04";
    //RIM i18n version.
    public static final String VERSION_5_1_10 = "5.1.10";
    
    public static final String VERSION_5_1_11 = "5.1.11";
    
    //Alltel brew 5.0.30 
    public static final String VERSION_ALLTEL_5_0_30 = "5.0.30";//5.0.30

    //Alltel brew 5.0.28
    public static final String VERSION_ALLTEL_5_0_28 = "5.0.28";//5.0.30
    
    //Boost TN5.0 Lite j2me
    public static final String VERSION_5_0_30="5.0.30";
    
    public static final String VERSION_5_2_01 = "5.2.01";
    
    public static final String VERSION_5_2_08 = "5.2.08";
	public static final String VERSION_529 = "5.2.09";
	public static final String VERSION_5211 = "5.2.11";
	public static final String VERSION_5212 = "5.2.12";
	
	public static final String VERSION_5213 = "5.2.13";	
	//SN 2.1 
	public static final String VERSION_5_2_10 = "5.2.10";
	
	public static final String VERSION_529_free = "5.2.09_free";
	public static final String VERSION_5211_free = "5.2.11_free";
	public static final String VERSION_5212_free = "5.2.12_free";
	public static final String VERSION_5213_free = "5.2.13_free";
	public static final String VERSION_5_2_25 = "5.2.25";
    public static final String VERSION_5_2_28 = "5.2.28";
    public static final String VERSION_5_2_30 = "5.2.30";
    public static final String VERSION_5_2_32 = "5.2.32";
    public static final String VERSION_5_2_20 = "5.2.20";
    public static final String VERSION_5_2_21 = "5.2.21";
    
    //YPC Nav
    public static final String VERSION_1_1_0 = "1.1.0";
    
    //Bell Mob WM
    public static final String VERSION_5_2_03 = "5.2.03";
    
    public static final String BILLING_FAMILY_SN2 = "SN2";
    public static final String BILLING_FAMILY_SN28 = "SN2.8";
    public static final String BILLING_FAMILY_YPC = "YPC";
	// route style 
	public static final int ROUTE_STYLE_FASTEST = 1;
	public static final int ROUTE_STYLE_SHORTEST = 2;
	public static final int ROUTE_STYLE_PREFER_STREETS = 3;
	public static final int ROUTE_STYLE_PREFER_HIGHWAY = 5;
	public static final int ROUTE_STYLE_PEDESTRIAN = 7;
	public static final int ROUTE_STYLE_TRAFFIC_OPTIMIZED = 8;
        
    /**
     * from TN55, client will send ROUTE_STYLE_COMMUTE_ALERT as route style
     * while sending routing/rerouting request
     * here 99 is a magic number, quite different from regular styles
     */
    public static final int ROUTE_STYLE_COMMUTE_ALERT = 99;
	
    
    //route settings
    public static final int ROUTE_SETTING_HOV_LINES = 1;
    public static final int ROUTE_SETTING_TOLLS = 2;
    public static final int ROUTE_SETTING_TRAFFIC_DELAYS = 4;
    
	// audio settings - determine what to play (if anything)
	public static final int AUDIO_NONE = 0;
	public static final int AUDIO_JINGLES = 1;
	public static final int AUDIO_PROMPTS = 2;
	public static final int AUDIO_EVERYTHING = 3;
	
    // prompt types
    public static final int ACTIONPROMPT = 0;
    public static final int PREPPROMPT = 1;
    public static final int INFOPROMPT = 2;
    
	// properties file define
	public static final String AUDIO_BASES_CONFIG = "AUDIO_BASES_CONFIG";
	public static final String AUDIO_NUMBERS_CONFIG = "AUDIO_NUMBERS_CONFIG";
	public static final String AUDIO_DIRECTIONS_CONFIG = "AUDIO_DIRECTIONS_CONFIG";
	public static final String AUDIO_STREET_TYPES_CONFIG = "AUDIO_STREET_TYPES_CONFIG";
	public static final String INVENTORY_CONFIG = "INVENTORY_CONFIG";
    
    public static final int GLOBAL_ANY_PARENT_ID = 50000; // the

    public static final int DEFAULT_PROMPTS = 0;
    public static final String INVENTORYSET = "InventorySet";
    
    public static final int DEFAULT_PREFERENCE_SET = 0;
    public static final String PREFERENCE_SET = "PreferenceSet";
    
    public static final int DEFAULT_ROADCOLOR_SET = 0;
    public static final String ROADCOLOR_SET = "RoadcolorSet";
    
    public static final int DEFAULT_POLYGONCOLOR_SET = 0;
    public static final String POLYGONCOLOR_SET = "PolygoncolorSet";
    
    public static final int DEFAULT_OTHERSCOLOR_SET = 0;
    public static final String OTHERSCOLOR_SET = "OtherscolorSet";
    
    public static final String DEFAULT_STREET_PROMPT = "CN";
    public static final String STREET_PROMPT_SET = "StreetPrompt";
    
    public static final String DEFAULT_LOGIN_LOGIC = "CN";
    public static final String LOGIN_LOGIC_SET = "LoginLogic";
 
    public static final int DEFAULT_ALERTS_FREQ = 300;
    
    public static final int NEED_NOT_MORE_DOWNLOAD = 0;
    
    public static final int TYPE_MSG_VALIDATE_ACCOUNT_ERROR = 56;
    
    public static final String DEFINE_RECEIVED_ADDRESS = "Received Addresses";
    
    public static final String FREE_VERSION = "_free";
    
    public static final String VERSION_10 = "1.0"; // For Sprint Navigation, e.g. 1.0.01
    
    public static final String VERSION_1_0_01 = "1.0.01"; // For Sprint Navigation (mimic 5.1) 
    public static final String VERSION_1_0_21 = "1.0.21"; // For Sprint Navigation (mimic 5.0 Lite)
    
    public static final String SERVER_URI = "CServer5x";
    
    //for node with type: TYPE_KEY_VALUE_MAPPING
    public final static String KEY_VALUE_TOKEN = "=";

    //for user info
    public final static String KEY_EMAIL = "EMAIL";
    public final static String KEY_FIRSTNAME = "FIRSTNAME";
    public final static String KEY_LASTNAME = "LASTNAME";    
    public final static String KEY_GUIDETONE = "GUIDETONE";
    
    //for DSR request type
    public final static int DSR_RECOGNIZE_CITY_STATE = 101;
    public final static int DSR_RECOGNIZE_AIRPORT = 201;
    public final static int DSR_RECOGNIZE_STREET_ADDRESS = 301;
    public final static int DSR_RECOGNIZE_1_STREET_NAME = 401;
    public final static int DSR_RECOGNIZE_2_STREET_NAME = 402;
    public final static int DSR_RECOGNIZE_INTERSECTION = 403;
    public final static int DSR_RECOGNIZE_POI = 501;
    public final static int TEXT_TO_SPEECH = 901;

    public static final String USER_PRODUCT_EU_ROAMING = "cg_tnunlimited_eu";
    
    /**
     * one year demo account for AT&T Nav Global Edition 
     */
    public static final String USER_PRODUCT_EU_ROAMING_DEMO = "cg_tnunlimited_eu_demo";
    public static final String CALLER_CSERVER = "C-SERVER";
    
    //Telenav Navigator
    public static final String PRODUCT_TN = "TN";
    
    //Rim Store
    public static final String PRODUCT_RIM_STORE = "RIM_STORE";
    
    public static final String PRODUCT_TNT = "TNT";
    //SprintPCS Navigator
    public static final String PRODUCT_SN = "SN"; 
    public static final String PRODUCT_SN_21 = "SN21";
    public static final String PRODUCT_SN_28 = "SN28";
    //Whereboutz
	public static final String PRODUCT_WB = "WB";	
	//AT&T Navigator
	public static final String PRODUCT_AN = "AN";	
	public static final String PRODUCT_ATT_NAV = "ATT_NAV";	
	public static final String PRODUCT_ATT_MAPS = "ATT_MAPS";	
	//YPC: YPC(YellowPages.Com) Mobile
	public static final String PRODUCT_YPC = "YPC";	
	//Celltop WhiteLable product
	public static final String PRODUCT_CELLTOP = "CELLTOP";
	//Alltel product
	public static final String PRODUCT_ALLTEL = "ALLTEL";	
	
	
    //for TTS request type
    public final static String TTS_REQUEST_STREET = "street";
    public final static String TTS_REQUEST_DOOR_NUMBER = "door_number";
    public final static String TTS_REQUEST_CITY_STATE = "city_state";
    public final static String TTS_REQUEST_POI = "poi";
    public final static String TTS_REQUEST_INTERSECTION = "intersection";
    public final static String TTS_REQUEST_AIRPORT = "airport";

    public static String PREFIX_APP_PATH = "config/";
    
    //Indicate if resource load success, this will be set in ResouceLoader.initProperties()
    //and used by heartbeat to monitor the resource load status
    public static boolean isResourceLoadedSuccess = true;
    
    public static boolean isSocketServer = true;
    
    //Support driving on the left
    public static final int U_TURN_L = 50;
    public static final int ROUNDABOUT_ENTER_L = 51;
    public static final int ROUNDABOUT_EXIT_L = 52;

    public static final String DEFAULT_MAP_DATA = "Navteq";
    public static final String DEFAULT_POI_DATASET = "TA";
    
    public static final String KEY_ADDTIONAL_IMAGE = "AddtionalImage";
    
    public static final String BRAZIL_COUNTY_CODE = "55";
    
    public static final String PRODUCT_CODE_MRC_WAP = "tn_sn_mrc_wap";
    public static final String PRODUCT_CODE_PPD_WAP = "tn_sn_1_DAY_WAP";
    
    //login failed type 
    public static final int LOGIN_FAILED_TYPE_ACCOUNT_NOT_FOUND = 1;
    public static final int LOGIN_FAILED_TYPE_TYPE_EXPIRED = 2;
    public static final String KEY_LOGIN_FAILED_TYPE = "KEY_lOGIN_FAILED_TYPE";
    public static final String KEY_LOGIN_FAILED_URL = "KEY_LOGIN_FAILED_URL";
    
    public static final String PROP_DSM_SERVER_STATUS = "dsm_server_status";
    public static final String DSM_SERVER_DOWN = "down";
    
    public static final String POI_ADSTYPE_SPONSORED_TEXT= "SPONSORED_TEXT";
    public static final String POI_ADSTYPE_MERCHANT_CONTENT= "MERCHANT_CONTENT";
    public static final String POI_ADSTYPE_COUPON= "COUPON";
    public static final String POI_ADSTYPE_MENU= "MENU";
    
    // Added these tile types to suppoty New Map tile; Ported these constants from TN5.x ResourceConst
    public static final String DATASET_TELEATLAS = "TeleAtlas";
    public static final String TILE_TYPE_PNG_NAVTEQ = "NA_NT";
    public static final String TILE_TYPE_PNG_TELEATLAS = "NA_TA";
    public static final String TILE_TYPE_PNG_TRAFFIC_NAVTEQ = "NA_NT_T";
    public static final String TILE_TYPE_PNG_TRAFFIC_TELEATLAS= "NA_TA_T";
    public static final String TILE_TYPE_SATELLITE_WITH_ROADNAMELEVEL ="BINGHYB";
    public static final String TILE_TYPE_SATELLITE_WITHOUT_ROADNAMELEVEL ="BINGAER";
    
}
