package com.telenav.cserver.stat;


/**
 * !!!!! Do not modify manually, it is copy from com.telenav.stat, client development
 * @author sergeyz
 *
 */

public interface AttributeID {
    // standard attributes for all events 10001-10010
    public static int EVENT_TYPE_ID         = 10001;
    public static int EVENT_ID              = 10002;
    public static int EVENT_TIMESTAMP       = 10003;
    
    // event counter
    public static int EVENT_COUNTER         = 10011;
    // duration event
    public static int EVENT_DURATION        = 10012;
    
////////////////////////// business specific attribute ids //////////////
    
    // generic attributes for all events 
    // start lat/lon or just lat and lon
    public static int LAT                   = 10101;
    public static int LON                   = 10102;
    public static int END_LAT               = 10103;
    public static int END_LON               = 10104;
    public static int BATTERY_START         = 10105;
    public static int BATTERY_END           = 10106;
    
    //everything is optional if ZIP is provided
    public static int STREET                = 10107;
    public static int CITY                  = 10108;
    public static int ZIP                   = 10109;
    public static int STATE                 = 10110;
    public static int COUNTRY_CODE          = 10111; 
    public static int ADDRESS_TYPE          = 10112;
    public static int ADDRESS_SOURCE        = 10113;
    
    public static int ROUTE_ID              = 10114;
    
    // EventTypes.SESSION_STARTUP = 1 
    /**
     * @see com.telenav.framework.stat.EventTypes#SESSION_STARTUP
     */
    public static int SESSION_ID            = 10200;
    public static int PTN                   = 10201;
    public static int CARRIER_ID            = 10202;
    public static int DEVICE_MAKE           = 10203;
    public static int DEVICE_MODEL          = 10204;
    public static int DEVICE_OS             = 10205;
    public static int APP_VERSION           = 10206;
    public static int CLIENT_VERSION        = 10207;
    // lat and lon are optional for an event
    
    // EventTypes.STARTUP_INFO = 2 
    /**
     * @see com.telenav.framework.stat.EventTypes#STARTUP_INFO
     */
    public static int STARTED_BY            = 10220;
    public static int STARTUP_ENTRY_POINT   = 10221;
    public static int STARTED_FROM  = 10222;
    
    // EventTypes.APP_SESSION_SUMMARY = 3 
    /**
     * @see com.telenav.framework.stat.EventTypes#APP_SESSION_SUMMARY
     */
    // attributes are generic (like battery) or part of composition for now
    
    // EventTypes.PREFERENCE_CHANGE = 4 
    /**
     * @see com.telenav.framework.stat.EventTypes#PREFERENCE_CHANGE
     */
    public static int PREFERENCE_ID     = 10240;
    public static int PREF_OLD_VALUE    = 10241;
    public static int PREF_NEW_VALUE    = 10242;
    
    // EventTypes.FEEDBACK = 5 
    /**
     * @see com.telenav.framework.stat.EventTypes#FEEDBACK
     */
    public static int F_APP_CONTEXT     = 10250;
    public static int F_USECASE         = 10251;
    public static int F_INVOKED         = 10252;

    // navigation 100-199
    //  EventTypes.ROUTE_REQUEST        = 100;
    // most of attributes are generic attributes
    /**
     * @see com.telenav.framework.stat.EventTypes#ROUTE_REQUEST
     */
    public static int ADDRESS_INPUT     = 11001;
    
    //  EventTypes.TRIP_SUMMARY         = 101;
    /**
     * @see com.telenav.framework.stat.EventTypes#TRIP_SUMMARY
     */
    public static int INITIAL_TRIP_DIST     = 11011;
    public static int ACTUAL_TRIP_DIST      = 11012;
    public static int INITIAL_ETA           = 11013;
    public static int DEVCNT_NODEV          = 11014;
    public static int DEVCNT_NEWROUTE       = 11015;
    public static int DEVCNT_ALL            = 11016;
    public static int DEVLOC                = 11017;
    public static int TRIP_END_METHOD       = 11018;
    public static int TMC_SEGMENTS          = 11019;
    public static int TRAFFIC_DELAY_FLAG    = 11020;
    public static int TRAFFIC_DELAY_TIME    = 11021;
    public static int TRAFFIC_INCIDENTS_CNT = 11022;
    public static int SPEED_TRAP_CNT            = 11023;
    public static int REDLIGHT_CAMERA_CNT       = 11024;
    
