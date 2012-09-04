/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.datatypes;

/**
 * PoiConstants.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-22
 */
public interface PoiConstants
{
	//distance unit
	public static final int UNIT_METER = 0;
	public static final int UNIT_MILE = 1;
	public static final int UNIT_KM = 2;
	public static final int UNIT_FOOT = 3;
	
	public static final double KM_TO_MILE = 0.621;
	public static final int KM_TO_METER = 1000;
	public static final double METER_TO_KM = 0.001;
	public static final double METER_TO_FT = 3.281;
	
	
	
	 // search type
    public static final int SEARCH_AROUND_ME = 0;
    public static final int SEARCH_RECENT_STOPS = 1;
    public static final int SEARCH_AIRPORT = 2;
    public static final int SEARCH_CITY = 3;
    public static final int SEARCH_ZIP = 4;
    public static final int SEARCH_ADDRESS = 5;
    public static final int SEARCH_WAYPOINTS = 6;
    public static final int SEARCH_ALONGROUTE = 7;
    
    
    public static final String VIVO_PROG = "VIVONAVPROG";
}
