/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

/**
 * WebServiceConfigurator to maintain related configuration with WebService
 * Server
 *
 * @author panzhang
 * @version 1.0 
 * 2011-3-14 10:23:08
 */
public class WebServiceConfigurator {

    private static final String URL_BANNER_ADS         = "URL_BANNER_ADS";
    private static final String URL_MSG_SERVICE        = "URL_MSG_SERVICE";
    private static final String URL_ONE_BOX_SEARCH     = "URL_ONE_BOX_SEARCH";
    private static final String URL_MOVIE_SEARCH       = "URL_MOVIE_SEARCH";
    private static final String URL_TRAFFIC_SERVER     = "URL_TRAFFIC_SERVER";
    private static final String URL_POI_REVIEW_WRITE = "URL_POI_REVIEW_WRITE";
    private static final String URL_LOGO_IMAGE = "URL_GETLOGOIMAGE";
    private static final String URL_POI_DETAIL = "URL_POI_DETAIL";
    private static final String URL_POI_DETAIL20 = "URL_POI_DETAIL20";
    private static final String URL_BILLING_BOARD = "URL_BILLING_BOARD";
    private static final String URL_ORGANIC_ADS_SEARCH = "URL_ORGANIC_ADS_SEARCH";
    private static final String TRAFFIC_ALERT_DATA_SRC = "TRAFFIC_ALERT_DATA_SRC";
    private static final String TRAFFIC_FLOW_DATA_SRC  = "TRAFFIC_FLOW_DATA_SRC";
    private static final String URL_FEEDBACK_SERVER = "URL_FEEDBACK_SERVER";
    private static final String URL_STATIC_MAP = "URL_STATIC_MAP";
    private static final String STATIC_MAP_APIKEY = "STATIC_MAP_APIKEY";
    private static final String URL_RESTAURANT_SERVICE = "URL_RESTAURANT_SERVICE";
    
    private static String CONFIG_FILE = "config.web_services";

    // log4j logger
    protected static Logger logger = Logger.getLogger(WebServiceConfigurator.class);

    private static String urlOfWeather = "";
    /** URL for banner ads web service*/
    private static String urlOfBannerAds = "";
    /** URL for msg service used for sending email */
    private static String urlOfMsgService = "";
    /** URL for one box search*/
    private static String urlOfOneBox = "";
    /** URL for poi review write */
    private static String urlOfPoiReviewWrite = "";
    /** URL for logo image*/
    private static String urlOfLogoImage = "";
    /** URL for poi detail */
    private static String urlOfPoiDetail = "";
    /** URL for poi detail */
    private static String urlOfPoiDetail20 = "";
    /** URL for virtual billing board*/
    private static String urlOfBillingBoard = "";
    /** URL for organic ads search */
    private static String urlOfOrganicAdsSearch = "";
    /** URL for movie search (for ads displaying new, popular movies, etc */
    private static String urlOfMovieSearch = "";
    /** URL for traffic (for ads displaying traffic incidents information */
    private static String urlOfTrafficServer = "";
    private static String       trafficAlertDataSrc    = "";
    private static String       trafficFlowDataSrc     = "";
    
    /** URL for feedback service */
    private static String       urlOfFeedbackServer     = "";
    /** URL for static map*/
    private static String urlOfStaticMap = "";
    private static String staticMapKey = "";
    /**URL for OpenTable*/
    private static String urlOfRestaurant = "";
    


