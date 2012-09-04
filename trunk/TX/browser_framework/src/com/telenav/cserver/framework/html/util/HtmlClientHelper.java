/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.html.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlClientHelper {
    
	public static final String LAYOUT_CONFIG_FILE = "config.Layout";
	public static final String MODULE_NAME_FILE = "ModuleName";
    public static final String DEVICE_FOLDER_NAME = "device";
    //public static final String COMMON_IMAGE_FOLDER = "common";
    public static final String DEVICE_CARRIER_FILE = "device_carrier_mapping";
    public static final String RESOURCE_MAPPING_FILE = "resourcemapping";
    public static final String SPECIAL_DEVICE_FOLDER_NAME = "specialDevice";
    public static final String CSS_FOLDER = "style";
    public static final String IMAGE_FOLDER = "image";
    public static final String AUDIO_FOLDER = "audio";
    
    //keys used in resourcemapping properties file.
    public static final String RESOURCE_MAPPING_KEY_MSG = "message";
    public static final String RESOURCE_MAPPING_KEY_SHAREDIMAGE = "sharedImage";
    public static final String RESOURCE_MAPPING_KEY_SHAREDCSS = "sharedCss";
    //public static final String RESOURCE_MAPPING_KEY_MANIFEST = "manifest";
    public static final String RESOURCE_MAPPING_KEY_SPECFICRESOURCE = "specficResource";
    public static final String RESOURCE_MAPPING_KEY_AUDIO = "audio";
    
    //public static final String DEFAULT_FOLDER = "800x480_480x800";
    public static final String SMALL_FOLDER = "320x240_240x320";
    public static final String MEDIUM_FOLDER = "480x320_320x480";
    public static final String LARGE_FOLDER = "800x480_480x800";
    public static final String VAST_FOLDER = "960x540_540x960";
    public static final String TABLET_FOLDER = "1280x800_800x1280";
    public static final String COMMON_FOLDER = "common";	//common folder for all screens
    
    public static final int SCREEN_SMALL = 400;//320;
    public static final int SCREEN_MEDIUM = 480;
    public static final int SCREEN_LARGE = 854;//800
    public static final int SCREEN_VAST = 1024;//960
    public static final int SCREEN_TABLET = 1280;
    
    /**
     * replace the "." as "_" for version no.
     * convert value from 7.x.y to 7.x
     * @param version
     * @return
     */
    public static String getVersion(String version)
    {
    	if(version.startsWith("7.") || version.startsWith("8."))
    	{
	    	String[] array = version.split("\\.");
	    	//
	    	String newVersion = array[0] + "." + array[1];
	        return newVersion.replace('.', '_');
    	}
    	else
    	{
    		return version;
    	}
    }

    /**
     * always return default
     * @param product
     * @return
     */
    public static String getProduct(String product)
    {
        return "default";
    }
    
    /**
     * get the device name, this maybe the real device name or default/closet device name
     * e.g one device's screensize is 854x480 for ATT 6.2, we will using "800x480_480x800" layout if does not config
     * the device for "854x480_480x854"
     * @param handler
     * @return
     */
    public static String getDevice(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
//    	String device = clientInfo.getDevice();
//    	
//    	boolean isSupportDeviceLevelConfig = config.isDeviceLevelConfigExist();
//    	if(!isSupportDeviceLevelConfig)
//    	{
//    		device = config.getDefaultDeviceName();
//    		
//    		boolean isScreenSizeLevelConfigExist = config.isScreenSizeLevelConfigExist();
//    		if(!isScreenSizeLevelConfigExist)
//    		{
//    			device = config.getClosestDeviceName();
//    		}
//    	}
//    	return device;
    	
    	return config.getClosestDeviceName();
    }
    
    /**
     * Generate Audio Key based on it is path(location).
     * If audio  file is shared across programCode/platform/device/product,
     * use the mapping read in from resourcemapping.properties to swap the path here.
     * 
     * @param clientInfo
     * @param config
     * @return auido key
     */
    public static String getAudioKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getAudioMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getAudioMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getAudioMapping()));
        //String device = getDevice(clientInfo,config);
        //device = getDeviceMapping(device,config.getAudioMapping());
        String product = getProduct(clientInfo.getProduct());
        
        String key = "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + COMMON_FOLDER + "/" + AUDIO_FOLDER + "/";
        return key;
    }
    
    /**
     * Generate messageI18Nkey based on it is path(location).
     * If message property file is shared across programCode/platform/device/product,
     * use the mapping read in from resourcemapping.properties to swap the path here.
     * 
     * @param clientInfo
     * @param config
     * @return message key
     */
    public static String getMessageI18NKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getMsgMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getMsgMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getMsgMapping()));
        String device = getDevice(clientInfo,config);
        device = getDeviceMapping(device,config.getMsgMapping());
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getMsgMapping());
        String locale = clientInfo.getLocale();
        
        String key = programCode + "." + platform + "." + version + "." + product + "." + device + "." + locale;
        return key;
    }

    /**
     * 
     * @param handler
     * @return
     */
