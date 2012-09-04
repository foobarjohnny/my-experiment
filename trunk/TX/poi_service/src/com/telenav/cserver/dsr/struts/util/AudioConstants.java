/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.dsr.struts.util;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-4-29
 */
public interface AudioConstants {
	
	/*
	No match found.                                        241
    Lets try again.                                       242
           
	Say the business name or category.                     216
	such as Coffee, or Coffee in Las Vegas, Nevada.        240
	such as Coffee.                                        275
	Searching nearby businesses.                           243 
	Searching businesses around                            245

	Say the city and state.                                235
	such as Los Angeles, California.                       115
	
	Say the airport name or city.                          117
	such as San Francisco Airport.                         118
	
	Say the address.                                       112
	Say the full address.                                  138
	Say the full address,such as 1130 Kifer Road, Sunnyvale, California.  113
	
	
	Say the intersection                                   119
	such as Mary Avenue at Fremont Avenue, Sunnyvale, California.120
	
	Say a command.                                         129
	Click trackball for examples                           139

    Getting map of                                         121
	Say 1 or press number 1 to select it.                  250
	Resuming your last trip                                140
	Current Location                                       141
	
	
	
    click the green Talk key and say an order number at anytime.            144
    Please say the order number of the item you want to select, such as 1.  147
    Please say an valid number, such as 1                                   148
    Sorry. I didnt get that. Lets try that again. If there is too much background noise   151
 
    Sorry. I didnt get that.                                               86
    You can click trackball at any time to end voice input.                 143
    
    ending beep												70
    I found multiple results                               246
	*/
	
	public static final String STATIC_AUDIO_COMMON_MULTIMATCH1 = "246";
	
    //No match found. Lets try again.
    public static final String STATIC_AUDIO_COMMON_NOMATCH = "241,242";
    //Click the green Talk key and say an order number at anytime
    public static final String STATIC_AUDIO_COMMON_MULTIMATCH = "144";  
    //Say 1 or press number 1 to select it.
    //public static final String STATIC_AUDIO_COMMON_NUMBER1 = "250";    
	//static audio for speak search
	//Say the business name or category. 
	public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO1 = "216";
	//Say the business name or category, such as Coffee, or Coffee in Las Vegas, Nevada. 
	public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO2 = "216,240";
	//No match found. Say the business name or category, such as Coffee, or Coffee in Las Vegas, Nevada. 
	public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO3 = "241,216,240";
	//No match found. Say the business name or category. 
	public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO4 = "241,216";
    //Say the business name or category. 
    public static final String STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO1 = "216";
    //Say the business name or category, such as Coffee. 
    public static final String STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO2 = "216,275";
    //No match found. Say the business name or category, such as Coffee. 
    public static final String STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO3 = "241,216,275";
    //No match found. Say the business name or category. 
    public static final String STATIC_AUDIO_SPEAKALONGSEARCH_AUDIO4 = "241,216";
	//Searching nearby businesses.
	//public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO5 = "243";
	//Searching businesses around Los Angeles.
	//public static final String STATIC_AUDIO_SPEAKSEARCH_AUDIO6 = "245";

	//static audio for city/state
	//Say the city and state.
	public static final String STATIC_AUDIO_CITY_AUDIO1 = "235";
	//Say the city and state, such as Los Angeles, California.
	public static final String STATIC_AUDIO_CITY_AUDIO2 = "235,115";
    //No match found. Say the city and state, such as Los Angeles, California. 
    public static final String STATIC_AUDIO_CITY_AUDIO3 = "241,235,115";
    //No match found. Say the city and state. 
    public static final String STATIC_AUDIO_CITY_AUDIO4 = "241,235";
    
    //static audio for airport
    //Say the airport name or city. 
    public static final String STATIC_AUDIO_AIRPORT_AUDIO1 = "117";
    //Say the airport name or city, such as San Francisco Airport. 
    public static final String STATIC_AUDIO_AIRPORT_AUDIO2 = "117,118";
    //No match found. Say the airport name or city, such as San Francisco Airport. 
    public static final String STATIC_AUDIO_AIRPORT_AUDIO3 = "241,117,118";
    //No match found. Say the airport name or city. 
    public static final String STATIC_AUDIO_AIRPORT_AUDIO4 = "241,117";
    
