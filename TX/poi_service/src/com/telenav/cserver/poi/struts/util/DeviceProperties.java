/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.struts.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;

/**
 * Device Properties: use client properties file
 * 
 * It can't be initiate by call constructor, use following code to get an instance:
 * DeviceProperties dp = DevicePropertiesHolder.getInstance().getDeviceProperties(UserInfoWrapper)
 * 
 * @author yqchen
 * @version 1.0 2007-2-8 9:26:26
 * @see DevicePropertiesHolder
 */
public class DeviceProperties
{
	Map attributes;
	
	DeviceProperties(Map attributes)
	{
		this.attributes = attributes;
		initAttributes();
	}
    
    public static String KEY_REVIEW_CHAR_LIMIT = "reviewCharLimit";
    public static String KEY_SEND_EMAIL = "sendEmail";
    public static String KEY_USE_NEW_VECTOR_TILES = "useVectorTile";
    public static String KEY_NEED_CHECK_ENTITLEMENT = "needEntitlement";
    public static String KEY_USER_FAMILY = "userFamily";
    public static String KEY_NEED_EQPIN = "needEqpin";
    public static String KEY_HAS_TNMAP_USER = "hasTnMapUser";
    public static String KEY_NEED_PPD = "needPPD";
    public static String KEY_POI_NEED_COUNTRY = "needCountryForPOI";
    public static String KEYP_ENABLE_AIRPORT_US_REGION = "enableAirportUSRegion";
    //[DH] Need client to control need geocoding or not
    public static String KEY_CLIENT_CONTROLLED_GEOCODING = "clientControlledGeoCoding";
    public static String KEY_NEED_AUDIO_FORMAT = "needAudioFormat";
    public static String KEY_NAVIGATOR_ZOOM_LEVEL = "defaultZoom";
    public static String KEY_NEED_SYNC_ADDRESS_SHARING = "needSyncAddressSharing";
    public static String KEY_USE_TRAFFIC = "useTraffic";
    public static String KEY_USE_YPC_MIS_LOGS = "useYPCMISLogs";
    
    
	/**
	 * reading from resource bundle and set attributes
	 *
	 */
	private void initAttributes()
	{
		audioIndex = getInt("AudioStoreIndex");
		imageStoreIndex = getInt("ImageStoreIndex");
		isSmallMemory = getBoolean("IsSmallMemoryDevice");
		useDecimatedPoints = getBoolean("UseDecimatedPoints");
		ignoreMissingResource  = getBoolean("IgnoreMissingResource");
		useStreaming   = getBoolean("StreamingTxNode");
		useCompression = getBoolean("UseCompression");
		
		flashScreenIndex = getInt("FlashScreenIndex");
		adsImageIndex = getInt("AdsImageIndex");
	    tourImageIndex = getInt("TourImageIndex");
	    
		inventorySet = getInt(ResourceConst.INVENTORYSET, ResourceConst.DEFAULT_PROMPTS);
		preferenceSet = getInt(ResourceConst.PREFERENCE_SET, ResourceConst.DEFAULT_PREFERENCE_SET);
		roadColorSet = getInt(ResourceConst.ROADCOLOR_SET, ResourceConst.DEFAULT_ROADCOLOR_SET);
		
		polygonSet = getInt(ResourceConst.POLYGONCOLOR_SET, ResourceConst.DEFAULT_POLYGONCOLOR_SET);
		otherColorSet = getInt(ResourceConst.OTHERSCOLOR_SET, ResourceConst.DEFAULT_OTHERSCOLOR_SET);
		
        stepDownload = getBoolean("StepDownload");
        useNewPrompt = getBoolean("UseNewPrompt");
        load52AudioWhenLogin = getBoolean("Load52AudioWhenLogin");
        useServerAudioLogic = getBoolean("UseServerAudioLogic");
        
        poiCatVersion = getString("PoiCatVersion");
		poiFinderVersion = getString("PoiFinderVersion");
		
		useBannerAD = getBoolean("UseBannerAD");

		useSponsorPOI = getBoolean("UseSponsorPOI");
		
		alertsFreq = getInt("AlertsFreqSet", ResourceConst.DEFAULT_ALERTS_FREQ);
        
        storeModel = getString("StoreModel");

        
        useNewStaticPrompt = getBoolean("UseNewStaticPrompt");
        
        //[JM] passNstl for Alltel PDE switch
		passNstl = getBoolean("PassNSTL");
		
		productTourStepDownload = getBoolean("ProductTourStepDownload");
        
        showPeriodicScreen = getBoolean("ShowPeriodicScreen");
        
        downloadAudioInRouting = getBoolean("DownloadAudioInRouting",true);
        
        downloadTileInRouting = getBoolean("DownloadTileInRouting", true);
        
        ignoreAllResourceExceptLogin = getBoolean("IgnoreAllResourceExceptLogin");
        
        needPoiCategory = getBoolean("NeedPoiCategory", true);
        
        needAirport = getBoolean("NeedAirport",true);
        
        needDriveTo = getBoolean("NeedDriveTo",true);
        
        needColor = getBoolean("NeedColor", true);
        
        needAcTemplate = getBoolean("NeedAcTemplate", true);
        
        needCrossAuth = getBoolean("NeedCrossAuth", true);
        
        needUpdateTerm = getBoolean("NeedUpdateTerm", true);
        
        needUpdateMenu = getBoolean("NeedUpdateMenu", true);
        
        needUpdatePromptDsrRule = getBoolean("NeedUpdatePromptDsrRule", true);
        
        needPromptRule = getBoolean("NeedPromptRule", true);
        
        needUpdatePoiBrandName = getBoolean("NeedUpdatePoiBrandName", true);
        
        needUpdateCarrierApn = getBoolean("NeedUpdateCarrierApn", true);
        
        needUpdateLocaleInfo = getBoolean("NeedUpdateLocaleInfo", true);
        
        needUpdateContent = getBoolean("NeedUpdateContent", true);
        
        needUpdateGateWay = getBoolean("NeedUpdateGateWay", true);
        
        needUpdatePrefences = getBoolean("NeedUpdatePrefences", true);
        
        needFeedback = getBoolean("NeedFeedback", true);
        
        needUpdateTrafficRule = getBoolean("NeedUpdateTrafficRule", true);
        
        needUpdateMiscInfo = getBoolean("NeedUpdateMiscInfo", true);
        
        multiScreenSizeDevice = getBoolean("IsMultiScreenSizeDevice", false);
        
        needNavRule = getBoolean("NeedNavRule", true);
        
        needTrafficAudio = getBoolean("NeedTrafficAudio", true);
        
        getAddtionalImages();
        
        needServerDrivenParam = getBoolean("needServerDrivenParam", false);		
        
        needPinForExistingAccount = getBoolean("needPinForExistingAccount", false);
        
        productTourNumber = getInt("ProductTourImageNumber", 1);
        
        isGlobalClient =getBoolean("IsGlobalClient",false);

        
        needUpdateMapVersion = getBoolean("NeedUpdateMapVersion", false);

        
        needClientLogNode= getBoolean("needClientLogNode", true);

	}
	