//    public static String getImageKeyWithoutSize(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
//    {
//    	String programCode = clientInfo.getProgramCode();
//        String platform = clientInfo.getPlatform();
//        String version = getVersion(clientInfo.getVersion());
//        String device = "";
//        	device = getDevice(clientInfo,config);
//        String product = getProduct(clientInfo.getProduct());
//        
//        String key = "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + device + "/";
//        
//        return key;
//    }
   
    /**
     * Get image key for shared images.
     * @param handler
     * @return
     */
    public static String getSharedImageKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getSharedImageMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getSharedImageMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getSharedImageMapping()));
        String device = getDevice(clientInfo,config);
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getSharedImageMapping());
        String key = "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + device + "/" + IMAGE_FOLDER + "/";
        
        return key;
    }
    
    /**
     * Get shared CSS key for shared css files.
     * @param clientInfo
     * @param config
     * @return
     */
    public static String getSharedCssKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getSharedCssMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getSharedCssMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getSharedCssMapping()));
        String device = getDevice(clientInfo,config);
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getSharedCssMapping());
        String key = "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + device + "/" + CSS_FOLDER + "/";
        return key;
    }
    
    
    public static String getSharedCssDeviceCommonKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getSharedCssMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getSharedCssMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getSharedCssMapping()));
        String device = COMMON_FOLDER;
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getSharedCssMapping());
        String key = "/" + programCode + "/" + platform + "/" + version + "/" + product + "/" + device + "/" + CSS_FOLDER + "/";
        return key;
    }
    
    /**
     * 
     * @param handler
     * @return
     * @throws JspException
     */
    public static String getLayoutKeyWithDevice(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = clientInfo.getProgramCode();
        String platform = clientInfo.getPlatform();
        String version = getVersion(clientInfo.getVersion());
        String device = getDevice(clientInfo,config);
        String product = getProduct(clientInfo.getProduct());
        
        String key = programCode + "." + platform + "." + version  + "." + product  + "." + device;
        
        //System.out.println("---LayoutKeyWithDevice:" + key);
        
        return key;
    }
    
    public static String getLayoutKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = clientInfo.getProgramCode();
        String platform = clientInfo.getPlatform();
        String version = getVersion(clientInfo.getVersion());
        String device = COMMON_FOLDER;
        String product = getProduct(clientInfo.getProduct());
        
        String key = programCode + "." + platform + "." + version  + "." + product  + "." + device;
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
     * Get image host config from layout config file.
     * @return image host string
     */
//    public static String getImageHost()
//    {
//    	String host = "";
//        try
//        {
//            ResourceBundle serverBundle = ResourceBundle.getBundle(LAYOUT_CONFIG_FILE);
//            
//            host = getString(serverBundle.getString("URL_HOST_HTML"));
//        }
//        catch(Exception e)
//        {
//        	
//        }
//    	
//    	return host;
//    }
 

    /**
     * 
     * @param handler
     * @return
     */
//    public static String getCommonImageFloder()
//    {
//    	return COMMON_IMAGE_FOLDER;
//    }
    
    /**
     * it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * it will use the screensize to represent the device name
     * get defalut devie name
     * @param handler
     * @return
     */