    //static audio for address
    //Say the address.
    public static final String STATIC_AUDIO_ADDRESS_AUDIO1 = "112";
    //Say the full address, such as 1130 Kifer Road, Sunnyvale, California. 
    public static final String STATIC_AUDIO_ADDRESS_AUDIO2 = "113";
    //No match found. Say the full address, such as 1130 Kifer Road, Sunnyvale, California. 
    public static final String STATIC_AUDIO_ADDRESS_AUDIO3 = "241,113";
    //No match found. Say the full address. 
    public static final String STATIC_AUDIO_ADDRESS_AUDIO4 = "241,138";

    
    //static audio for intersection
    //Say the intersection. 
    public static final String STATIC_AUDIO_INTERSECTION_AUDIO1 = "119";
    //Say the intersection, such as Mary Avenue at Fremont Avenue, Sunnyvale, California. 
    public static final String STATIC_AUDIO_INTERSECTION_AUDIO2 = "119,120";
    //No match found. Say the intersection, such as Mary Avenue at Fremont Avenue, Sunnyvale, California. 
    public static final String STATIC_AUDIO_INTERSECTION_AUDIO3 = "241,119,120";
    //No match found. Say the intersection. 
    public static final String STATIC_AUDIO_INTERSECTION_AUDIO4 = "241,119";

    //static audio for command and control
    //Say a command.
    public static final String STATIC_AUDIO_COMMAND_AUDIO1 = "129";
    //Say a command. Click trackball for examples
    public static final String STATIC_AUDIO_COMMAND_AUDIO2 = "129,139";
    //I didnt get that. Say a command. Click trackball for examples.
    public static final String STATIC_AUDIO_COMMAND_AUDIO3 = "86,129,139";
    //I didnt get that. Say a command.
    public static final String STATIC_AUDIO_COMMAND_AUDIO4 = "86,129";
    //Say a command. Click trackpad for examples
    public static final String STATIC_AUDIO_COMMAND_AUDIO5 = "129,276";
  //I didnt get that. Say a command. Click trackpad for examples.
    public static final String STATIC_AUDIO_COMMAND_AUDIO6 = "86,129,276";

    //Getting 
    public static final String STATIC_AUDIO_GETTING = "225";
    //To
    public static final String STATIC_AUDIO_TO = "30";
    //fastest route
    public static final String STATIC_AUDIO_ROUTESTYLE_FASTEST = "227";
    //shortest route
    public static final String STATIC_AUDIO_ROUTESTYLE_SHORTEST = "228";
    //prefer streets
    public static final String STATIC_AUDIO_ROUTESTYLE_STREET = "229";
    //prefer highways
    public static final String STATIC_AUDIO_ROUTESTYLE_HIGHWAY = "230";
    //use pedestrian
    public static final String STATIC_AUDIO_ROUTESTYLE_PEDESTRIAN = "231";
    //Getting map of 
    public static final String STATIC_AUDIO_GETTING_MAP = "121";
    
    //Resuming your last trip
    public static final String STATIC_AUDIO_RESUME_TRIP = "140";
    //Current Location
    public static final String STATIC_AUDIO_CURRENT_LOCATION = "141";
    
    //Please say the order number of the item you want to select, such as 1.
    public static final String STATIC_AUDIO_NUMBER_AUDIO1 = "147";
    //Please say an valid number, such as 1. 
    public static final String STATIC_AUDIO_NUMBER_AUDIO2 = "148";
    public static final String STATIC_AUDIO_NOMATCHFOUND = "241";

    //Sorry. I didnt get that. Lets try that again. If there is too much background noise. 
    //You can click trackball at any time to end voice input. 
    public static final String STATIC_AUDIO_ENDING_AUDIO1 = "151,143";
    
    //ending beep
    public static final String STATIC_AUDIO_ENDING_BEEP = "70";
    
	//timeout
	public static final int TIMEOUT_THREESCEONDS = 3000; 
	public static final int TIMEOUT_FOURSCEONDS = 4000;
	public static final int TIMEOUT_FIVESCEONDS = 5000;
	public static final int TIMEOUT_TWOSCEONDS = 2000;
	
