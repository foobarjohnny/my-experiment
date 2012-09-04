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
package com.telenav.cserver.browser.framework;

/**
 * @author lwei
 * 
 */
public class BrowserFrameworkConstants {
    public static final String CLIENT_INFO = "CLIENT_INFO";
    public static final String MESSAGE_KEY = "MESSAGE_KEY";
    public static final String IMAGE_KEY = "IMAGE_KEY";
    public static final String IMAGE_KEY_WITHOUT_SIZE = "IMAGE_KEY_WITHOUT_SIZE";
    public static final String LOCALE_KEY = "LOCALE_KEY";
    
	public static final String LOCALSERVICE_SYNCRESOURCE = "SyncResource";
	public static final String LOCALSERVICE_CONTACT = "SelectContact";
	public static final String LOCALSERVICE_CONTACT_ADDRESS = "SelectAddressFromContact";
	public static final String LOCALSERVICE_DRIVETO_BACK = "BackFromSendETA";
	public static final String LOCALSERVICE_MAKEPHONECALL = "makePhoneCall";
	public static final String LOCALSERVICE_SYNCADDRESS = "SyncAddress";
	public static final String LOCALSERVICE_INVOKEPHONEBROWSER = "LocalService_invokePhoneBrowser";
	public static final String LOCALSERVICE_DIAGNOSTIC = "ShowDiagnostic";
	public static final String LOCALSERVICE_SWITCHAUDIOPATH = "SwitchAudioPath";
	public static final String LOCALSERVICE_GETNONBLOCKINGGPS = "getNonBlockingGPS";

	public static final class CurrentLocation {
		public static final int GPS = 0;
		public static final int CURRENT_LOCATION = 1;
		public static final int LAST_KNOWN = 2;

	}
	
    public static class StorageKey {
    	//poi server
    	public static final String SELECT_ADDRESS_PARAMETER = "SELECT_ADDRESS_PARAMETER";
		public static final String NAMEANDEMAIL_CALLBACK_URL = "NAMEANDEMAIL_CALLBACK_URL";
		public static final String NAMEANDEMAIL_CALLBACK_FUNC = "NAMEANDEMAIL_CALLBACK_FUNC";
		public static final String SOURCE_FOR_NAMEANDEMAIL = "SOURCE_FOR_NAMEANDEMAIL";
		
		public static final String SELECT_CONTACT_PARAMETER = "SELECT_CONTACT_PARAMETER";
		public static final String SHARE_SELECT_CONTACT_DEFAULT = "SHARE_SELECT_CONTACT_DEFAULT";
		public static final String SELECTCONTACT_RUNNING = "SELECTCONTACT_RUNNING";
		
		public static final String STOP_TYPE = "STOP_TYPE";
		public static final String NAV_TYPE = "NAV_TYPE";
		public static final String ATT_MAPS_STATIC_ROUTE = "ATT_MAPS_STATIC_ROUTE";
		
		//movie server
		public static final String BACK_ACTION_MOVIE = "BACK_ACTION_MOVIE";
		
    }
    
    public static class StorageKeyForJSON {
    	//post location server
        public static final String POST_LOCATION_ADDRESS = "POST_LOCATION_ADDRESS";
    }   
}