	static {
        PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle
                .getBundle(CONFIG_FILE);
        urlOfWeather = bundle.getString("URL_WEATHER");
        urlOfBannerAds = bundle.getString(URL_BANNER_ADS);
        urlOfMsgService = bundle.getString(URL_MSG_SERVICE);
        urlOfOneBox = bundle.getString(URL_ONE_BOX_SEARCH);
        urlOfMovieSearch = bundle.getString(URL_MOVIE_SEARCH);
        urlOfTrafficServer = bundle.getString(URL_TRAFFIC_SERVER);
        urlOfPoiReviewWrite = bundle.getString(URL_POI_REVIEW_WRITE);
        urlOfLogoImage = bundle.getString(URL_LOGO_IMAGE);
        urlOfPoiDetail = bundle.getString(URL_POI_DETAIL);
        urlOfPoiDetail20 = bundle.getString(URL_POI_DETAIL20);
        urlOfBillingBoard = bundle.getString(URL_BILLING_BOARD);
        urlOfOrganicAdsSearch = bundle.getString(URL_ORGANIC_ADS_SEARCH);
        trafficAlertDataSrc = bundle.getString(TRAFFIC_ALERT_DATA_SRC);
        trafficFlowDataSrc = bundle.getString(TRAFFIC_FLOW_DATA_SRC);
		urlOfFeedbackServer = bundle.getString(URL_FEEDBACK_SERVER);
        urlOfStaticMap = bundle.getString(URL_STATIC_MAP);
        staticMapKey = bundle.getString(STATIC_MAP_APIKEY);
        urlOfRestaurant = bundle.getString(URL_RESTAURANT_SERVICE);

        if (logger.isInfoEnabled()) {
            logger.info("URL_WEATHER:" + urlOfWeather);
            logger.info(URL_BANNER_ADS+urlOfBannerAds);
            logger.info(URL_MSG_SERVICE+urlOfMsgService);
            logger.info(URL_ONE_BOX_SEARCH+urlOfOneBox);
            logger.info(URL_MOVIE_SEARCH+urlOfMovieSearch);
            logger.info(URL_TRAFFIC_SERVER+urlOfTrafficServer);
            logger.info(URL_FEEDBACK_SERVER + ":" + urlOfFeedbackServer);
            logger.info(TRAFFIC_ALERT_DATA_SRC+trafficAlertDataSrc);
            logger.info(TRAFFIC_FLOW_DATA_SRC+trafficFlowDataSrc);
            logger.info(URL_POI_REVIEW_WRITE+urlOfPoiReviewWrite);
            logger.info(URL_LOGO_IMAGE+urlOfLogoImage);
            logger.info(URL_POI_DETAIL+urlOfPoiDetail);
            logger.info(URL_BILLING_BOARD+urlOfBillingBoard);
            logger.info(URL_STATIC_MAP+urlOfStaticMap);
            logger.info(STATIC_MAP_APIKEY+staticMapKey);
            logger.info(URL_RESTAURANT_SERVICE+urlOfRestaurant);
        }
    }
    
    /**
	 * @return the urlOfOneBox
	 */
	public static String getUrlOfOneBox() {
		return urlOfOneBox;
	}

    /**
	 * @return the urlOfMovieSearch
	 */
	public static String getUrlOfMovieSearch() {
		return urlOfMovieSearch;
	}

    /**
     * @return the urlOfTrafficServer
     */
    public static String getUrlOfTrafficServer()
    {
        return urlOfTrafficServer;
    }
    
    public static String getTrafficAlertDataSrc()
    {
        return trafficAlertDataSrc;
    }

    public static String getTrafficFlowDataSrc()
    {
        return trafficFlowDataSrc;
    }

    /**
     * return URL for Weather service
     *
     * @return
     */
    public static String getUrlOfWeather() {
        return urlOfWeather;
    }


    public static String getUrlOfBannerAds() {
        return urlOfBannerAds;
    }
    
    public static String getUrlOfPoiReviewWrite(){
    	return urlOfPoiReviewWrite;
    }

    public static void setUrlOfBannerAds(String urlOfBannerAds) {
        WebServiceConfigurator.urlOfBannerAds = urlOfBannerAds;
    }

    public static String getUrlOfMsgService() {
        return urlOfMsgService;
    }

    public static void setUrlOfMsgService(String urlOfMsgService) {
        WebServiceConfigurator.urlOfMsgService = urlOfMsgService;
    }

	public static String getUrlOfLogoImage() {
		return urlOfLogoImage;
	}

	public static void setUrlOfLogoImage(String urlOfLogoImage) {
		WebServiceConfigurator.urlOfLogoImage = urlOfLogoImage;
	}

	public static String getUrlOfPoiDetail() {
		return urlOfPoiDetail;
	}

	public static void setUrlOfPoiDetail(String urlOfPoiDetail) {
		WebServiceConfigurator.urlOfPoiDetail = urlOfPoiDetail;
	}

	public static String getUrlOfBillingBoard() {
		return urlOfBillingBoard;
	}

	public static void setUrlOfBillingBoard(String urlOfBillingBoard) {
		WebServiceConfigurator.urlOfBillingBoard = urlOfBillingBoard;
	}
    
    
    public static String getUrlOfOrganicAdsSearch()
    {
        return urlOfOrganicAdsSearch;
    }

    public static void setUrlOfOrganicAdsSearch(String urlOfOrganicAdsSearch)
    {
        WebServiceConfigurator.urlOfOrganicAdsSearch = urlOfOrganicAdsSearch;
    }

	public static String getUrlOfStaticMap() {
		return urlOfStaticMap;
	}
	
	public static String getUrlOfFeedbackServer() {
    	return urlOfFeedbackServer;
    }
    
    public static void setUrlOfFeedbackServer(String urlOfFeedbackServer) {
    	WebServiceConfigurator.urlOfFeedbackServer = urlOfFeedbackServer;
    }

	public static String getStaticMapKey() {
		return staticMapKey;
	}

	public static void setStaticMapKey(String staticMapKey) {
		WebServiceConfigurator.staticMapKey = staticMapKey;
	}
	
    public static String getUrlOfRestaurant() {
		return urlOfRestaurant;
	}

	public static String getUrlOfPoiDetail20() {
		return urlOfPoiDetail20;
	}

}