	//DSR type
	//public final static int DSR_RECOGNIZE_CITY_STATE_NOLATLON = 0;
    //public final static int DSR_RECOGNIZE_STREET_ADDRESS = 2;
    //public final static int DSR_RECOGNIZE_1_STREET_NAME = 3;
    //public final static int DSR_RECOGNIZE_2_STREET_NAME = 4;
	public final static int DSR_RECOGNIZE_CITY_STATE = -1;
	public final static int DSR_RECOGNIZE_POI = 11;
	public final static int DSR_RECOGNIZE_SEARCHALONG = 1;
    public final static int DSR_RECOGNIZE_AIRPORT = 5;
    public final static int DSR_RECOGNIZE_ONE_SHOT_ADDRESS = 9;
    public final static int DSR_RECOGNIZE_ONE_SHOT_INTERSECTION = 10;
    public final static int DSR_RECOGNIZE_COMMAND_CONTROL = 6;
    public final static int DSR_RECOGNIZE_NUMBER = 8;
    
    public final static int DSR_RECOGNIZE_CITY_STATE_MAXSPEECHTIME = 6000;
    public final static int DSR_RECOGNIZE_POI_MAXSPEECHTIME = 10000;
    public final static int DSR_RECOGNIZE_AIRPORT_MAXSPEECHTIME = 6000;
    public final static int DSR_RECOGNIZE_ONE_SHOT_ADDRESS_MAXSPEECHTIME = 10000;
    public final static int DSR_RECOGNIZE_ONE_SHOT_INTERSECTION_MAXSPEECHTIME = 12000;
    public final static int DSR_RECOGNIZE_COMMAND_CONTROL_MAXSPEECHTIME = 10000;
    public final static int DSR_RECOGNIZE_NUMBER_MAXSPEECHTIME = 4000;
    
	//Command Text
	public final static String DSR_COMMAND_TEXT_DRIVETO = "drive";
	public final static String DSR_COMMAND_TEXT_MAPIT = "map";
	public final static String DSR_COMMAND_TEXT_SEARCH = "search";
	public final static String DSR_COMMAND_TEXT_RESUME= "resume";
	public final static String DSR_COMMAND_TEXT_CURRENTLOCATION= "current location";
	
	public final static String DSR_COMMAND_TEXT_SHOW = "show";
	public final static String DSR_COMMAND_TEXT_WEATHER = "weather";
	public final static String DSR_COMMAND_TEXT_TRAFFIC = "traffic";
	public final static String DSR_COMMAND_TEXT_COMMUTE = "commute";
	public final static String DSR_COMMAND_TEXT_MOVIE= "movie";
	public final static String DSR_COMMAND_TEXT_THEATER= "theater";
	
	public final static int DSR_COMMAND_VALUE_HOME = 105;
	//103 is map poi, 101 is search poi
	public final static int DSR_COMMAND_VALUE_POI = 103;
	public final static int DSR_COMMAND_VALUE_DRIVE_POI = 102;
	public final static int DSR_COMMAND_VALUE_CURRENTLOCATION = 107;
	public final static int DSR_COMMAND_VALUE_OFFICE = 108;
	//DSR return status
	public static final int DSR_STATUS_ERROR = -2;
	public static final int DSR_STATUS_TIMEOUT = -1;
	public static final int DSR_STATUS_SPEAK_START = 0;
	public static final int DSR_STATUS_SPEAK_FINISH = 1;
	public static final int DSR_STATUS_RECOGNIZE_FINISH = 3;
	public static final int DSR_STATUS_VOLUME_CHANGE = 4;
	//multiple size
	public static final int PAGE_SIZE = 5;
	//Screen TYPE
	public static final String SCREEN_TYPE_CITY = "City";
	public static final String SCREEN_TYPE_ADDRESS = "Address";
    public static final String SCREEN_TYPE_AIRPORT = "Airport";
    public static final String SCREEN_TYPE_INTERSECTION = "Intersection";	
    public static final String SCREEN_TYPE_SEARCH = "Search";
    public static final String SCREEN_TYPE_COMMAND = "Command";
    public static final String SCREEN_TYPE_INITIAL = "Speak";
    //Flow Type
    public static final String SCREEN_FLOW_ONE = "1"; 
    public static final String SCREEN_FLOW_TWO = "2";
    public static final String SCREEN_FLOW_THREE = "3";
    public static final String SCREEN_FLOW_FOUR = "4";
    
    public static final String LOG_TYPE_DSR = "100";
    