	private int getInt(String key)
	{
		return getInt(key, -1);
	}
	
	public int getInt(String key, int defValue)
	{
		try
		{
			String sRtn = (String)attributes.get(key);
			if (sRtn != null && sRtn.length() > 0)
			{
				return Integer.parseInt(sRtn.trim());
			} 
		} catch (Exception e)
		{
			// no config or incorrect config			
		}
		return defValue;
	}

	private boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}
	
	/**
     * Get device property value, if the key does not exist, we can use the def.  
     * @param key
     * @param def
     * @return
	 */
    public boolean getBoolean(String key, boolean def)
    {
        try
        {
            String sRtn = (String)attributes.get(key);
            if (sRtn != null && sRtn.length() > 0)
            {
               return Boolean.valueOf(sRtn.trim()).booleanValue();
            } 
        } catch (Exception e)
        {
            // no config or incorrect config
        }
        return def;
    }
	
    private String getString(String key)
    {
       return getString(key, "");
    }
    
    public String getString(String key, String def)
    {
        try
        {
            String sRtn = (String)attributes.get(key);
            if (sRtn != null && sRtn.length() > 0)
            {
                return sRtn.trim();
            }
        } catch (Exception e)
        {
            // no config or incorrect config
        }
        return def;
    }
    
    //AddtionalImage
    private void getAddtionalImages()
    {
        Iterator attr = attributes.keySet().iterator();
        while(attr.hasNext())
        {
            String key = (String)attr.next();
            if(key.startsWith(ResourceConst.KEY_ADDTIONAL_IMAGE))
            {
                addtionalImages.put(key, attributes.get(key)) ;      
            }
        }
    }
    
	int audioIndex = -1;

	/**
	 * get audio index from audio properties file
	 * 
	 * @return index int if error return -1
	 */
	public int getAudioIndex()
	{
		return audioIndex;
	}

	int imageStoreIndex = -1;

	/**
	 * get image index from image properties file
	 * 
	 * @return index int if error return -1
	 */
	public int getImageStoreIndex()
	{
		return imageStoreIndex;
	}

	boolean isSmallMemory = false;
	
	/**
	 * get IsSmallMemeroyDevice from properties file
	 * 
	 * @return index boolean if error return false
	 */
	public boolean isSmallMemory()
	{
		return isSmallMemory;
	}
	
	boolean useStreaming = false;
	
	/**
	 * get useStreaming from properties file
	 * 
	 * @return boolean if error return false
	 */
	public boolean useStreaming()
	{
		return useStreaming;
	}

	boolean useCompression = false;
	/**
	 * get useCompression from properties file, default is false
	 * @return boolean if error return false
	 */
	public boolean useCompression()
	{
		return useCompression;
	}
	
	boolean useDecimatedPoints = false;
	
	/**
	 * get UseDecimatedPoints from properties file
	 * 
	 * @return index boolean if error return false
	 */
	public boolean shouldUseDecimatedPoints()
	{
		return useDecimatedPoints;
	}
	
	boolean ignoreMissingResource = false;

	/**
	 * get UseDecimatedPoints from properties file
	 * 
	 * @return index boolean if error return false
	 */
	public boolean ignoreMissingResource()
	{
		return ignoreMissingResource;
	}

	int inventorySet = ResourceConst.DEFAULT_PROMPTS;
	
	public int getInventorySet()
	{
		return inventorySet;
	}

	int preferenceSet = ResourceConst.DEFAULT_PREFERENCE_SET;
	
	public int getPreferenceSet()
	{
		return preferenceSet;
	}

	int roadColorSet = ResourceConst.DEFAULT_ROADCOLOR_SET;
	public int getRoadColorSet()
	{
		return roadColorSet;
	}

	int polygonSet = ResourceConst.DEFAULT_POLYGONCOLOR_SET;
	public int getPolygonSet()
	{
		return polygonSet;
	}

	int otherColorSet = -1;
	public int getOthersColorSet()
	{
		return otherColorSet;
	}
    
    boolean stepDownload = false;
    public boolean getStepDownload()
    {
        return stepDownload;
    }
    
    /**
     * Default value is true for step download product tour 
     */
    boolean productTourStepDownload = false;
    public boolean getProductTourStepDownload()
    {
    	return productTourStepDownload;
    }

    boolean passNstl = false;
    
    public boolean passNSTL()
    {
    	return passNstl;
    }
    
	public String toString()
	{
		return "AudioStoreIndex = " + getAudioIndex() + "; isSmallMemeroy = "
				+ isSmallMemory() + "; ImageStoreIndex = "
				+ getImageStoreIndex() + "; shouldUseDecimatedPoints = "
				+ shouldUseDecimatedPoints()+ "; stepDownload = "
                + stepDownload
                + "; preferenceSet = " + preferenceSet
                ;
		
	}
	
	boolean useNewPrompt = false;
    boolean useNewStaticPrompt = false;

	/**
	 * whether uses new prompt format(with locale)
	 * 
	 * @return
	 */
	public boolean useNewPrompt()
	{
		return useNewPrompt;
	}
	
	/**
	 * This value is only used in 5.2 j2me sprint phone. 
	 */
	boolean load52AudioWhenLogin = false;
	
	public boolean load52AudioWhenLogin()
	{
		return load52AudioWhenLogin;
	}
	
	boolean useServerAudioLogic = false;
	
	/**
	 * whether uses server audio paly logic
	 * 
	 * @return
	 */
	public boolean useServerAudioLogic()
	{
		return useServerAudioLogic;
	}
	
	int flashScreenIndex = 0;
	
	int adsImageIndex = 0;
	
	int tourImageIndex = 0;
	
	public int getTourImageIndex()
	{
		return tourImageIndex;
	}
	
	public int getFlashScreenIndex()
	{
		return flashScreenIndex;
	}
	
	public int getAdsImageIndex ()
	{
		return adsImageIndex;
	}
	
    HashMap addtionalImages = new HashMap();

    //AddtionalImage
    public String getAddtionalImage(String mapDataType)
    {
        String addtionImageName = (String)addtionalImages.get(ResourceConst.KEY_ADDTIONAL_IMAGE + "_" + mapDataType);
        if(addtionImageName != null && addtionImageName.length() > 0)
            return addtionImageName;
        else
            return (String)addtionalImages.get(ResourceConst.KEY_ADDTIONAL_IMAGE);
    }
    
    private String poiCatVersion="";

	public String getPoiCatVersion() 
	{
		return poiCatVersion;
	}
	
	private String poiFinderVersion;

	public String getPoiFinderVersion() 
	{
		return poiFinderVersion;
	}
	
	private boolean useSponsorPOI;
	
	public boolean isUseSponsorPOI()
	{
		return useSponsorPOI;
	}
	
	private boolean useBannerAD;
	
	public boolean isUseBannerAD()
	{
		return useBannerAD;
	}
	

	//Unit: second
	private int alertsFreq;
	
	public int getAlertsFreq()
	{
		return alertsFreq;
	}
	
    private String storeModel = "";
    
    public String getStoreModel()
    {
        return storeModel;
    }
    
    private boolean useNewStaticPrompt()
    {
        return useNewStaticPrompt;
    }
    
    /**
     * @return Returns the useNewStaticPrompt.
     */
    public boolean isUseNewStaticPrompt(boolean isSNBranch)
    {
        return (!isSNBranch && useNewPrompt || isSNBranch && useNewStaticPrompt);
    }
    
    private boolean showPeriodicScreen;  
    
    public boolean needShowPeriodicScreen()
    {
        return showPeriodicScreen;
    }
    
    private boolean downloadAudioInRouting;
    
    public boolean needDownloadAudioInRouting()
    {
        return downloadAudioInRouting;
    }
    
    private boolean downloadTileInRouting;
    
    public boolean needDownloadTileInRouting()
    {
        return downloadTileInRouting;
    }
    
    private boolean ignoreAllResourceExceptLogin;
    
    public boolean isIgnoreAllResourceExceptLogin()
    {
        return ignoreAllResourceExceptLogin;
    }
    
    private boolean needPoiCategory;
    
    public boolean isNeedPoiCategory()
    {
        return needPoiCategory;
    }
    
    private boolean needAirport;
    
    public boolean isNeedAirport()
    {
        return needAirport;
    }
    
    private boolean needDriveTo;
    
    public boolean isNeedDriveTo()
    {
        return needDriveTo;
    }
    
    private boolean needColor;
    
    public boolean isNeedColor()
    {
        return needColor;
    }
    
    private boolean needAcTemplate;
    
    public boolean isNeedAcTemplate()
    {
        return needAcTemplate;
    }
    
    private boolean needCrossAuth;
    
    public boolean isNeedCrossAuth()
    {
        return needCrossAuth;
    }
    
    
    
    
    
    
    
    
    
    
    
    private boolean needUpdateTerm;
    
    public boolean isNeedUpdateTerm()
    {
        return needUpdateTerm;
    }
    
    private boolean needUpdateMenu;
    
    public boolean isNeedUpdateMenu()
    {
        return needUpdateMenu;
    }
    
    private boolean needUpdatePromptDsrRule;
    
    public boolean isNeedUpdatePromptDsrRule()
    {
        return needUpdatePromptDsrRule;
    }
    
    private boolean needPromptRule;
    
    public boolean isNeedPromptRule()
    {
        return needPromptRule;
    }
    
    private boolean needUpdatePoiBrandName;
    
    public boolean isNeedUpdatePoiBrandName()
    {
        return needUpdatePoiBrandName;
    }
    
    private boolean needUpdateCarrierApn;
    
    public boolean isNeedUpdateCarrierApn()
    {
        return needUpdateCarrierApn;
    }
    
    
    private boolean needUpdateLocaleInfo;
    
    public boolean isNeedUpdateLocaleInfo()
    {
        return needUpdateLocaleInfo;
    }
    
    
    private boolean needUpdateContent;
    
    public boolean isNeedUpdateContent()
    {
        return needUpdateContent;
    }
    
    
    private boolean needUpdateGateWay;
    
    public boolean isNeedUpdateGateWay()
    {
        return needUpdateGateWay;
    }
    
    private boolean needUpdatePrefences;
    
    public boolean isNeedUpdatePrefences()
    {
        return needUpdatePrefences;
    }
    
    private boolean needFeedback;
    
    public boolean isNeedFeedback()
    {
        return needFeedback;
    }
    
    private boolean needUpdateTrafficRule;
    
    public boolean isNeedUpdateTrafficRule()
    {
        return needUpdateTrafficRule;
    }
    
    private boolean needUpdateMiscInfo;
    
    public boolean isNeedUpdateMiscInfo()
    {
        return needUpdateMiscInfo;
    }
    
    private boolean multiScreenSizeDevice;
    public boolean isMultiScreenSizeDevice()
    {
    	return multiScreenSizeDevice;
    }
    
    private boolean needNavRule;
    public boolean isNeedNavRule()
    {
        return needNavRule;
    }
    
    private boolean needServerDrivenParam;
    public boolean useServerDrivenParam()
    {
    	return needServerDrivenParam;
    }
    
    private boolean needTrafficAudio;
    public boolean disableTrafficAudio()
    {
    	return !needTrafficAudio;
    }
    private boolean needPinForExistingAccount = false;
	public boolean needPinForExistingAccount()
	{
		return needPinForExistingAccount;
	}
	
	private int productTourNumber;
	public int getProductTourNumber()
	{
		return productTourNumber;
	}
	
	private boolean isGlobalClient = false;

	public boolean isGlobalClient()
	{
		return isGlobalClient;
	}

	
	private boolean needUpdateMapVersion = false;
	public boolean isNeedUpdateMapVersion()
	{
		return needUpdateMapVersion;
	}

	
	private boolean needClientLogNode = true;
	public boolean needClientLogNode()
	{
		return needClientLogNode;
	}



}
