/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.protocol.constants;

/**
 * ExecutorTypeDefinitions.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-1
 *
 */
public interface ExecutorTypeDefinitions {
	/**
	 * resource c-s
	 */
	public final static String TYPE_GET_MISSING_AUDIO = "GetMissingAudio";
	public final static String TYPE_SYNC_RESOURCE = "SyncResource";
	public final static String TYPE_SWITCH_LOCALE = "SwitchLocale";
	public final static String TYPE_SWITCH_REGION = "SwitchRegion";
	public final static String TYPE_CELL_TOWER = "CellTower";
	public final static String TYPE_LOCATION_SEARCH = "LocationSearch";
	public final static String TYPE_CACHE_CITIES = "CacheCities";
	public final static String TYPE_STARTUP = "Startup";
	public final static String TYPE_MIS_REPORTING = "MISReporting";
	public final static String TYPE_SYNC_SERVICE_LOCATOR = "SyncServiceLocator";
	
	/**
	 * common c-s
	 */
	public final static String TYPE_SYNC_NEWSTOPS="SyncNewStops";
	public final static String TYPE_LOGGING="Logging";
	public final static String TYPE_FEEDBACK="Feedback";
	public final static String TYPE_SYNC_PREFERENCE="SyncPreference";
	public final static String TYPE_FETCH_ALLSTOPS="FetchAllStops";
	public final static String TYPE_CONFIRM_RECENT_STOPS="ConfirmRecentStops";
	
	/**
	 * poi c-s
	 */
	public final static String TYPE_POI="poi";
    public final static String TYPE_VALIDATA_ADDRESS="ValidateAddress";
    public final static String TYPE_POI_CATEGORY="POICategory";
    public final static String TYPE_SHARE_ADDRESS="ShareAddress";
    public final static String TYPE_SEND_UPDATE="SendUpdate";
    public final static String TYPE_SYNC_HOME="SyncHome";
    public final static String TYPE_ADD_POI="addPOI";
    public final static String TYPE_EDIT_POI="editPOI";
    public final static String TYPE_DELETE_POI="deletePOI";
    public final static String TYPE_FLAG_POI="flagPOI";
    public final static String TYPE_VIEW_REVHISTORYBYPOI="viewRevHistoryByPOI";
    public final static String TYPE_SENT_ADDRESS="SentAddress";
    public final static String TYPE_WEATHER="Weather";
    public final static String TYPE_ABOUT_FEEDBACK="AboutFeedback";
    public final static String TYPE_THIRD_PART="ThirdPart";
    
    /**
     * nav-map c-s
     */
    public final static String TYPE_STREAMING_MAP="Streaming_Map";
    public final static String TYPE_MAP="Map";
    public final static String TYPE_DYNAMIC_ROUTE="Dynamic_Route";
    public final static String TYPE_GET_SPT="Get_SPT";
    public final static String TYPE_GET_EXTRA_EDGE="Get_Extra_Edge";
    public final static String TYPE_CHECK_DEVIATION="Check_Deviation";
    public final static String TYPE_STATIC_ROUTE="Static_Route";
    public final static String TYPE_REVERSE_ROUTE="Reverse_Route";
    public final static String TYPE_DECIMATED_ROUTE="Decimated_Route";
    public final static String TYPE_TRAFFIC_SUMMARY="Traffic_Summary";
    public final static String TYPE_TRAFFIC_ALERTS_MOVING_MAP="Traffic_Alerts_Moving_Map";
    public final static String TYPE_TRAFFIC_AVOID_SELECTED_SEG_REROUTE="Traffic_Avoid_Selected_Seg_Reroute";
    public final static String TYPE_TRAFFIC_MIN_DELAY_REROUTE="Traffic_Min_Delay_Reroute";
    public final static String TYPE_TRAFFIC_SELECTED_REROUTE="Traffic_Selected_Reroute";
    public final static String TYPE_TRAFFIC_AVOID_INCIDENTS_REROUTE="Traffic_Avoid_Incidents_Reroute";
    public final static String TYPE_TRAFFIC_STATIC_AVOID_SELECTED_SEG_REROUTE="Traffic_Static_Avoid_Selected_Seg_Reroute";
    public final static String TYPE_TRAFFIC_STATIC_MIN_DELAY_REROUTE="Traffic_Static_Min_Delay_Reroute";
    public final static String TYPE_TRAFFIC_INCIDENT_REPORT="Traffic_Incident_Report";
    public final static String TYPE_REVERSE_GEOCODING="Reverse_Geocoding";
    public final static String TYPE_COMMUTE_ALERT_DETAILS="Commute_Alert_Details";
    
    /**
     * login-start c-s
     */
    public final static String TYPE_LOGIN="login";
    public final static String TYPE_REGISTER="register";
    public final static String TYPE_CHECK_ACCOUNTSTATUS="checkAccountStatus";
    public final static String TYPE_FORGET_PIN="forgetPin";
    public final static String TYPE_GET_ACCOUNTSTATUS = "Get_Account_Status";
    
    /**
     * commute-alert c-s
     */
    public final static String TYPE_SYNC_ALERTS="SyncAlerts";
    public final static String TYPE_COMMUTE_ALERT="CommuteAlert";
}