//    public static String getDefaultDeviceName(HtmlClientInfo clientInfo)
//    {
//    	String defaultDeviceName = "";
//    	String width = clientInfo.getSupportedScreenWidth();
//        String height = clientInfo.getSupportedScreenHeight();
//        if("".equals(width))
//        {
//        	width = clientInfo.getWidth();
//        }
//        
//        if("".equals(height))
//        {
//        	height = clientInfo.getHeight();
//        }
//        
//        String[] widths = width.split("-");
//        String[] heights = height.split("-");
//        
//        if(widths.length >1)
//        {
//        	defaultDeviceName = widths[0] + "x" + heights[0] + "_" + widths[1] + "x" + heights[1]; 
//        }
//        else
//        {
//        	defaultDeviceName = widths[0] + "x" + heights[0];
//        }
//        
//        //System.out.println("---defaultDeviceName:" + defaultDeviceName);
//        
//        return defaultDeviceName;
//    }
    
    
    /**
     * Get closest device name to the current device based screensize.
     * @param handler
     * @return closest device name
     */
//    public static String getClosetDeviceName(HtmlClientInfo clientInfo,String screenSizeDeviceName)
//    {
//    	String closetDeviceName = screenSizeDeviceName;
//    	//first get all the screen size level device name
//    	String programCode = clientInfo.getProgramCode();
//        String platform = clientInfo.getPlatform();
//        String version = getVersion(clientInfo.getVersion());
//        String product = getProduct(clientInfo.getProduct());
//
//    	String parentFilePath = DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/";
//    	
//    	//get all the devices under programCode.platform.version.product folder
//    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
//    	URL url = cl.getResource(parentFilePath);
//		File folderName = new File(url.getFile());	
//		String[] childFolderName = folderName.list();
//		List<HtmlDeviceScreenSize> screenSizeList = new ArrayList<HtmlDeviceScreenSize>();
//    	for(int i=0;i<childFolderName.length;i++)
//    	{
//    		//filter the device with the screensize level config
//    		if(isDeviceScreenSize(childFolderName[i]))
//    		{
//    			HtmlDeviceScreenSize device = new HtmlDeviceScreenSize();
//    			device.setDeviceName(childFolderName[i]);
//    			device.setDeviceScreenSize();
//    			screenSizeList.add(device);
//    		}
//    	}
//    	//set current device
//    	HtmlDeviceScreenSize currentDevice = new HtmlDeviceScreenSize();
//    	currentDevice.setDeviceName(screenSizeDeviceName);
//    	currentDevice.setDeviceScreenSize();
//    	//filter the device with the condition that currentDevice.height > exist height && currentDevice.width > exist width
//    	List<HtmlDeviceScreenSize> screenSizeListFilter1 = new ArrayList<HtmlDeviceScreenSize>();
//    	for(HtmlDeviceScreenSize device:screenSizeList)
//    	{
//    		if(currentDevice.compareTo(device) >0)
//    		{
//    			screenSizeListFilter1.add(device);
//    		}
//    	}
//    	//during the screenSizeListFilter1, get the max value of widthxheight
//    	int maxScreenSize = 0;
//    	String maxScreenSizeDevieName = "";
//    	for(int i=0;i<screenSizeListFilter1.size();i++)
//    	{
//    		HtmlDeviceScreenSize device = screenSizeListFilter1.get(i);
//    		int area = device.getWidth()*device.getHeight();
//    		if(area > maxScreenSize)
//    		{
//    			maxScreenSize = area;
//    			maxScreenSizeDevieName = device.getDeviceName();
//    		}
//    	}
//    	
//    	closetDeviceName = maxScreenSizeDevieName;
//    	//System.out.println("---closetDeviceName:" + closetDeviceName);
//    	
//    	return closetDeviceName;
//    }
//    
//    /**
//     * 800x480_480x800
//     * if device name contain "_" and "x", then think this is screensize level device name
//     * @return
//     */
//    private static boolean isDeviceScreenSize(String deviceName)
//    {
//    	boolean isScreenSize = false;
//    	
//    	if(deviceName.indexOf("_") != -1 && deviceName.indexOf("x") != -1)
//    	{
//    		isScreenSize = true;
//    		String[] width = deviceName.split("x");
//    		try
//    		{
//    			Integer.parseInt(width[0]);
//    		}
//    		catch(NumberFormatException e)
//    		{
//    			isScreenSize = false;
//    		}
//    	}
//    	return isScreenSize;
//    }
    
    /**
     * get the cached key for layout
     * @param handler
     * @return
     */
    public static String getCacheKey(HtmlClientInfo clientInfo)
	{
        String version = getVersion(clientInfo.getVersion());
		
        StringBuilder sb = new StringBuilder();
        sb.append(clientInfo.getProgramCode() );
        sb.append("_" );
        sb.append(clientInfo.getPlatform());
        sb.append("_" );
        sb.append(version);
        sb.append("_" );
        sb.append(clientInfo.getDevice());
        sb.append("_" );
        sb.append(clientInfo.getLocale());
        sb.append("_" );
        sb.append(clientInfo.getWidth());
        sb.append("_" );
        sb.append(clientInfo.getHeight());

        
		return sb.toString();
	}
    
    
    public static String getCssKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
        String key = getCommonFolderPath(clientInfo,config) + CSS_FOLDER + "/";
        return key;
    }
    
    public static String getCssDeviceCommonKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
        String key = getDeviceCommonFolderPath(clientInfo,config) + CSS_FOLDER + "/";
        return key;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getImageKey(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String key = getCommonFolderPath(clientInfo,config) + IMAGE_FOLDER + "/";
        return key;
    }
    
    public static String getDeviceCommonFolderPath(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getSpecficMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getSpecficMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getSpecficMapping()));
        String device = COMMON_FOLDER;
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getSpecficMapping());
        
        String commonFolder = "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + device + "/";
        return commonFolder;
    }
    
    public static String getCommonFolderPath(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String programCode = getProgrameCodeMapping(clientInfo.getProgramCode(),config.getSpecficMapping());
        String platform = getPlatformMapping(clientInfo.getPlatform(),config.getSpecficMapping());
        String version = getVersion(getVersionMapping(clientInfo.getVersion(),config.getSpecficMapping()));
        String device = getDevice(clientInfo,config);
        String product = getProduct(clientInfo.getProduct());
        //product = getProductMapping(product, config.getSpecficMapping());
        
        String commonFolder = "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + device + "/";
        return commonFolder;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getScreenSizeLevel(HtmlClientInfo clientInfo)
    {
    	String screenSizeLevel = "";
    	//first get all the screen size level device name
    	//String programCode = clientInfo.getProgramCode();
        //String platform = clientInfo.getPlatform();
        //String version = getVersion(clientInfo.getVersion());
        //String product = getProduct(clientInfo.getProduct());

    	//String parentFilePath = DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/";
    	
    	int nHeight = Integer.parseInt(clientInfo.getHeight());
    	int nWidth = Integer.parseInt(clientInfo.getWidth());
    	
    	int maxScreen = Math.max(nWidth, nHeight);
    	
    	if(maxScreen <=SCREEN_SMALL)
    	{
    		screenSizeLevel = SMALL_FOLDER;
    	}
    	else if(maxScreen <=SCREEN_MEDIUM)
    	{
    		screenSizeLevel = MEDIUM_FOLDER;
    	}
    	else if(maxScreen <=SCREEN_LARGE)
    	{
    		screenSizeLevel = LARGE_FOLDER;    	
    	}
    	else if(maxScreen <=SCREEN_VAST)
    	{
    		screenSizeLevel = VAST_FOLDER;    	
    	}
    	else
    	{
    		screenSizeLevel = TABLET_FOLDER;    	
    	}
    	
//    	String fielPath = HtmlClientHelper.DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + screenSizeLevel;
//    	
//    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
//    	URL url = cl.getResource(fielPath);
//		if(url == null)
//		{
//			screenSizeLevel = DEFAULT_FOLDER;;
//		}
    	//System.out.println("---closetDeviceName:" + closetDeviceName);
    	
    	return screenSizeLevel;
    }
    
    public static String getDeviceCarrierMapping(HtmlClientInfo clientInfo)
    {
    	String carrier = "";
    	//String programCode = clientInfo.getProgramCode();
    	String deviceCarrier = clientInfo.getDeviceCarrier();
        try
        {
            ResourceBundle serverBundle = ResourceBundle.getBundle(DEVICE_CARRIER_FILE);
            Enumeration<String> enumeration = serverBundle.getKeys();
            String key = "";
            while(enumeration.hasMoreElements())
            {
            	key = HtmlCommonUtil.getString(enumeration.nextElement());
            	if(key.equals(deviceCarrier))
            	{
            		carrier = HtmlCommonUtil.getString(serverBundle.getString(key));
            	}
            }
        }
        catch(Exception e)
        {
        }
        
        if("".equalsIgnoreCase(carrier))
        {
        	if("".equals(deviceCarrier))
        	{
        		carrier = clientInfo.getProgramCode();
        	}
        	else
        	{
        		carrier = deviceCarrier;
        	}
        }
        
    	return carrier;
    }
    
    public static String getDeviceLevelConfig(HtmlClientInfo clientInfo, String key)
    {
    	HtmlDeviceConfig config = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo);
    	return (String)config.getConfigMap().get(key);
    }
    
    /**
     * Read in resource mapping properties file, hold the resource mapping relations in hashmaps in DeviceConfig object
     * 
     * @param clientInfo
     * @param config
     */
    public static void setResourceMapping(HtmlClientInfo clientInfo,HtmlDeviceConfig config)
    {
    	String fielPath = DEVICE_FOLDER_NAME + "." + config.getLayoutKey() + "." + RESOURCE_MAPPING_FILE;
    	try
        {
            ResourceBundle serverBundle = ResourceBundle.getBundle(fielPath);
            Enumeration<String> enumeration = serverBundle.getKeys();
            Map<String,String> map = new HashMap<String,String>();
            String key;
            while(enumeration.hasMoreElements())
            {
            	key = enumeration.nextElement();
                map.put(key, serverBundle.getString(key));
            }
            config.setConfigMap(map);
            //
            String msgMapping = HtmlCommonUtil.getString(map.get(RESOURCE_MAPPING_KEY_MSG));
            config.setMsgMapping(getClientInfoMapping(msgMapping));
            //
            String sharedImageMapping = HtmlCommonUtil.getString(map.get(RESOURCE_MAPPING_KEY_SHAREDIMAGE));
            config.setSharedImageMapping(getClientInfoMapping(sharedImageMapping));
            //
            String sharedCssMapping = HtmlCommonUtil.getString(map.get(RESOURCE_MAPPING_KEY_SHAREDCSS));
            config.setSharedCssMapping(getClientInfoMapping(sharedCssMapping));
            //
            String specficMapping = HtmlCommonUtil.getString(map.get(RESOURCE_MAPPING_KEY_SPECFICRESOURCE));
            config.setSpecficMapping(getClientInfoMapping(specficMapping));
            //
            String audioMapping = HtmlCommonUtil.getString(map.get(RESOURCE_MAPPING_KEY_AUDIO));
            config.setAudioMapping(getClientInfoMapping(audioMapping));
        }
        catch(Exception e)
        {
        	//e.printStackTrace();
        }
    }
    
    
    //sharedWeatherImage=programCode:ATTNAVPROG;productType:ATT
    private static Map getClientInfoMapping(String mapping)
    {
    	Map map = new HashMap();
    	if(!"".equals(mapping))
    	{
    		String[] str1 = mapping.split(";");
    		if(str1 != null)
    		{
	    		for(int i=0;i<str1.length;i++)
	    		{
	    			String[] temp1 = str1[i].split(":");
	    			if(temp1 != null && temp1.length >1)
	    			{
	    				map.put(temp1[0], temp1[1]);
	    			}
	    		}
    		}
    	}
    	
    	return map;
    }
    
    private static String getProgrameCodeMapping(String programCode,Map map)
    {
    	return HtmlCommonUtil.getStringFromMapKey(map, HtmlFrameworkConstants.CLIENT_INFO_KEY_PROGRAMECODE, programCode);
    }
    
//    private static String getProductMapping(String product,Map map)
//    {
//    	return HtmlCommonUtil.getStringFromMapKey(map, HtmlFrameworkConstants.CLIENT_INFO_KEY_PRODUCTTYPE, product);
//    }
    
    private static String getDeviceMapping(String device,Map map)
    {
    	return HtmlCommonUtil.getStringFromMapKey(map, HtmlFrameworkConstants.CLIENT_INFO_KEY_DEVICE, device);
    }
    
    private static String getPlatformMapping(String platform, Map map)
    {
    	return HtmlCommonUtil.getStringFromMapKey(map, HtmlFrameworkConstants.CLIENT_INFO_KEY_PLATFORM, platform);
    }
    
    private static String getVersionMapping(String version,Map map)
    {
    	return HtmlCommonUtil.getStringFromMapKey(map, HtmlFrameworkConstants.CLIENT_INFO_KEY_VERSION, version);
    }
}
