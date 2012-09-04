package com.telenav.cserver.framework.html.util;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;

/**
 * The manager class maintains a map of cached key and client device configuration.
 * cached key is composed of 
 * programCode + "_" + platform + "_" + version + "_" + realDevice + "_" + locale
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlDeviceManager {
	
	private Logger logger = Logger.getLogger(HtmlDeviceManager.class);
	private static HtmlDeviceManager instance = new HtmlDeviceManager();
	private ConcurrentMap<String,HtmlDeviceConfig> deviceConfigMap = new ConcurrentHashMap<String,HtmlDeviceConfig>();
	
	/**
	 * 
	 * @return
	 */
	public static HtmlDeviceManager getInstance()
	{
		return instance;
	}
	

	
	public HtmlDeviceConfig getDeviceConfig(HtmlClientInfo clientInfo)
	{
		String realDevice = clientInfo.getDevice();
		String cachedKey = HtmlClientHelper.getCacheKey(clientInfo);
		HtmlDeviceConfig config = getDeviceConfigMap().get(cachedKey);
		if(config == null)
		{
			//set default device name
			config = new HtmlDeviceConfig();
			config.setRealDeviceName(realDevice);
			config.setCachedKey(cachedKey);
			
			//String screenSizeDeviceName = HtmlClientHelper.getDefaultDeviceName(clientInfo);
			
			String closestDeviceName = HtmlClientHelper.getScreenSizeLevel(clientInfo);
			config.setClosestDeviceName(closestDeviceName);
			//set device's attribute
			//this must be first step
			config.setLayoutKeyWithDevice(HtmlClientHelper.getLayoutKeyWithDevice(clientInfo,config));
			config.setLayoutKey(HtmlClientHelper.getLayoutKey(clientInfo,config));
			//this must be second step
			HtmlClientHelper.setResourceMapping(clientInfo,config);
			//
			//config.setImageKeyWithoutSize(HtmlClientHelper.getImageKeyWithoutSize(clientInfo,config));
			config.setSharedImageKey(HtmlClientHelper.getSharedImageKey(clientInfo,config));
			config.setImageKey(HtmlClientHelper.getImageKey(clientInfo,config));
			config.setMessageI18NKey(HtmlClientHelper.getMessageI18NKey(clientInfo,config));
			config.setCssKey(HtmlClientHelper.getCssKey(clientInfo, config));
			config.setSharedCssKey(HtmlClientHelper.getSharedCssKey(clientInfo,config));
			config.setSharedCssDeviceCommonKey(HtmlClientHelper.getSharedCssDeviceCommonKey(clientInfo,config));
			config.setCssDeviceCommonKey(HtmlClientHelper.getCssDeviceCommonKey(clientInfo,config));
			config.setAudioKey(HtmlClientHelper.getAudioKey(clientInfo,config));
			//
			this.getDeviceConfigMap().put(cachedKey, config);
			//
			logger.debug("--generate config:" + config.toString());
		}
		return config;
	}

	
	
    /**
     * if return true, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\Nexusone\
     * if return false, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * @param handler
     * @return
     */
    public boolean isDeviceLevelLayoutExist(HtmlClientInfo clientInfo)
    {
    	boolean isDeviceLevelLayoutExist = false;
//    	
//    	String programCode = clientInfo.getProgramCode();
//        String platform = clientInfo.getPlatform();
//        String version = HtmlClientHelper.getVersion(clientInfo.getVersion());
//        String device = clientInfo.getDevice();
//        String product = HtmlClientHelper.getProduct(clientInfo.getProduct());
//
//    	//if can't find the config files under device folder, 
//    	//we need read the config files under screensize folder
//    	String filePath = HtmlClientHelper.DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + device;
//    	
//    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
//    	URL url = cl.getResource(filePath);
//		if(url == null)
//		{
//			isDeviceLevelLayoutExist = false;
//		}
    	return isDeviceLevelLayoutExist;
    }

    /**
     * if return true, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\854x480_480x854\
     * if return false, it means get layout file/image from the closed config such as device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * @param handler
     * @return
     */
    public boolean isScreenSizeLevelConfigExist(HtmlClientInfo clientInfo,String screenSizeDeviceName)
    {
    	boolean isScreenSizeLevelConfigExist = true;
    	
    	String programCode = clientInfo.getProgramCode();
        String platform = clientInfo.getPlatform();
        String version = HtmlClientHelper.getVersion(clientInfo.getVersion());
        String device = clientInfo.getDevice();
        String product = HtmlClientHelper.getProduct(clientInfo.getProduct());

    	//if can't find the config files under device folder, 
    	//we need read the config files under screensize folder
    	String filePath = HtmlClientHelper.DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + device;
    	
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
    	URL url = cl.getResource(filePath);
		if(url == null)
		{
			isScreenSizeLevelConfigExist = false;
		}
    	return isScreenSizeLevelConfigExist;
    }

	public ConcurrentMap<String, HtmlDeviceConfig> getDeviceConfigMap() {
		return deviceConfigMap;
	}
}
