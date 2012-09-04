/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.pref.struts.util;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-6-3
 */
public interface PreferenceConstants {
    //Preference KEY
    public static final int KEY_GENERAL_FIRSTNAME = 25;
    public static final int KEY_GENERAL_LASTNAME = 26;
    public static final int KEY_GENERAL_EMAIL = 24;
    public static final int KEY_GENERAL_REGION = 10;
    public static final int KEY_GENERAL_LANGUAGE = 9;
    public static final int KEY_GENERAL_DISTANCEUNITS = 1;
    public static final int KEY_GENERAL_TIPS = 5;
    public static final int KEY_GENERAL_CONNECTION = 11;
    public static final int KEY_GENERAL_MERCHANT = 30;
    public static final int KEY_GENERAL_DEALS = 31;
    public static final int KEY_GENERAL_MENU = 32;
    public static final int KEY_GENERAL_SPONSOR = 33;
    
    public static final int KEY_NAVIGATION_ROUTESTYLE = 0;
    public static final int KEY_NAVIGATION_ROUTEAVOID = 15;   
    public static final int KEY_NAVIGATION_MOVINGMAP = 2;
    public static final int KEY_NAVIGATION_BACKLIGHT = 13;
    public static final int KEY_NAVIGATION_LANEASSIST = 31;
    public static final int KEY_NAVIGATION_SPEEDLIMIT = 32;
    public static final int KEY_NAVIGATION_TRAFFICALERT = 7;
    public static final int KEY_NAVIGATION_TRAFFICCAMERA = 33;
    public static final int KEY_NAVIGATION_POLICETRAP = 34;
    
    public static final int KEY_NAVAUDIO_SETTINGS = 3;
    public static final int KEY_NAVAUDIO_GUIDETONE = 4;
    public static final int KEY_NAVAUDIO_DURINGCALL = 18; 
    
    public static final int KEY_SPEECHINPUT_SPEECHINPUT = 19;
    public static final int KEY_SPEECHINPUT_ADDRESSINPUT = 20;
    public static final int KEY_SPEECHINPUT_SEARCHINPUT = 21;
    public static final int KEY_SPEECHINPUT_CONVENIENCEKEY = 28;
    
    public static final int KEY_SPEECHINPUT_ANOUNCESEARCHRESULT = 102;
    
    public static final int VALUE_NOT_AVAIL = -1;
    //value
    public static final int VALUE_ADDRESSINPUT_SPEEKIN =1;
    
    public static final int VALUE_SEARCHINPUT_SPEEKIN =1;
    
    public static final int VALUE_ROUTESTYLE_FASTEST = 1;
    public static final int VALUE_ROUTESTYLE_SHORTEST = 2;
    public static final int VALUE_ROUTESTYLE_STREET = 3;
    public static final int VALUE_ROUTESTYLE_HIGHWAY = 5;
    public static final int VALUE_ROUTESTYLE_PEDESTRIAN = 7;
    public static final int VALUE_SPEECHINPUT_CALLIN = 1;
    public static final int VALUE_ANOUNCESEARCHRESULT_YES = 0;
    
    public static final String VALUE_LANGUAGE_ENGLISH = "en_US";
    
    public static final String VALUE_REGION_AMERICA = "NA";
    public static final String VALUE_REGION_EUROPE = "EU";
    public static final String VALUE_REGION_CHINA = "CN";
    
    public static final int VALUE_NEED_MERCHANT_ON = 0;
    public static final int VALUE_NEED_DEALS_ON = 0;
    public static final int VALUE_NEED_MENU_ON = 0;
    public static final int VALUE_NEED_SPONSOR_ON = 0;
}
