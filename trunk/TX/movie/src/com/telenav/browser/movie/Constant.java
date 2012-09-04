package com.telenav.browser.movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Constant {
    
	public static final String DATE_FORMAT_STR = "EEEE, MMM d";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			DATE_FORMAT_STR , new Locale("en", "US"));

    public static final SimpleDateFormat NUMBER_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd", new Locale("en", "US"));

    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(
            "MMM d", new Locale("en", "US"));
    
    // should it be based on device ?????? TODO talk to PM
    public static final int BATCH_MULTIPLIER = 2;
    
    public static final double MILE_METER = 1609.344;
    public static final int SEARCH_RADIUS = (int)(25*MILE_METER);
    public static final double DEGREE_MULTIPLIER = 1.e5; 
    
    public static final String LOCALSERVICE_CONTACT = "SelectContact";
    public static final int RECEIPIENT_MAX_SIZE = 5;
    public static final int DUNIT_MILES = 1;
    
    public static final String shareMovie = "SHARE_MOVIE";
    
    // New Sort By string constants. This is what should be defined in adjuggler also
    public static final String SORT_BY_NAME = "NAME";
    public static final String SORT_BY_RANK = "RANK";
    public static final String SORT_BY_RELEASE_DATE = "RELEASE_DATE"; // new movies
    public static final String SORT_BY_WEEKEND_GROSS = "WEEKEND_GROSS"; // recent popular movies
    
    public final static String BACK_ACTION_MAIN_SCREEN = "main_screen"; 
	public final static String BACK_ACTION_EXIT_APP = "exit_app";
	
	public static final double RATEUNIT = 0.5;
    
    public static class StorageKey {
        public static final String MOVIE_PAGE_KEYWORD = "MOVIE_PAGE_KEYWORD";
        public static final String MOVIE_PAGE_ADDRESS = "MOVIE_PAGE_ADDRESS";
        public static final String MOVIE_PAGE_DATE_ID = "MOVIE_PAGE_DATE_ID";
        public static final String MOVIE_PAGE_DATE_LABEL = "MOVIE_PAGE_DATE_LABEL";
        public static final String MOVIE_LIST_CURRENT_PAGE = "MOVIE_LIST_CURRENT_PAGE";
        public static final String MOVIE_LIST_BATCH_NUMBER = "MOVIE_LIST_BATCH_NUMBER";
        // original app name from which we came to movie search (values theater, movie for now)
        public static final String MOVIE_FROM_SEARCH = "MOVIE_FROM_SEARCH";
        public static final String MOVIE_SORT_TYPE = "MOVIE_SORT_TYPE";
        public static final String MOVIE_NEW_SORT_BY = "MOVIE_NEW_SORT_BY";
        public static final String SELECT_CONTACT_PARAMETER = "MOVIE_SELECT_CONTACT_PARAMETER";
        public static final String SHARE_MOVIE_CONTACT_DEFAULT = "SHARE_MOVIE_CONTACT_DEFAULT";
        public static final String SHARE_MOVIE_PHONE = "SHARE_MOVIE_PHONE";
        public static final String MOVIE_FROM_DP = "MOVIE_FROM_DP";
        public static final String BACK_ACTION_MOVIE = "BACK_ACTION_MOVIE";
        public static final String EXTERNAL_FROM_SAYCOMMAND = "EXTERNAL_FROM_SAYCOMMAND";
    }
    
    public static class StorageKeyForJSON {
        public static final String JSON_OBJECT_SEARCH_POI_ADDESS = "MOVIE_PAGE_JSON_ADDRESS";
        public static final String MOVIE_LIST_ARRAY_JSON = "MOVIE_LIST_ARRAY_JSON";
        public static final String MOVIE_SELECT_DATE_PARAMS_JSON = "MOVIE_DATE_PARAMS_JSON";
        public static final String MOVIE_SHARE_PARAMS_JSON = "MOVIE_SHARE_PARAMS_JSON";
        public static final String MOVIE_SHARE_RECIPIENTS_JSON = "MOVIE_SHARE_RECIPIENTS_JSON";
    }
    /** Keys for request/response values */
    public static class RRKey{
    	// Movie Search request-reply keys
    	public static final String MS_INPUT_STRING = "inputString"; 
    	public static final String MS_SORT_BY_NAME = "sortByName";
    	public static final String MS_NEW_SORT_BY = "newSortBy";
    	public static final String MS_BATCH_NUMBER = "batchNumber";
    	public static final String MS_BATCH_SIZE = "batchSize";
    	public static final String MS_ADDRESS = "addressString";
    	public static final String MS_DATE_INDEX = "dateIndex";
    	public static final String MS_DISTANCE_UNIT = "DU";
    	// share movie keys
    	public static final String SM_RECIPIENT = "recipient";
    	public static final String SM_RECIPIENT_NUMBER = "r_number";
    	// generic movie keys
    	public static final String M_ID = "id";
    	public static final String M_NAME = "name";
    	public static final String M_VENDOR_ID = "vendor";
    	public static final String M_CAST = "cast";
    	public static final String M_DIRECTOR = "director";
    	public static final String M_RATING = "rating";
    	public static final String M_GRADE = "grade";
    	public static final String M_GENRES = "genres";
    	public static final String M_DESCRIPTION = "description";
    	public static final String M_SHOW_TIME = "showTime";
    	public static final String M_SML_IMAGE_ID = "sm_img";
    	public static final String M_BIG_IMAGE_ID = "big_img";
    	public static final String M_RUNTIME= "runTime";
    	
    	public static final String M_THEATER_ID = "tId";
    	public static final String M_THEATER_NAME = "tName";
    	public static final String M_THEATER_ADDRESS = "tAddr";
    	public static final String M_THEATER_DISTANCE = "tDist";
    	public static final String M_MOVIE_ID = "mId";
    	public static final String M_CALLBACK_FUNCTION = "cbFunc";
    	public static final String M_PAGE_URL = "url";
    	
    	
    }
 
}
