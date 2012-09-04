/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.html.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;

/**
 * 	This help class can get the feature flag from feature.properties
 * 	The file path:device\ATTNAVPROG\ANDROID\7_1\default\common
 * @author  panZhang@telenav.cn
 * @version 1.0 2012-3-12
 */
public class HtmlFeatureHelper {
	private static HtmlFeatureHelper instance = new HtmlFeatureHelper(); 
	private Logger logger = Logger.getLogger(HtmlFeatureHelper.class);
	
	//cache the feature list when first time use
	private ConcurrentMap<String,Map> featureMapAll = new ConcurrentHashMap<String,Map>();
	/**
	 * 
	 * @return
	 */
	public static HtmlFeatureHelper getInstance()
	{
		return instance;
	}
	
	/**
	 * cache the feature list when first time use
	 * @param clientInfo
	 * @return: feature mapping
	 */
	private Map<String,String> getFeatureMap(HtmlClientInfo clientInfo)
	{
		HtmlDeviceConfig config = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo);
	  	String fielPath = "device." + config.getLayoutKey() + ".feature";
		String cacheKey = config.getLayoutKey();
	  	
	  	Map<String,String> featureMap = featureMapAll.get(cacheKey);
	  	if(featureMap == null)
	  	{
	  		featureMap = new HashMap();
	    	try
	        {
	            ResourceBundle serverBundle = ResourceBundle.getBundle(fielPath);
	            Enumeration<String> enumeration = serverBundle.getKeys();
	            String key;
	            while(enumeration.hasMoreElements())
	            {
	            	key = enumeration.nextElement();
	            	//System.out.println(key + "," + serverBundle.getString(key));
	            	featureMap.put(key, serverBundle.getString(key));
	            }
	            //
	            featureMapAll.put(cacheKey, featureMap);
	        }
	        catch(Exception e)
	        {
	        	logger.debug("error during read feature file:" + e.getMessage());
	        }
        }

        return featureMap;
	}
	
	/**
	 * return true when support the feature
	 * @param clientInfo
	 * @param feature key
	 * @return support or not flag
	 */
	public boolean supportFeature(HtmlClientInfo clientInfo,String featureKey)
	{
		String value = getFeature(clientInfo,featureKey);
	  	boolean support = false;
        //
        if("Y".equalsIgnoreCase(value))
        {
        	support = true;
        }
        return support;
	}
	
	/**
	 * return true when disable the feature
	 * @param clientInfo
	 * @param feature key
	 * @return disable flag
	 */
	public boolean disableFeature(HtmlClientInfo clientInfo,String featureKey)
	{
		String value = getFeature(clientInfo,featureKey);
	  	boolean disable = false;
        //
        if("N".equalsIgnoreCase(value))
        {
        	disable = true;
        }
        return disable;
	}
	
	public String getFeature(HtmlClientInfo clientInfo,String featureKey)
	{
		Map<String,String> featureMap = getFeatureMap(clientInfo);
		return HtmlCommonUtil.getString(featureMap.get(featureKey));
	}
}
