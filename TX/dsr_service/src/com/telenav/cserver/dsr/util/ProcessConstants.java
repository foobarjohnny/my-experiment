package com.telenav.cserver.dsr.util;

/**
 * 
 * @author joses
 * 
 */
public class ProcessConstants
{

	// Used in ProcessedResult values Map
	public static int CITY = 0;
	public static int STATE = 1;
	public static int STREET = 2;
	public static int POI = 3;
	public static int AIR_LABEL = 10;
	public static int AIR_FULL = 11;
	public static int AIRPORT = 12;
	public static int COMMAND_ACTION = 20;
	public static int COMMAND_MYSTUFF = 23;
	public static int FAVORITE_ID = 24;
	public static int COMMAND_LAT = 25;
	public static int COMMAND_LON = 26;
	public static int FAVORITE_ADDRESS = 27;
	public static int FAVORITE_LABEL = 28;
	public static int COMMAND_TARGET = 29;
	public static int COMMAND_DAY = 30;
	public static int COMMAND_TYPE = 50;
	public static int COMMAND_EXACT_ACE = 60;
	public static final int CMD_LOCATION_TYPE = 51;
	public static final int CMD_TYPE_ADDRESS = 100;
	public static final int CMD_TYPE_SEARCH_POI = 101;
	public static final int CMD_TYPE_DRIVE_POI = 102;
	public static final int CMD_TYPE_MAP_POI = 103;
	public static final int CMD_TYPE_LOCATE_AIRPORT = 104;
	public static final int CMD_TYPE_LOCATE_MY_HOME = 105;
	public static final int CMD_TYPE_LOCATE_FAVOR = 106;
	public static final int CMD_TYPE_MAP_CURRENT = 107;
	public static final int CMD_TYPE_LOCATE_MY_OFFICE = 108;
	public static final int CMD_TYPE_SHORTCUT = 109;

	// Used in RecResult slots Map
	public static String SLOT_CITY = "city";
	public static String SLOT_STATE = "state";
	public static String SLOT_STREET_NUM = "snum";
	public static String SLOT_STREET_NAME = "sname";
	public static String SLOT_COMMAND_ACTION = "action";
	public static String SLOT_COMMAND_NAME = "name";
	public static String SLOT_COMMAND_TARGET = "target";
	public static String SLOT_COMMAND_DAY = "day";
	public static String SLOT_ADDRESS = "address";
	public static String SLOT_AIRPORT = "airport";
	public static String SLOT_COMMAND_MYSTUFF = "mystuff";
	public static String SLOT_COMMAND_FAVORITE = "favoriteid";
	public static String SLOT_FAVORITE_USE_ADDRESS = "use_address";
	public static String SLOT_FAVORITE_USE_LABEL = "use_label";
	public static String SLOT_COMMAND_LAT = "lat";
	public static String SLOT_COMMAND_LON = "lon";
	public static String SLOT_STREET = "street";
	public static String SLOT_XSTREET = "xstreet";
	public static String SLOT_POI = "name";
	public static String SLOT_CURRENT_LOC = "use_current_location";
	public static String SLOT_TRANSACTION_ID = "transactionId";
	public static String SLOT_TYPE = "type";
	public static String SLOT_APPLICATION = "application";
	public static String SLOT_ACTION = "action";
	public static String SLOT_DIGIT_TEXT = "digitText";
	public static String SLOT_DIGITS = "digits";
	public static String SLOT_LANDMARK_CITY = "landmark_city";
	public static String SLOT_LANDMARK_STATE = "landmark_state";
	public static String SLOT_CURRENT_CITY = "current_city";
	public static String SLOT_CURRENT_STATE = "current_state";
	public static String SLOT_SPOKEN_TERM = "spoken_term";
	public static String SLOT_AIRPORT_CODE = "airport_code";
	public static String SLOT_CODE = "code";
	public static String SLOT_SEARCH_RESULT = "search_result";
	public static String ADDRESS_TYPE = "address";
	public static String POI_TYPE = "poi";
	public static String STREETS_TYPE = "streets";
	public static String XSTREET_TYPE = "xstreet";
	public static String APP_MAPS = "poi";

	public static final String CURRENT_LOCATION_HEADER = "current location";

	public static final String IPHONE = "iphone";
	public static final int MAX_DSR_RESULT = 5;
}
