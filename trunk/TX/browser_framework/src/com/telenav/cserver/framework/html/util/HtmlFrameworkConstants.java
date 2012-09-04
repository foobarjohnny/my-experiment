/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 22, 2009
 * File name: BrowserFrameworkConstants.java
 * Package name: com.telenav.cserver.browser.framework
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 12:34:52 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.framework.html.util;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlFrameworkConstants {
	
	public static final int MOVIE_SEARCHDATE_MAXRANGE = 999;
    public static final String HTML_CLIENT_INFO = "HTML_CLIENT_INFO";
    public static final String HTML_MESSAGE_KEY = "HTML_MESSAGE_KEY";
    //public static final String HTML_IMAGE_KEY = "HTML_IMAGE_KEY";
    //public static final String HTML_CSS_KEY = "HTML_CSS_KEY";
    //public static final String HTML_IMAGE_KEY_WITHOUT_SIZE = "HTML_IMAGE_KEY_WITHOUT_SIZE";
    //public static final String HTML_DEVICENAME_KEY = "HTML_DEVICENAME_KEY";
    //public static final String HTML_MANIFEST_KEY = "HTML_MANIFEST_KEY";
    //public static final String HTML_SHARED_IMAGE_KEY = "HTML_SHARED_IMAGE_KEY";
    //public static final String HTML_SHARED_CSS_KEY = "HTML_SHARED_CSS_KEY";
    
    //new path used resource server
    public static final String FULLPATH_CSS = "cssPath";
    public static final String FULLPATH_CSSPROG = "cssProgPath";
    public static final String FULLPATH_IMAGE = "imageKey";
    public static final String FULLPATH_SHARED_IMAGE = "sharedImageKey";
    public static final String FULLPATH_JS_COMMON = "commonJsPath";
    public static final String FULLPATH_JS = "jsPath";
    public static final String FULLPATH_CSS_DEVCIECOMMON_KEY = "cssDeviceCommonPath";
    public static final String FULLPATH_SHARED_CSS_DEVCIECOMMON_KEY = "cssProgDeviceCommonPath";
    public static final String FULLPATH_AUDIO = "audioPath";
    
    public static final String META_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String RESPONSE_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String VIEW_POINT = "target-densitydpi=device-dpi, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no";
    
    public static final String CLIENT_INFO_KEY_PROGRAMECODE = "programCode";
    public static final String CLIENT_INFO_KEY_DEVICECARRIER = "deviceCarrier";
    public static final String CLIENT_INFO_KEY_PLATFORM = "platform";
    public static final String CLIENT_INFO_KEY_VERSION = "version";
    public static final String CLIENT_INFO_KEY_PRODUCTTYPE = "productType";
    public static final String CLIENT_INFO_KEY_DEVICE = "device";
    public static final String CLIENT_INFO_KEY_BUILDNUMBER = "buildNumber";
    public static final String CLIENT_INFO_KEY_LOCALE = "locale";
    public static final String CLIENT_INFO_KEY_REGION = "region";
    public static final String CLIENT_INFO_KEY_WIDTH = "width";
    public static final String CLIENT_INFO_KEY_HEIGHT = "height";
    public static final String CLIENT_INFO_KEY_SSOTOKEN = "ssoToken";
    
    //cli key
    public static final String CLI_KEY_USERID = "userId";
    public static final String CLI_KEY_MACADDRESS = "macAddress";
    public static final String CLI_KEY_EMAIL = "email";
    
    
    public static final int DUNIT_MILES = 1;
    public static final String DUNIT_DISPLAY_KPH = "KM/H";
    
    public static class FEATURE {
    	//if support movie feature, if value is "Y", poidetail will dispay ShowTime tab when it's theater
    	public static final String FEATURE_MOVIE = "MOVIE";
    	//if support hotel feature, if value is "Y", poidetail will display Hotel specific logic, now the code is removed
    	public static final String FEATURE_HOTEL = "HOTEL";
    	//if support OpenTable feature, if value is "Y", poidetail will display OpenTable specific logic, now the code is removed
    	public static final String FEATURE_RESTAURANT = "RESTAURANT";
    	//if View Review screen need add back button, it need display back button for iOS, it's not used anymore. 
    	//use isPhone to check if should add back button in view and don't display title for About
    	public static final String FEATURE_BACKBUTTON = "BACKBUTTON";
    	//if Movie function can buy ticket.
    	public static final String FEATURE_MOVIE_BUY = "MOVIE_BUY";
    	//if it will use Scout Style UI.
    	public static final String SCOUTSTYLE = "SCOUTSTYLE";
    	//if poidetail map use OpenGL map
    	public static final String OPENGLMAP = "OPENGLMAP";
    	//weather->Date format
    	public static final String WEATHERDATEFORMAT = "WEATHER_DATEFORMAT";
    	//weather->if use backend 2.0 API. this api will return day/night icons
    	public static final String WEATHER_ORIGINAL = "WEATHER_ORIGINAL";
    	//the Poi Logo Theme
    	public static final String POI_LOGO_THEME = "POI_LOGO_THEME";
    	//if support Yelp integration
    	public static final String YELP = "YELP";
    	//if support GasPrcie tab. it value is "N", does not display GasPrice tab in poidetail
    	public static final String GASPRICE = "GASPRICE";
    	//if support Trip Advisor integration
    	public static final String TRIPADVISOR = "TRIPADVISOR";
    }
    
    public static class SERVICE {
    	public static final String CHECKOUT = "CHECKOUT";
    	public static final String RESOURCE = "RESOURCE";
    }
}
