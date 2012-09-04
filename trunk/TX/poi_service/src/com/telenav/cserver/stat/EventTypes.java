package com.telenav.cserver.stat;
/**
 * !!!!! Do not modify manually, it is copy from com.telenav.framework.stat, client development
 * Constant values for event type identifiers. See MCD_TN6.0_logging v1c.doc for more details 
 * @author sergeyz
 */

public interface EventTypes {
	
	////////////////// Generic events 720-729
	/** generic event with sessionid/device/ptn/client version, etc. See nn. 2.1 */
	public static final int SESSION_STARTUP 	= 720;
	/** How TN was started. See nn. 2.2.1.2 (CL9)*/
	public static final int STARTUP_INFO 		= 721;
	/** Session summary. See nn. 2.2.1.4 (CL2)*/
	public static final int APP_SESSION_SUMMARY = 722;
	/** Whenever preferences are changed. See nn. 2.2.1.14 (CL11)*/
	public static final int PREFERENCE_CHANGE 	= 723;
	/** Whenever feedback request is sent. See nn. 2.2.1.7 (CL14)*/
	public static final int FEEDBACK 			= 724;
	
	// navigation 730-739
	/** Route request. See nn. 2.2.1.5 (CL4) */
	public static final int ROUTE_REQUEST 		= 730;
	/** Trip summary. See nn. 2.2.1.3 (CL1) */
	public static final int TRIP_SUMMARY 		= 731;
	/** Total map update time. See nn. 2.2.1.12 (CL7)*/
	public static final int MAP_UPDATE_TIME 	= 732;
	/** Total map update time. See nn. 2.2.1.11 */
	public static final int MAP_DISPLAY_TIME 	= 733;
	
	/** Whenever speed camera is shown. See nn. 2.2.1.16 (CL10)*/
	public static final int SPEED_CAMERA_IMPRESSION = 734;
	/** Whenever speed limit is shown. See nn. 2.2.1.17 (CL13)*/
	public static final int SPEED_LIMIT_IMPRESSION 	= 735;
	
	// UI experience 740-749
	/** Total home screen time. See nn. 2.2.1.10 (CL6)*/
	public static final int HOME_SCREEN_TIME 		= 740;
	/** Clickstream before request. See nn. 2.2.1.13 (CL8)*/
	public static final int CLICKSTREAM 			= 741;
	/** Generic logging event for client action without invoking server. See nn. 2.2.1.15 (CL12)*/
	public static final int UI_USAGE_REPORT 		= 742;
	/** Once in lifetime event. See nn. 2.2.1.9 (CL5)*/
	public static final int FIRST_TIME_LOGGIN 		= 743;

	// misc 750-759
	/** Whenever DSR is used. See nn. 2.2.1.8 (CL15)*/
	public static final int DSR_GENERIC 		= 750;
	/** Whenever POI search is used. See nn. 2.2.1.6 (CL3)*/
	public static final int POI_SEARCH_REQUEST 		= 751;
	
	//////////////////// events that are part of composition 400-699
	// events that are part of composition, this id is used internal
	public static final int GPS_LOSS_COUNT 				= 400;
	public static final int GPS_LOSS_TIME 				= 401;
	public static final int NETWORK_LOSS_COUNT 			= 402;
	public static final int NETWORK_LOSS_TIME 			= 403;
	public static final int STOP_COUNT 					= 404;
	public static final int STOP_TIME 					= 405;
	public static final int REDLIGHT_CAMERA_COUNT 		= 406;
	public static final int SPEED_TRAP_COUNT 			= 407;
	public static final int POI_SEARCH_ALONG_COUNT 		= 408;
	public static final int RESUME_TRIP_COUNT 			= 409;
	public static final int TIME_TO_SHOW_HOME 			= 410;
	
	///////////////////////////// POI events 700-707, comes from 5.x requirements
	// Requirement from 5.x + extras nn. 2.2.1.6 (CL3)
	public static final int POI_IMPRESSION 			= 700;
	public static final int POI_DETAILS 			= 701;
	public static final int POI_VIEW_MAP 			= 702;
	public static final int POI_DRIVE_TO 			= 703;
	public static final int POI_CALL_TO 			= 704;
	public static final int POI_VIEW_MERCHANT 		= 705;
	public static final int POI_VIEW_COUPON 		= 706;
	public static final int POI_VIEW_MENU 			= 707;
	public static final int POI_VIEW_MAPALL 		= 708;

	//850 - TN 6x internal campaign / ad banner view 
	public static final int AD_BANNER_VIEW 			= 850;
	//851 - TN 6x internal campaign / ad banner click
	public static final int AD_BANNER_CLICK 		= 851;

	public static int[] defaultEvents = {
		SESSION_STARTUP, 
		// POI events have priority P1, so all of them in default list
		POI_IMPRESSION, POI_DETAILS, POI_VIEW_MAP,
		POI_DRIVE_TO, POI_CALL_TO, POI_VIEW_MERCHANT,
		POI_VIEW_COUPON, POI_VIEW_MENU
	};

}
