/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.browser.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.datatype.DeviceScreenSize;
import com.telenav.cserver.webapp.taglib.TnLayoutTag;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-7-20
 */
public class ClientHelper {
    
	public static final String LAYOUT_CONFIG_FILE = "config.Layout";
	public static final String MODULE_NAME_FILE = "ModuleName";
    public static final String DEVICE_CONFIG_FILE = "Device";
    public static final String COMMON_IMAGE_FOLDER = "common";
    public static final String DEFAULT_FOLDER = "default";
	
    /**
     * repleace the "." as "_" for version no.
     * e,g "6.0.01" will be changed to "6_0_01"
     * @param version
     * @return
     */
    public static String getVersion(String version)
    {
        return version.replace('.', '_');
    }

    /**
     * only get the first part of the product.
     * e.g "ATT_NAV"/"ATT_MAPS" will return as "ATT"
     * @param product
     * @return
     */
    public static String getProduct(String product)
    {
        String newProdcut = product;
        if(product == null)
        {
            product = "";
        }
        
        int index = product.indexOf("_");
        if(index != -1)
        {
            newProdcut = product.substring(0, index);
        }
        return newProdcut;
    }
    
    /**
     * get the device name, this maybe the real device name or default/closet device name
     * e.g one device's screensize is 854x480 for ATT 6.2, we will using "800x480_480x800" layout if does not config
     * the device for "854x480_480x854"
     * @param handler
     * @return
     */
    public static String getDevice(DataHandler handler,DeviceConfig config)
    {
    	String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
    	
    	boolean isSupportDeviceLevelConfig = config.isDeviceLevelConfigExist();
    	if(!isSupportDeviceLevelConfig)
    	{
    		device = config.getDefaultDeviceName();
    		
    		boolean isScreenSizeLevelConfigExist = config.isScreenSizeLevelConfigExist();
    		if(!isScreenSizeLevelConfigExist)
    		{
    			device = config.getClosestDeviceName();
    		}
    	}
    	//System.out.println("--device:" + device);
    	return device;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getMessageI18NKey(DataHandler handler,DeviceConfig config)
    {
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String device = getDevice(handler,config);
        String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);
        
        String key = carrier + "." + platform + "." + version + "." + product + "." + device + "." + locale;
        
        //System.out.println("---MessageI18NKey:" + key);
        
        return key;
    }
    
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getImageKey(DataHandler handler,DeviceConfig config)
    {
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String device = getDevice(handler,config);
        String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        String width = handler.getClientInfo(DataHandler.KEY_WIDTH);
        String height = handler.getClientInfo(DataHandler.KEY_HEIGHT);
        if(config.isUsingClosingResulotion())
    	{
        	DeviceScreenSize deviceScreenSize = new DeviceScreenSize();
        	deviceScreenSize.setDeviceName(config.getClosestDeviceName());
        	deviceScreenSize.setDeviceScreenSize();
        	
        	width = String.valueOf(deviceScreenSize.getWidth());
        	height = String.valueOf(deviceScreenSize.getHeight());
    	}
        
        String key = "/" + carrier + "/" + platform + "/" + version + "/" + product + "/" + device + "/" + width + "x" + height;
        
        //System.out.println("---imageKey:" + key);
        return key;
    }

    /**
     * 
     * @param handler
     * @return
     */
    public static String getImageKeyWithoutSize(DataHandler handler,DeviceConfig config)
    {
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        String device = getDevice(handler,config);
        
        String key = "/" + carrier + "/" + platform + "/" + version + "/" + product + "/" + device + "/";
        
        //System.out.println("---ImageKeyWithoutSize:" + key);
        return key;
    }
    
    /**
     * 
     * @param handler
     * @return
     * @throws JspException
     */
    public static String getLayoutKeyWithDevice(DataHandler handler,DeviceConfig config)
    {
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String device = getDevice(handler,config);
        String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        
        String key = carrier + "." + platform + "." + version  + "." + product  + "." + device;
        
        //System.out.println("---LayoutKeyWithDevice:" + key);
        
        return key;
    }
    