    //Cookie Key 
    public static class StorageKey {
        
        //DSR reply data
        /*
         * TxNode(DSR Retured)
         *      msg0=DATA_COMMAND
         *      Child0=DATA_TEXT
         *      Child1=DATA_AUDIO
         *      Child2=DATA_AUDIOPURE
         */
        public static final String AUDIO_RESULT_DATA_TEXT = "AUDIO_RESULT_DATA_TEXT";
        public static final String AUDIO_RESULT_DATA_AUDIO = "AUDIO_RESULT_DATA_AUDIO";
        public static final String AUDIO_RESULT_DATA_AUDIOPURE = "AUDIO_RESULT_DATA_AUDIOPURE";
        public static final String AUDIO_RESULT_DATA_AUDIO_ALONGROUTE = "AUDIO_RESULT_DATA_AUDIO_ALONGROUTE";
        public static final String AUDIO_RESULT_DATA_SELECTED_INDEX = "AUDIO_RESULT_DATA_SELECTED_INDEX";
        public static final String AUDIO_RESULT_DATA_SELECTED_TEXT = "AUDIO_RESULT_DATA_SELECTED_TEXT";
        public static final String AUDIO_RESULT_DATA_SELECTED_TEXTVALUE = "AUDIO_RESULT_DATA_SELECTED_TEXTVALUE";
        public static final String AUDIO_RESULT_DATA_TRXNID = "AUDIO_RESULT_DATA_TRXNID";
        public static final String AUDIO_RESULT_DATA_COMMAND = "AUDIO_RESULT_DATA_COMMAND";
        public static final String AUDIO_RESULT_DATA_SELECTED_ADDRESS = "AUDIO_RESULT_DATA_SELECTED_ADDRESS";
        public static final String AUDIO_RESULT_DATA_TYPE = "AUDIO_RESULT_DATA_TYPE";
        public static final String AUDIO_RESULT_DATA_DEFAULT_ADDRESS = "AUDIO_RESULT_DATA_DEFAULT_ADDRESS";
        public static final String AUDIO_RESULT_DATA_SPEECHTIME = "AUDIO_RESULT_DATA_SPEECHTIME";
        
        public static final String AUDIO_NORESUT_FROM = "AUDIO_NORESUT_FROM";
        public static final String AUDIO_NORESUT_FIRSTFALL = "AUDIO_NORESUT_FIRSTFALL";
        public static final String AUDIO_FROM_PARAMEPTER = "AUDIO_FROM_PARAMEPTER";
        public static final String AUDIO_MULTIPLE_RESULT_INDEX = "AUDIO_MULTIPLE_RESULT_INDEX";
        public static final String AUDIO_MULTIPLE_RESULT_CONTINUE_FLAG = "AUDIO_MULTIPLE_RESULT_CONTINUE_FLAG";
        public static final String AUDIO_SPEAK_NUMBER_CONTINUE_FLAG = "AUDIO_SPEAK_NUMBER_CONTINUE_FLAG";
        public static final String AUDIO_POI_SEARCHALONG_DATA = "AUDIO_POI_SEARCHALONG_DATA";
        public static final String AUDIO_FLOW3_CANCELURL = "AUDIO_FLOW3_CANCELURL";
        public static final String AUDIO_TIMEOUT_COUNT = "AUDIO_TIMEOUT_COUNT";
        
        public static final String AUDIO_POI_PARAMETER = "AUDIO_POI_PARAMETER";
        public static final String AUDIO_POI_FROM_SEARCHALONG = "searchalong";
        
        public static final String AUDIO_SAYCOMMAND_COMMAND = "AUDIO_SAYCOMMAND_COMMAND";
        
        public static final String AUDIO_SAYNUMBER_MAXSIZE = "AUDIO_SAYNUMBER_MAXSIZE";
        public static final String AUDIO_SAYNUMBER_AVAILABLE = "AUDIO_SAYNUMBER_AVAILABLE";
        public static final String AUDIO_SEARCHALONG_AHEAD_FLAG = "AUDIO_SEARCHALONG_AHEAD_FLAG";
        
        public static final String AUDIO_SEARCH_INST_FLAG = "AUDIO_SEARCH_INST_FLAG";
        public static final String EXTERNAL_FROM_SAYCOMMAND = "EXTERNAL_FROM_SAYCOMMAND";
    }
}
