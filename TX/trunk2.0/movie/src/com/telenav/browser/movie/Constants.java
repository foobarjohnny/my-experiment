/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 27, 2008
 * File name: Contants.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 2:14:49 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Constants of the actions.
 * 
 * @author lwei (lwei@telenav.cn) 2:14:49 PM, Oct 27, 2008
 */
public class Constants {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "EEEE, MMM d", new Locale("en", "US"));

    public static final SimpleDateFormat NUMBER_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd", new Locale("en", "US"));

    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(
            "MMM d", new Locale("en", "US"));

    /** The date of the movie. */
    public static final String MOVIES_DATE = "movies.date";
    //local service
    public static final String LOCALSERVICE_DRIVETO = "doNav";
    public static final String LOCALSERVICE_PHONECALL = "makePhoneCall";
    /**
     * The search circle. Both CurrentLocation or AC Address will be saved as
     * search circle. This is only used to calculate the theater distance.
     */
    public static final String SEARCH_CIRCLE = "search.circle";

    public static final String MOVIES_ID_ARRAY_SORT_BY_NAME = "movies.id.array.sort.by.name";
    
    public static final String MOVIES_ID_ARRAY_SORT_BY_RATING = "movies.id.array.sort.by.rating";
    
    public static final String MOVIES_ID_ARRAY_SORT_BY_NEWOPENING = "movies.id.array.sort.by.newopening";
    
    public static final String THEATER_ID_ARRAY = "theater.id.array";

    public static final int SEARCH_RADIUS = 25;

    public static final int PAGE_SIZE = 10; //Theater
    public static final int PAGE_SIZE_MOVIE = 10; //Movie

    public static final int PREFETCH_PAGE_COUNT = 2;

    public static final double MILE_METER = 1609.344;

    public static final String CLIENT_KEY = "client.properties.map";

    public static final String COMMON_PROPERTIES_MAP = "common";

    public static final String LAYOUT_CONFIG = "layoutConfig";
    
    public static final String SEARCH_LOCATION = "movies.location";
    
    public static long LATLON_MULTIPLY = 100000;

}
