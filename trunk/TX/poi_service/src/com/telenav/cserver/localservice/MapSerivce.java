/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Jul 8, 2009
 * File name: MapSerivce.java
 * Package name: com.telenav.cserver.localservice
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 7:34:58 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.localservice;

/**
 * @author lwei
 */
public class MapSerivce {
	public static class Key {
		public static final String SCREEN_FEATURE = "ScreenFeatures";
		public static final String SCREEN_FEATURE_INDEX = "ScreenFeaturesIndex";
		public static final String MENU_ITEMS = "MenuItems";
		public static final String ACTION_FOR_ADDRESS = "ActionsForAddress";
		public static final String MAP_TYPE = "MapType";
		public static final String SERVICE_TYPE = "ServiceType";
		public static final String KEY_I_POI_SELECTED_INDEX = "SelectedIndex";
		public static final String KEY_A_POI_ADS_LIST = "PoiAdsList";
		public static final String KEY_I_SELECTED_POI_ADS_INDEX = "SelectedPoiAdsIndex";
	}

	public static class ServiceType {
		public static final String SHOW_MAP = "ShowMap";
		public static final String RESUME = "Resume";
	}

	public static class MapType {
		public static final int FOLLOW_ME = 0;
		public static final int SINGLE_ADDRESS = 1;
		public static final int POI = 2;
		public static final int COMMUT_ALERT = 3;
		public static final int SUMMARY = 4;

		/**
		 * According to Sean Xia, the entry for map in main screen should be
		 * last known location not follow me. So follow me map is not available
		 * from browser pages.
		 */
		public static final int LAST_KNOWN_LOCATION = 5;
		public static final int POI_WITH_ROUTE = 7;
		//new type to support map with traffic
		public static final int FOLLOW_ME_WITH_TRAFFIC = 8;
	}

	public static class Menu {
		/**
		 * The key for JSON object
		 */
		public static class Key {
			public static final String TEXT = "Text";
			public static final String ID = "ID";
			public static final String TYPE = "Type";
		}

		public static class Type {
			public static final String DYNAMIC = "0";
			public static final String STATIC = "1";
			public static final String SEPERATOR = "2";
		}

		public static class StaticMenu {
			public static final String ZOOM_IN = "ZoomIn";
			public static final String ZOOM_OUT = "ZoomOut";
			public static final String TRAFFIC = "Traffic";
			public static final String CURRNT_LOCATION = "CurrentLocation";
			public static final String ZOOM = "Zoom";
			public static final String MAP_STYLE = "MapStyle";
			public static final String RECORD_LOCATION = "RecordLocation";

		}

		public static class DynamicMenu {
			public static final String DRIVE_TO = "DriveTo";
			public static final String QUICK_SEARCH = "QuickSearch";
			public static final String SEARCH_NEAR_BY = "SearchNearBy";
			public static final String COMMUTE_ALERT = "CommuteAlert";
			public static final String SHARE_LOCATION = "ShareLocation";
			public static final String CHANGE_LOCATION = "ChangeLocation";
			public static final String FEATURE_SELECTED = "FeatureSelected";
			public static final String LIST_RESULTS = "ListResult";
			public static final String GET_ROUTE = "GetRoute";
			public static final String SAVE_FAVORITE = "SaveFavorite";
			public static final String SHOW_DSR = "ShowDSR";
			public static final String UPGRADE_VERSION = "UpgradeVersion";
			public static final String DRIVE_TO_MENU = "DriveToMenu";

			public static final String EDIT_ALERT = "EditAlert";
			public static final String DELETE_ALERT = "DeleteAlert";
			public static final String TURN_OFF_ALERT = "DisableAlert";
			public static final String TURN_ON_ALERT = "EnableAlert";
			public static final String COPY_ALERT = "CopyAlert";
			public static final String REVERSE_ALERT = "ReverseAlert";
			public static final String UPGRADE = "UPGRADE";

			/** This is not visible in menu. */
			public static final String BACK = "Back";

			// ATT Extras
			public static final String ATT_EXTRAS = "AttExtras";

			// ATT About Menu
			public static final String ATT_ABOUT = "ATTAbout";
			
			public static final String POI_SHOW_NEXT = "NextPOI";
			public static final String POI_SHOW_PREVIOUS = "PreviousPOI";
			public static final String POI_NEXT_PAGE = "NextPage";
			public static final String POI_PREVIOUS_PAGE = "PreviousPage";
		}
	}

	public static class Response {
		public static class Key {
			public static final String MAP_STATUS = "MapStatus";
			public static final String TYPE = "Type";
			public static final String ZOOM = "Zoom";
			public static final String CENTER = "MapCenter";
			public static final String LAT = "Lat";
			public static final String LON = "Lon";
			public static final String STYLE = "MapStyle";
			public static final String ID = "CallbackID";
			public static final String SELECTED_INDEX = "SelectedIndex";
			public static final String SELECTED_ADS_INDEX = "SelectedPoiAdsIndex";
		}
	}

	public static class ScreenFeature {
		public static class key {
			public static final String LAT = "Lat";
			public static final String LON = "Lon";
			public static final String TEXT = "Text";
			public static final String ICON = "Icon";
			public static final String ICON_URL = "IconURL";
			public static final String ICON_X = "IconX";
			public static final String ICON_Y = "IconY";
		}
	}
}
