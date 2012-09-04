/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes;

/**
 * Constants.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-14
 */
public interface Constants
{
	//for POI
	//sort type
    public static final int SORT_BY_DISTANCE = 0;
    public static final int SORT_BY_RATING = 1;
    public static final int SORT_BY_POPULAE = 2;
    public static final int SORT_BY_RELEVANCE = 3;
    public static final int SORT_BY_GASPRICE = 4;
    
    //UGC preference
    public static final int GENERATE_BY_ALL_USERS = 0;
    public static final int GENERATE_BY_ME = 1;
    public static final int GENERATE_BY_HIGHLY_RATED_USERS = 2;
    public static final int GENERATE_BY_TRUSTED_USERS = 3;
    
    // country code
    public static final String US_COUNTRY = "US";
    public static final String CA_COUNTRY = "CA";
    public static final String BR_COUNTRY = "BR";
    public static final String IN_COUNTRY = "IN";
    public static final String MX_COUNTRY = "MX";
    
    // for commute alerts and navstar copy from com.telenav.cserver.nav.NavConstants
    
	/** the constants to convert from backend datatypes to client datatypes and vice versa */
	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
}
