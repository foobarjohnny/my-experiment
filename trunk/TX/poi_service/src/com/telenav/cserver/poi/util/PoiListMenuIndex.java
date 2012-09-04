package com.telenav.cserver.poi.util;

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Refresh menu index in POI list, it depends on the feature controller in feature list
 * Now it only support FeatureConstant.FEEDBACK_POI & FeatureConstant.SHARE_ADDRESS
 * 
 * If you add a new feature controller in PoiList.jsp, please do with steps:
 * 1. Define a new feature controller, e.g.: isFeedbackEnabled
 * 2. Define a index array about the menu controlled by your controller, e.g.: feedBackIndex
 * 3. Update menu index with your controller
 * 
 * @author Hunter Liu
 * 
 */
public class PoiListMenuIndex
{
	public static Logger logger = Logger.getLogger(PoiListMenuIndex.class);
	
	public static int	SELECT;				    // 0
	public static int	VIEW_DETAILS;			// 1
	public static int	MAP_RESULTS;			// 2
	public static int	FIRST_MENU_SEPARATOR;	// 3

	public static int	DRIVE_TO;				// 4
	public static int	CALL;					// 5
	public static int	MAP;					// 6
	public static int	SHARE;					// 7
	public static int	SAVE;					// 8
	public static int	FEEDBACK;				// 9

	public static int	SECOND_MENU_SEPARATOR;	// 10

	public static int	POPULAR;				// 11
	public static int	PRICE;				    // 12
	public static int	RELEVANCE;				// 13
	public static int	RATING;				    // 14
	public static int	DISTANCE;				// 15

	public static int	DRIVE_TO_CONTEXT;		// 16
	public static int	CALL_CONTEXT;			// 17
	public static int	MAP_CONTEXT;			// 18
	public static int	SHARE_CONTEXT;			// 19
	public static int	SAVE_CONTEXT;			// 20
	public static int	RATE_CONTEXT;			// 21
	
	private boolean isFeedbackEnabled = true;
	private boolean isShareAddressEnabled = true;
	
	/**Initialize the menu index, please don't change this unless you add or delete a menu in PoiList.jsp*/
	private Integer[] menuIndex={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
	/**Initialize menu index controlled by feature: feedback*/
	private int[] feedBackIndex={9}; //9:feedBackMenu
	/**Initialize menu index controlled by feature: shareAddress*/
	private int[] shareAddressIndex={7,19}; //7:shareMenu, 19:shareContentMenu
	/**Mark the disabled menu*/
	private int disableFlag=100;
	
	public void refreshMenuIndex()
	{
		//Update menu index with controller
		updateIndex(feedBackIndex, isFeedbackEnabled);
		updateIndex(shareAddressIndex, isShareAddressEnabled);
		
		//Refresh the menu index
		int disabledMenu=0;
		for(int i=0; i<menuIndex.length; i++)
		{
			if(menuIndex[i]==disableFlag)
			{
				disabledMenu++;
				continue;
			}
			menuIndex[i]-=disabledMenu;
		}
		
		initIndex();
	}
	
	private void updateIndex(int[] featureIndex, boolean featureEnabled)
	{
		if(!featureEnabled)
		{
			for(int i: featureIndex)
			{
				menuIndex[i]=disableFlag;
			}
		}
	}
	
	/**
	 * Initialize menu with the real index
	 */
	private void initIndex()
	{
		SELECT = menuIndex[0];
		VIEW_DETAILS = menuIndex[1];
		MAP_RESULTS = menuIndex[2];
		FIRST_MENU_SEPARATOR = menuIndex[3];

		DRIVE_TO = menuIndex[4];
		CALL = menuIndex[5];
		MAP = menuIndex[6];
		SHARE = menuIndex[7];
		SAVE = menuIndex[8];
		FEEDBACK = menuIndex[9];

		SECOND_MENU_SEPARATOR = menuIndex[10];

		POPULAR = menuIndex[11];
		PRICE = menuIndex[12];
		RELEVANCE = menuIndex[13];
		RATING = menuIndex[14];
		DISTANCE = menuIndex[15];

		DRIVE_TO_CONTEXT = menuIndex[16];
		CALL_CONTEXT = menuIndex[17];
		MAP_CONTEXT = menuIndex[18];
		SHARE_CONTEXT = menuIndex[19];
		SAVE_CONTEXT = menuIndex[20];
		RATE_CONTEXT = menuIndex[21];
		
		if(logger.isDebugEnabled())
		{
			logger.debug("POI list Menu index : "+ Arrays.asList(menuIndex));
		}
	}
	
	public void setFeedBackEnable(boolean isFeedbackEnabled)
	{
		this.isFeedbackEnabled = isFeedbackEnabled;
	}

	public void setShareAddressEnable(boolean isShareAddressEnabled)
	{
		this.isShareAddressEnabled = isShareAddressEnabled;
	}
}
