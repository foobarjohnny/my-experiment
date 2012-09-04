package com.telenav.cserver.browser.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.webapp.taglib.TnLayoutTag;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author panzhang
 *
 */
public class DeviceManager {
	
	private static DeviceManager instance = new DeviceManager();
	private ConcurrentMap<String,DeviceConfig> deviceConfigMap = new ConcurrentHashMap<String,DeviceConfig>();
	
	/**
	 * 
	 * @return
	 */
	public static DeviceManager getInstance()
	{
		return instance;
	}
	

	
	public DeviceConfig getDeviceConfig(DataHandler handler)
	{
		String realDevice = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
		String cachedKey = ClientHelper.getCacheKey(handler);
		DeviceConfig config = getDeviceConfigMap().get(cachedKey);
		if(config == null)
		{
			//set default device name
			config = new DeviceConfig();
			config.setRealDeviceName(realDevice);
			config.setCachedKey(cachedKey);
			
			boolean isDeviceLevelLayoutExist = isDeviceLevelLayoutExist(handler);
			config.setDeviceLevelConfigExist(isDeviceLevelLayoutExist);
			
			if(!isDeviceLevelLayoutExist)
			{
				String screenSizeDeviceName = ClientHelper.getDefaultDeviceName(handler);
				config.setDefaultDeviceName(screenSizeDeviceName);
				
				//check if  default device config exist
				boolean isScreenSizeLevelConfigExist = isScreenSizeLevelConfigExist(handler,screenSizeDeviceName);;
				config.setScreenSizeLevelConfigExist(isScreenSizeLevelConfigExist);
	    		if(!isScreenSizeLevelConfigExist)
	    		{
	    			config.setClosestDeviceName(ClientHelper.getClosetDeviceName(handler,screenSizeDeviceName));
	    		}
			}
			//set device's attribute
			config.setLayoutKeyWithDevice(ClientHelper.getLayoutKeyWithDevice(handler,config));
			config.setLayoutKeyWithProduct(ClientHelper.getLayoutKeyWithProduct(handler, config));
			config.setImageKeyWithoutSize(ClientHelper.getImageKeyWithoutSize(handler,config));
			config.setImageKey(ClientHelper.getImageKey(handler,config));
			config.setMessageI18NKey(ClientHelper.getMessageI18NKey(handler,config));
			//
			this.getDeviceConfigMap().put(cachedKey, config);
			//
			System.out.println("--generate config:" + config.toString());
		}
		return config;
	}

	
	
    /**
     * if return true, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\Nexusone\
     * if return false, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * @param handler
     * @return
     */
    public boolean isDeviceLevelLayoutExist(DataHandler handler)
    {
    	boolean isDeviceLevelLayoutExist = true;
    	
    	String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = ClientHelper.getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        String product = ClientHelper.getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));

    	//if can't find the config files under device folder, 
    	//we need read the config files under screensize folder
    	String fielPath = TnLayoutTag.DEVICE_FOLDER + "/" + carrier + "/" + platform + "/" + version  + "/" + product  + "/" + device;
    	
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
    	URL url = cl.getResource(fielPath);
		if(url == null)
		{
			isDeviceLevelLayoutExist = false;
		}
    	return isDeviceLevelLayoutExist;
    }

    /**
     * if return true, it means get layout file/image from device\ATT\ANDROID\6_2_01\ATT\854x480_480x854\
     * if return false, it means get layout file/image from the closed config such as device\ATT\ANDROID\6_2_01\ATT\800x480_480x800\
     * @param handler
     * @return
     */
    public boolean isScreenSizeLevelConfigExist(DataHandler handler,String screenSizeDeviceName)
    {
    	boolean isScreenSizeLevelConfigExist = true;
    	
    	String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = ClientHelper.getVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        String device = screenSizeDeviceName;
        String product = ClientHelper.getProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));

    	//if can't find the config files under device folder, 
    	//we need read the config files under screensize folder
    	String filePath = TnLayoutTag.DEVICE_FOLDER + "/" + carrier + "/" + platform + "/" + version  + "/" + product  + "/" + device;
    	
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
    	URL url = cl.getResource(filePath);
		if(url == null)
		{
			isScreenSizeLevelConfigExist = false;
		}
    	return isScreenSizeLevelConfigExist;
    }

	public ConcurrentMap<String, DeviceConfig> getDeviceConfigMap() {
		return deviceConfigMap;
	}
}
