package com.telenav.cserver.stat;

/**
 * !!!!! Do not modify manually, it is copy from com.telenav.stat, client development
 * @author sergeyz
 *
 */

public interface AttributeValues {

	/**
	 * STARTED BY values from MCD_TN_6 0_Logging v1c.doc n.n. 2.2.1.2 
	 * @see com.telenav.framework.stat.EventTypes#STARTUP_INFO
	 */
	public static final String FROM_CONVENIENCE_KEY = "1";
	public static final String FROM_CALENDAR = "2";
	public static final String FROM_EMAIL = "3";
	public static final String FROM_COMMUTE_ALERT = "4";
	public static final String FROM_ADDRESS_BOOK = "5";
	public static final String FROM_SHARE_ADDRESS = "6";
	public static final String FROM_MANUAL = "7";
	public static final String FROM_OTHER = "8";
	
	/**
	 * Start entry point values from MCD_TN_6 0_Logging v1c.doc n.n. 2.2.1.2 
	 * @see com.telenav.framework.stat.EventTypes#STARTUP_INFO
	 */
	public static final String ENTRY_POINT_DRIVE_TO = "1";
	public static final String ENTRY_POINT_SEARCH = "2";
	public static final String ENTRY_POINT_SHARE_ADDR = "3";
	public static final String ENTRY_POINT_MAP_IT = "4";
	public static final String ENTRY_POINT_HOME = "5";
	public static final String ENTRY_POINT_TRAFFIC_SUMMARY = "6";
	public static final String ENTRY_POINT_OTHER = "7";

	/**
	 * POI type values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.18
	 */
	public static final String POI_TYPE_SPONSORED = "1";
	public static final String POI_TYPE_NORMAL = "2";
	public static final String POI_TYPE_WITH_ADD = "3";
	public static final String POI_TYPE_UPSELL = "4";
	public static final String POI_TYPE_OTHER = "5";
	
	/** Address source types n.n. 2.2.1.5 */
	public static final String AS_RECENT_PLACES = "1";
	public static final String AS_RESUME_TRIP = "2";
	public static final String AS_FAVORITES = "3";
	public static final String AS_RECEIVED_ADDRESS = "4";
	public static final String AS_SEARCH_POI = "5";
	public static final String AS_MAP = "6";
	public static final String AS_EMAIL = "7";
	public static final String AS_CALENDAR = "8";
	public static final String AS_CONTACTS = "9";
	public static final String AS_API = "10";
	public static final String AS_OTHER = "11";
	
	/** Address type n.n. 2.2.1.5 */
	public static final String AT_CITY = "1";
	public static final String AT_POI = "2";
	public static final String AT_INTERSECTION = "3";
	public static final String AT_AIRPORT = "4";
	public static final String AT_ADDRESS = "5";
	public static final String AT_OTHER = "6";
	// the following two from search request address type n.n. 2.2.1.6
	public static final String AT_CURRENT_LOCATION = "7";
	public static final String AT_ALONG_ROUTE = "8";
	
	/** Search area attribute n.n. 2.2.1.6 */
	public static final String SEARCH_AREA_CURRENT_LOCATION = "1";
	public static final String SEARCH_AREA_ADDRESS = "2";
	public static final String SEARCH_AREA_ALONG_ROUTE = "3";
	
	

}