    //  EventTypes.MAP_UPDATE_TIME  = 102;
    /**
     * @see com.telenav.framework.stat.EventTypes#MAP_UPDATE_TIME
     */
    public static int MAP_UPDATE_MIN        = 11041;
    public static int MAP_UDATE_AVG         = 11042;
    public static int MAP_UDATE_MAX         = 11043;
    
    //  EventTypes.MAP_DISPLAY_TIME     = 103;
    /**
     * @see com.telenav.framework.stat.EventTypes#MAP_DISPLAY_TIME
     */
    public static int MAP_DISPLAY_TIME      = 11051;
    
    //  EventTypes.SPEED_CAMERA_IMPRESSION = 104;
    /**
     * @see com.telenav.framework.stat.EventTypes#SPEED_CAMERA_IMPRESSION
     */
    public static int SPEED_TRAP_ID             = 11061;
    public static int SPEED_TRAP_APP_SCREEN     = 11062;
    public static int SPEED_TRAP_ALERT          = 11063;
    
    //  EventTypes.SPEED_LIMIT_IMPRESSION   = 105;
    /**
     * @see com.telenav.framework.stat.EventTypes#SPEED_LIMIT_IMPRESSION
     */
    public static int SPEED_LIMIT_CNT   = 11071;
    public static int SPEED_AVG         = 11072;
    public static int SPEED_MAX         = 11073;
    public static int SPEED_LIMIT       = 11074;
    public static int ALERT_AVG_TIME    = 11075;
    public static int ALERT_AVG_MAX     = 11076;
    
    // UI experience 200-299
    /**
     * @see com.telenav.framework.stat.EventTypes#HOME_SCREEN_TIME
     */
    public static final int TIME_1      = 13001;
    public static final int TIME_2      = 13002;
    public static final int IS_FIRST_TIME_LOGIN = 13003;
    
    /**
     * @see com.telenav.framework.stat.EventTypes#UI_USAGE_REPORT
     */
    public static final int UI_ID               = 13011;
    public static final int UI_CONTAINER        = 13012;
    public static final int UI_USE_CASE         = 13013;
    
    // misc 300-399
    /** Whenever DSR is used. See nn. 2.2.1.8 (CL15)*/
    public static final int DSR_GENERIC         = 13031;
    
    /**
     * @see com.telenav.framework.stat.EventTypes#POI_IMPRESSION
     */
    // EventTypes.POI_IMPRESSION        = 700;
    // EventTypes.POI_DETAILS           = 701;
    // EventTypes.POI_VIEW_MAP          = 702;
    // EventTypes.POI_DRIVE_TO          = 703;
    // EventTypes.POI_CALL_TO           = 704;
    // EventTypes.POI_VIEW_MERCHANT     = 705;
    // EventTypes.POI_VIEW_COUPON       = 706;
    // EventTypes.POI_VIEW_MENU         = 707;
    // all of them has the same structure
    public static int POI_ID            = 70001;
    public static int ADS_ID            = 70002;
    public static int AD_SOURCE         = 70003;
    public static int SEARCH_UID        = 70004;
    public static int PAGE_NAME         = 70005;
    public static int PAGE_INDEX        = 70006;
    public static int POI_RATING        = 70007;
    public static int POI_DISTANCE      = 70008;
    public static int POI_SORTING       = 70009;
    public static int PAGE_NUMBER       = 70010;
    public static int PAGE_SIZE         = 70011;
    public static int POI_TYPE          = 70012;
    public static int VIEW_TIME         = 70013;

    // EventTypes.POI_SEARCH_REQUEST = 709 
    /**
     * @see com.telenav.framework.stat.EventTypes#POI_SEARCH_REQUEST
     */
    public static int SEARCH_AREA       = 70023;
    
    // AD_BANNER_MSG same as CAMPAIGN_NAME
    public static int       AD_BANNER_MSG         = 14050;
    public static int       CAMPAIGN_VERSION      = 14051;
    public static int       CAMPAIGN_ENTRY_POIT   = 14052;
    public static int		ACTION_NAME           = 14053;
    
    // MESSAGE_NAME same as ACTION_NAME
    public static int       MESSAGE_NAME          = 14053;
    public static int       ACTION_ITEM_NAME      = 14054;
    public static int       PAGE_NAME_TML         = 14055;
    public static int       ACCOUNT_TYPE          = 14056;
    public static int       CAMPAIGN_POSITION     = 14057;
    public static int THIRTYDAYS_FREETRIAL_ENABLED    = 14058;
    
   
}