    /**
     * get Layout key with product type level
     * 
     * @param handler
     * @param config
     * @return
     */
	public static String getLayoutKeyWithProduct(DataHandler handler, DeviceConfig config)
	{
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
		String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
		String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));

		String key = carrier + "." + platform + "." + version + "." + product;

		return key;
	}

    /**
     * http://172.16.10.79:8080/poi_service/WEB-INF/jsp/ToolsMain.jsp
     * @param requestURL
     * @return
     */
    public static String getHostURL(String requestURL,String servletPath)
    {
        String hostURL = "";
        int pos = 0;
        if (requestURL != null) {
            pos = requestURL.length();
            if (servletPath != null) {
                pos = requestURL.indexOf(servletPath);
            }
            if (pos != -1) {
                hostURL = requestURL.substring(0, pos);
            }
        }
        return hostURL;
    }
    
    /**
     * 
     * @param str
     * @return
     */
    private static String getString(String str)
    {
    	if(str == null)
    	{
    		return "";
    	}
    	else
    	{
    		return str.trim();
    	}
    }
    
    /**
     * 
     * @return
     */
    public static String getImageHost()
    {
    	String host = "";
        try
        {
            ResourceBundle serverBundle = ResourceBundle.getBundle(LAYOUT_CONFIG_FILE);
            
            host = getString(serverBundle.getString("URL_HOST"));
        }
        catch(Exception e)
        {
        	
        }
    	
    	return host;
    }
 
    /**
     * Get the module name for login c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForLogin(DataHandler handler)
    {
    	return getModuleName(handler,"login.http");
    }
  
    /**
     * Get the module name for poi c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForPoi(DataHandler handler)
    {
    	return getModuleName(handler,"poi.http");
    }
  
    /**
     * Get the module name for movie c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForMovie(DataHandler handler)
    {
    	return getModuleName(handler,"movie.http");
    }
  
    
    /**
     * Get the module name for addon c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForAddon(DataHandler handler)
    {
    	return getModuleName(handler,"addon.http");
    }
    /**
     * Get the module name for post location c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForPostLocation(DataHandler handler)
    {
    	return getModuleName(handler,"postlocation.http");
    }
    
    /**
     * Get the module name for commute alert c-server
     * @param handler
     * @return
     */
    public static String getModuleNameForCommute(DataHandler handler)
    {
    	return getModuleName(handler,"commuteAlert.http");
    }
    
    /**
     * Get the module name of one device
     * @param handler
     * @param serverName
     * @return
     */
    private static String getModuleName(DataHandler handler,String serverName)
    {
    	Map<String,String> tempCacheForModule = new HashMap<String,String>();
    	String commFilePath = TnLayoutTag.DEVICE_FOLDER + "." + DeviceManager.getInstance().getDeviceConfig(handler).getLayoutKeyWithProduct() + "." + DEFAULT_FOLDER + "." + MODULE_NAME_FILE;
    	String fielPath = TnLayoutTag.DEVICE_FOLDER + "." + DeviceManager.getInstance().getDeviceConfig(handler).getLayoutKeyWithDevice() + "." + MODULE_NAME_FILE;
    	//System.out.println("--getModuleName:" + fielPath);
    	String moduelName = "";
        try
        { 
        	String loc = handler.getClientInfo(DataHandler.KEY_LOCALE);
        	String[] locale;
    		if(loc.indexOf("_") != -1 && !loc.endsWith("_")){
    			locale = loc.split("_");
    		}else{
    			locale = new String[]{"en","US"};
    		}
        	ResourceBundle serverBundleFromCommon = ReloadablePropertyResourceBundle.getResourceBundle(commFilePath,locale);
        	if (null != serverBundleFromCommon){
        		tempCacheForModule.putAll(getCacheForModule(serverBundleFromCommon));
        	}
            ResourceBundle serverBundleFromDevice = ReloadablePropertyResourceBundle.getResourceBundle(fielPath,locale);
            if (null != serverBundleFromDevice){
            	tempCacheForModule.putAll(getCacheForModule(serverBundleFromDevice)); 
            }
            moduelName = tempCacheForModule.get(serverName);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    	return moduelName;
    }
    
    private static Map<String,String> getCacheForModule(ResourceBundle serverBundle){
    	Map<String,String> cacheForModule = new HashMap<String,String>();
    	Enumeration<String> enumeration = serverBundle.getKeys();
        String itemKey = "";
        String itemValue = "";
    	while(enumeration.hasMoreElements())
        {
    		itemKey = enumeration.nextElement();
         	itemValue = serverBundle.getString(itemKey).trim();
         	cacheForModule.put(itemKey, itemValue);
        }
    	
    	return cacheForModule;
    }

    /**
     * 
     * @param handler
     * @return
     */
    public static String getCommonImageFloder(DataHandler handler)
    {
    	return COMMON_IMAGE_FOLDER;
    }
    
    /**
     * it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * it will use the screensize to represent the device name
     * get defalut devie name
     * @param handler
     * @return
     */
    public static String getDefaultDeviceName(DataHandler handler)
    {
    	String defaultDeviceName = "";
    	String width = handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH);
        String height = handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT);
        if("".equals(width))
        {
        	width = handler.getClientInfo(DataHandler.KEY_WIDTH);
        }
        
        if("".equals(height))
        {
        	height = handler.getClientInfo(DataHandler.KEY_HEIGHT);
        }
        
        String[] widths = width.split("-");
        String[] heights = height.split("-");
        
        if(widths.length >1 && heights.length > 1)
        {
        	defaultDeviceName = widths[0] + "x" + heights[0] + "_" + widths[1] + "x" + heights[1]; 
        }
        else
        {
        	defaultDeviceName = widths[0] + "x" + heights[0];
        }
        
        //System.out.println("---defaultDeviceName:" + defaultDeviceName);
        
        return defaultDeviceName;
    }
    
    
    /**
     * 
     * @param handler
     * @return
     */
    
    //TODO: simply this: use one loop should be sufficient- jz
    public static String getClosetDeviceName(DataHandler handler,String screenSizeDeviceName)
    {
    	String closetDeviceName = screenSizeDeviceName;
    	//first get all the screen size level device name
    	String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = ClientHelper.getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String product = ClientHelper.getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));

    	String parentFilePath = TnLayoutTag.DEVICE_FOLDER + "/" + carrier + "/" + platform + "/" + version  + "/" + product  + "/";
    	
    	//get all the devices under carrier.platform.version.product folder
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
    	URL url = cl.getResource(parentFilePath);
		File folderName = new File(url.getFile());	
		String[] childFolderName = folderName.list();
		List<DeviceScreenSize> screenSizeList = new ArrayList<DeviceScreenSize>();
    	for(int i=0;i<childFolderName.length;i++)
    	{
    		//filter the device with the screensize level config
    		if(isDeviceScreenSize(childFolderName[i]))
    		{
    			DeviceScreenSize device = new DeviceScreenSize();
    			device.setDeviceName(childFolderName[i]);
    			device.setDeviceScreenSize();
    			screenSizeList.add(device);
    		}
    	}
    	//set current device
    	DeviceScreenSize currentDevice = new DeviceScreenSize();
    	currentDevice.setDeviceName(screenSizeDeviceName);
    	currentDevice.setDeviceScreenSize();
    	//filter the device with the condition that currentDevice.height > exist height && currentDevice.width > exist width
    	List<DeviceScreenSize> screenSizeListFilter1 = new ArrayList<DeviceScreenSize>();
    	for(DeviceScreenSize device:screenSizeList)
    	{
    		if(currentDevice.compareTo(device) >0)
    		{
    			screenSizeListFilter1.add(device);
    		}
    	}
    	//during the screenSizeListFilter1, get the max value of widthxheight
    	int maxScreenSize = 0;
    	String maxScreenSizeDevieName = "";
    	for(int i=0;i<screenSizeListFilter1.size();i++)
    	{
    		DeviceScreenSize device = screenSizeListFilter1.get(i);
    		int area = device.getWidth()*device.getHeight();
    		if(area > maxScreenSize)
    		{
    			maxScreenSize = area;
    			maxScreenSizeDevieName = device.getDeviceName();
    		}
    	}
    	
    	closetDeviceName = maxScreenSizeDevieName;
    	//System.out.println("---closetDeviceName:" + closetDeviceName);
    	
    	return closetDeviceName;
    }
    
    /**
     * 800x480_480x800
     * if device name contain "_" and "x", then think this is screensize level device name
     * @return
     */
    private static boolean isDeviceScreenSize(String deviceName)
    {
    	boolean isScreenSize = false;
    	
    	if(deviceName.indexOf("_") != -1 && deviceName.indexOf("x") != -1)
    	{
    		isScreenSize = true;
    		String[] width = deviceName.split("x");
    		try
    		{
    			Integer.parseInt(width[0]);
    		}
    		catch(NumberFormatException e)
    		{
    			isScreenSize = false;
    		}
    	}
    	return isScreenSize;
    }
    
    /**
     * get the cached key for layout
     * @param handler
     * @return
     */
    public static String getCacheKey(DataHandler handler)
	{
		String cacheKey = "";
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String product = getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        String realDevice = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);

        // use screen side as a key for layout, for dev env, the KEY_DEVICEMODEL always the same, 
        // it will cause MissingResourceException while find layout file
        String screenSizeDeviceName = ClientHelper.getDefaultDeviceName(handler);

        cacheKey = carrier + "_" + platform + "_" + version + "_" + product +"_"+ realDevice + "_" + screenSizeDeviceName +  "_" + locale;
        
		return cacheKey;
	}
    
    public static String getImageUrl(DataHandler handler) {
		return getImageHost()
				+ DeviceManager.getInstance().getDeviceConfig(handler)
						.getImageKey() + "/image/";
	}

	public static String getCommonImageUrl(DataHandler handler) {
		return getImageHost()
				+ DeviceManager.getInstance().getDeviceConfig(handler)
						.getImageKeyWithoutSize()
				+ getCommonImageFloder(handler) + "/image/";
	}
}
