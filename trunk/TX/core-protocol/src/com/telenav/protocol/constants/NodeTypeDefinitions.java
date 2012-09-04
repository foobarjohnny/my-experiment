/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.protocol.constants;

/**
 * NodeTypeDefinitions.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-1
 *
 */
public interface NodeTypeDefinitions 
{

	public final static int TYPE_AUDIO = 0;
	public final static int TYPE_RULES = 1;
	
	public final static int TYPE_COMMON_BAR_COLLECTION_DEL = 600;
	public final static int TYPE_LOCALE_BAR_COLLECTION_DEL = 601;
	
	public final static int TYPE_COMMON_BAR_COLLECTION = 100;
	public final static int TYPE_LOCALE_BAR_COLLECTION = 101;	
	public final static int TYPE_SERVICE_MAPPING = 102;	
	public final static int TYPE_SERVER_DRIVEN_PARAMETERS = 108;
	public final static int TYPE_BROWSER_PAGE_VERSION = 109;
	public final static int TYPE_UPGRADE_INFORMATION = 110;
	public final static int TYPE_ADDRESS_SHARED_NUMBER = 111;
	public final static int TYPE_SYNC_RESOURCE_FLAG = 112;
	public final static int TYPE_ACCOUNT_STATUS = 113;
	public final static int TYPE_NEW_APP_INFORMATION = 114;
	public final static int TYPE_DATA_PROVIDER=115;
			
	
	public static final int TYPE_HAS_REMAINDER = 160;
	
	public static final int TYPE_SPT = 209;
	
	public static final int TYPE_ROUTE_DYNAMIC_AUDIO = 260;
	public static final int TYPE_MAP_TILES = 261;
	public static final int TYPE_SEGMENT_LANINFO = 262;
	public static final int TYPE_RESOURCE_FORMAT = 263;
	public static final int TYPE_ROUTE_DESTINATION_AUDIO = 264;
	public static final int TYPE_TRAFFIC_REPORT_LOCATION = 265;
	public static final int TYPE_GEOCODED_REVERSE_ROUTE_DESTINATION=266;
	public static final int TYPE_GEOCODED_DESTINATION=267;

	
	public static final int TYPE_TRAFFIC_ALERT_IDS = 270;
	public static final int TYPE_TRAFFIC_FILTER = 271;
	
	public static final int TYPE_HOME_ADDRESS_STOP = 273;
	public static final int TYPE_OFFICE_ADDRESS_STOP = 274;
	

	/**
	 * common c-server
	 */

	/**
	 * user information(email, firstname, lastname, etc)
	 */
	public static final int TYPE_USER_INFORMATION = 301;
	
	/**
	 * guide tone information(SANTA CLAUS)
	 */
	public static final int TYPE_GUIDE_TONE_INFORMATION = 302;
	
	/**
	 * office/home address upload
	 */
	public static final int TYPE_HOME_OFFICE_ADDRESS=303;
    //sync stops
	public final static int TYPE_ADDRESS_SHARING_NUMBER = 304;
	
	//airport
	public final static int TYPE_AIRPORT=305;
	
	/**
	 * For map node in traffic reroute-related executor
	 */
	public final static String TYPE_SCREEN_NODE="TYPE_SCREEN_NODE";
	
	//poi node type
	public final static int TYPE_ONE_BOX_NODE=116;
	
	public final static int TYPE_ONE_BOX_ROUTE_NODE=117;
	
	public final static int TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE=118;
	
	public final static int TYPE_ONE_BOX_ADDRESS_SHARING_DEST_NODE=119;
	
	public final static int TYPE_CONTACT_INFO_NODE=120;
	
	public final static int TYPE_SUGGESSION_NODE=121;
	
	public final static int TYPE_SUPPLEMENTINFO_NODE=122;
	
	public final static int TYPE_REVIEWEDETAILS_NODE=123;
	
	public final static int TYPE_POI_AD_NODE=124;
	
	public final static int TYPE_OPENTABLE_INFO_NODE=125;
	
	public final static int TYPE_COUPON_NODE=126;
	
	public final static int TYPE_SPONSOR_POI_NODE=127;
	
	//for billing part
	public final static int TYPE_USER_INFO_NODE=128;
	
	public final static int TYPE_ACCOUNT_INFO_NODE=129;
	
	public final static int TYPE_PURCHASE_INFO_NODE=130;
	
	public final static int TYPE_PURCHASE_PRODUCT_INFO_NODE=131;
	
	public final static int TYPE_CUSTOMER_BUSINESS_CAT_LIST=132;
	
	public final static int TYPE_CUSTOMER_BUSINESS_CAT=133;
	
	public final static int TYPE_POI_AUDIO_ITEM=134;
	
	public final static int TYPE_POI_MSG_AUDIO=135;
	
	public final static int TYPE_POI_AUDIO_PROMPT=136;
	
	public final static int TYPE_POI_INT_ARGUMENTS=137;
	
	public final static int TYPE_POI_NODE_ARGUMENTS=138;
	
	public final static int TYPE_HOTBRAND = 139;
	
	public final static int TYPE_AIRPORT_WITH_SHOW_MSG = 140;
	
	public final static int TYPE_POI_AUDIO_SEQUENCE = 141;

    public final static int TYPE_BANNER_ADS = 142;
    
    public final static int TYPE_DSM_INFO_NODE = 143;
    
    public final static int TYPE_SENT_ADDRESS_NODE = 144;
    
    public final static int TYPE_ADDRESS_RECEIVER_NODE = 145;
    
    public final static int TYPE_SIMPLE_STOP_NODE = 146;
    
    public final static int TYPE_ONEBOX_ADDRESS_NODE = 147;
    
    public final static int TYPE_MAP_DATA_UPGRADE_NODE = 148;
    
    public final static int TYPE_PURCHASED_CAR_ICON = 149;
    
    public final static int TYPE_UNPURCHASED_CAR_ICON = 150;
    
    public final static int TYPE_PURCHASED_VOICE = 151;
    
    public final static int TYPE_UNPURCHASED_VOICE = 152;

    public final static int TYPE_SYNC_RESOURCE_NODE = 153;

    public final static int TYPE_SYNC_RESOURCE_VERSION_NODE = 154;
    
    public final static int TYPE_SYNCPURCHASED_SUBSCRITIONINFO_NODE = 155;
    
    public final static int TYPE_DSR_SDP_NODE = 156;
    
    public final static int TYPE_CARICON_NODE = 171;
	
}
