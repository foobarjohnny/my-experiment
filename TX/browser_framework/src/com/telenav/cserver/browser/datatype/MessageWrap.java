/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.browser.datatype;

import java.util.Map;

import com.telenav.cserver.browser.util.MessageHelper;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-7-15
 */
public class MessageWrap {
	private Map messages;
	private boolean readFromServer = false;
	
	public MessageWrap(Map messages,boolean readFromServer)
	{
		this.messages = messages;
		this.readFromServer = readFromServer;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key)
	{
	    String value = "";
	    
	    if(isReadFromServer())
	    {
    		value = (String)this.getMessages().get(key);
    		
    		if(value == null)
    		{
    			value = key;
    		}
	    }
	    else
	    {
	        value = "$(" + key + ")";
	    }
		
		return value;
	}

	/**
     * 
     * @param key
     * @return
     */
    public String getValue(String key)
    {
        String value = "";

        value = (String)this.getMessages().get(key);
        
        if(value == null)
        {
            value = key;
        }
        
        return value;
    }

    
    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getValue(String key,String defaultValue)
    {
        String value = "";

        value = (String)this.getMessages().get(key);
        
        if(value == null)
        {
            value = defaultValue;
        }
        
        return value;
    }
    
	public Map getMessages() {
		return messages;
	}

	public void setMessages(Map messages) {
		this.messages = messages;
	}

    public boolean isReadFromServer() {
        return readFromServer;
    }

    public void setReadFromServer(boolean readFromServer) {
        this.readFromServer = readFromServer;
    }
}
