/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.browser.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.browser.datatype.MessageWrap;
import com.telenav.tnbrowser.util.DataHandler;


/**
 * @author pzhang
 *
 * @version 1.0, 2009-7-15
 */
public class MessageHelper {

    private Map messagesAll = new HashMap();
    private static MessageHelper instanceClient = new MessageHelper(false);
    private static MessageHelper instanceServer = new MessageHelper(true);
    public static final String DEVICE_FOLDER = "device";
    public static final String MESSAGE_FILE = "message";
    
    //true:read message from server
    //false:turn key, then read message from client
    private boolean readFromServer = false;
    
    /**
     * 
     */
    private MessageHelper(boolean readFromServer)
    {
    	this.readFromServer = readFromServer;
    }
    
    /**
     * 
     * @return
     */
    public static MessageHelper getInstance(boolean readFromServer)
    {
    	if(readFromServer)
    		return instanceServer;
    	else
    		return instanceClient;
    }
    
    /**
     * return server instance as default
     * @return
     */
    public static MessageHelper getInstance()
    {
    	return instanceServer;
    }
    
    /**
     * 
     */
    public void initMessage(DataHandler handler)
    {
        if(isReadFromServer())
        {
            String deviceKey = DeviceManager.getInstance().getDeviceConfig(handler).getMessageI18NKey();
            
            Map message = (Map)messagesAll.get(deviceKey);
            if(message == null)
            {
                message = this.getMessageViaFileName(DEVICE_FOLDER + "." + deviceKey + "." + MESSAGE_FILE);
                messagesAll.put(deviceKey, message);
            }
        }
    }
    
    /**
     * 
     * @param key
     * @return
     */
    private Map getMessageViaKey(String deviceKey)
    {
        Map message = new HashMap();
        if(isReadFromServer())
        {
            message = (Map)messagesAll.get(deviceKey);
            if(message == null)
            {
                message = this.getMessageViaFileName(DEVICE_FOLDER + "." + deviceKey + "." + MESSAGE_FILE);
                messagesAll.put(deviceKey, message);            
            }
        }
        return message;
    }
    
    /**
     * 
     * @param handler
     * @param key
     * @return
     */
    public String getMessageValue(DataHandler handler,String key)
    {
        String value = "";
        if(isReadFromServer())
        {
            String deviceKey = DeviceManager.getInstance().getDeviceConfig(handler).getMessageI18NKey();
            value = (String)getMessageViaKey(deviceKey).get(key);
        }
        else
        {
        //
            value = "$(" + key + ")";
        }
        
        return value;
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public MessageWrap getMessageWrap(String key)
    {
        Map message = getMessageViaKey(key);
        MessageWrap messageWrap = new MessageWrap(message,this.isReadFromServer());
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
        loadKey(map,generateCommonKey(fileName));
        loadKey(map,fileName);
        
        if(map.size()==0){
        	CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        	cli.setFunctionName("getMessageViaFileName");
        	cli.addData("MissingResourceException:message.properties","can not find message configuration file,the resource file path is:"+ fileName);
        	cli.complete();
        }
        return map;
    }
    
    private void loadKey(Map map, String filePath){
    	try{
    		ResourceBundle serverBundle = ReloadablePropertyResourceBundle.getBundle(filePath);
    		Enumeration<String> enumeration = serverBundle.getKeys();
            String key="";
            String value = "";
            while(enumeration.hasMoreElements())
            {
                key = enumeration.nextElement();
                value = serverBundle.getString(key);
                map.put(key, value);          
            }
    	}catch(MissingResourceException e){
    		
    	}
    }
    
    private String generateCommonKey(String fileName){
    	String[] strArray = fileName.split("\\.");
		StringBuffer commonKeysb = new StringBuffer();
		for (int i = 0; i < strArray.length; i++) {
			// Remove the device level key
			if (i == strArray.length - 3) {
				continue;
			}
			commonKeysb.append(strArray[i] + ".");
		}
		commonKeysb.setLength(commonKeysb.length() - 1);
    	return commonKeysb.toString();
    }
	
	
    public boolean isReadFromServer() {
        return readFromServer;
    }

    public void setReadFromServer(boolean readFromServer) {
        this.readFromServer = readFromServer;
    }
}
