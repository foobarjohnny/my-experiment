/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.framework.html.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.telenav.cserver.browser.util.ReloadablePropertyResourceBundle;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlMessageWrap;


/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlMessageHelper {

    private Map messagesAll = new HashMap();
    private static HtmlMessageHelper instance = new HtmlMessageHelper();
    public static final String DEVICE_FOLDER = "device";
    public static final String MESSAGE_FILE = "message";

    
    /**
     * return server instance as default
     * @return
     */
    public static HtmlMessageHelper getInstance()
    {
    	return instance;
    }
    
    /**
     * 
     */
    public void initMessage(HtmlClientInfo clientInfo)
    {
        String deviceKey = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo).getMessageI18NKey();
        
        Map message = (Map)messagesAll.get(deviceKey);
        if(message == null)
        {
            message = this.getMessageViaFileName(DEVICE_FOLDER + "." + deviceKey + "." + MESSAGE_FILE);
            messagesAll.put(deviceKey, message);
        }
    }
    
    /**
     * Nested hashmap holds the key - message mapping for all the possible devices.
     * This method retrieves message map for the designated device key.
     * If the map doesn't exist yet, getMessageViaFileName is called to retrieve the message properties.
     * 
     * @param key
     * @return hashmap that holds message properties
     */
    private Map getMessageViaKey(String deviceKey)
    {
        Map message = new HashMap();
        message = (Map)messagesAll.get(deviceKey);
        if(message == null)
        {
            message = this.getMessageViaFileName(DEVICE_FOLDER + "." + deviceKey + "." + MESSAGE_FILE);
            messagesAll.put(deviceKey, message);            
        }
        return message;
    }
    
    /**
     * 
     * @param handler
     * @param key
     * @return
     */
    public String getMessageValue(HtmlClientInfo clientInfo,String key)
    {
        String value = "";
        String deviceKey = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo).getMessageI18NKey();
        value = (String)getMessageViaKey(deviceKey).get(key);

        return value;
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public HtmlMessageWrap getMessageWrap(String key)
    {
        Map message = getMessageViaKey(key);
        HtmlMessageWrap messageWrap = new HtmlMessageWrap(message);
        //MessageWrap messageWrap = new MessageWrap();
        return messageWrap;
    }
    
    
    
    /**
     * 
     * @param fileName
     * @return
     */
    private Map getMessageViaFileName(String fileName)
    {
        Map map = new HashMap();
        try
        {
            ResourceBundle serverBundle = ReloadablePropertyResourceBundle.getBundle(fileName);
            
            Enumeration<String> enumeration = serverBundle.getKeys();
            String key="";
            String value = "";
            while(enumeration.hasMoreElements())
            {
                key = enumeration.nextElement();
                value = serverBundle.getString(key);
                
                value = new String(value.getBytes("ISO8859-1"),"UTF-8");
                map.put(key, value);
            }
        }catch(Exception e)
        {
           e.printStackTrace();             
        }        
        
        return map;
    }
}
