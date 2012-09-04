package com.telenav.cserver.movie.html.util;

public interface HtmlConstants {
    
	//4 means use can search date in next 4 days, plus today user can total search date for 5 days.
	//day0,1,2,3,4
	public static final int MOVIE_SEARCHDATE_MAXRANGE = 2;
	
	public static final int MOVIE_PAGESIZE = 20;
	public static final int MOVIE_SMALL_PAGE_SIZE = 10;
	
	public static final String PAGE_TYPE_SIMPLE = "simpleList";
	public static final String PAGE_TYPE_SUB = "subList";
	public static final String PAGE_TYPE_FULL = "fullList";
	public static final String PAGE_TYPE_AJAXSIMPE = "ajaxSimpleList";
	
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
        public static final String SELECT_CONTACT_PARAMETER = "MOVIE_SELECT_CONTACT_PARAMETER";
        public static final String SHARE_MOVIE_CONTACT_DEFAULT = "SHARE_MOVIE_CONTACT_DEFAULT";
        public static final String SHARE_MOVIE_PHONE = "SHARE_MOVIE_PHONE";
        public static final String MOVIE_FROM_DP = "MOVIE_FROM_DP";
        public static final String BACK_ACTION_MOVIE = "BACK_ACTION_MOVIE";
        public static final String EXTERNAL_FROM_SAYCOMMAND = "EXTERNAL_FROM_SAYCOMMAND";
    }
    
    /** Keys for request/response values */
    public static class RRKey{
    	// Movie Search request-reply keys
    	public static final String MS_INPUT_STRING = "inputString"; 
    	public static final String MS_SORT_BY_NAME = "sortByName";
    	public static final String MS_BATCH_NUMBER = "batchNumber";
    	public static final String MS_BATCH_SIZE = "batchSize";
    	public static final String MS_ADDRESS = "addressString";
    	public static final String MS_DATE_INDEX = "dateIndex";
    	public static final String MS_DISTANCE_UNIT = "distanceUnit";
    	public static final String MS_THEATER_ID = "theaterId";
    	public static final String MS_START_INDEX = "startIndex";
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
    	public static final String M_SCHEDULE_ITEM = "scheduleItem";
    	public static final String M_THEATER_LIST= "theaterList";
    	public static final String M_THEATER_ID= "theaterId";
    	
    	//get ticket quantity keys
    	public static final String GTQ_MOVIE_ID = "movieId";
    	public static final String GTQ_THEATER_ID = "theaterId";
    	public static final String GTQ_SHOW_TIME = "showTime";
    	public static final String GTQ_SHOW_DATE = "showDate";
    	public static final String GTQ_TICKET_LIST = "ticketList";
    	public static final String GTQ_TICKET_ID = "ticketId";
    	public static final String GTQ_TICKET_TYPE = "type";
    	public static final String GTQ_TICKET_PRICE = "price";
    	public static final String GTQ_TICKET_CURRENCY = "currency";
    	public static final String GTQ_TICKET_QUANTITY = "quantity";
    	
    	//load images keys
    	public static final String LM_MOVIE_ID = "movieIds";
    	public static final String LM_IMAGES = "images";
    	
    	//for theater
    	public static final String T_ID = "id";
    	public static final String T_NAME = "name";
    	public static final String T_ADDRESS = "address";
    	public static final String T_DISTANCE = "distance";
    	public static final String T_DISTANCE_UNIT = "distanceUnit";
    	public static final String T_SCHEDULE_ITEM = "scheduleItem";
    	public static final String T_MOVIE_LIST= "movieList";
    	//for schedule
    	public static final String S_ID = "id";
    	public static final String S_MOVIEID = "movieId";
    	public static final String S_THEATERID = "theaterId";
    	public static final String S_VENDORNAME = "vendorName";
    	public static final String S_VENDOR_THEATERID = "vendorTheaterId";
    	public static final String S_TICKETURL = "ticketURI";
    	public static final String S_SHOWTIMES = "showTimes";
    	//
    	public static final String M_CALLBACK_FUNCTION = "cbFunc";
    	public static final String M_PAGE_URL = "url";
    	
    	
    }
 
}
