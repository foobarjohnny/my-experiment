/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.framework.html.datatype;

import java.util.Map;

/**
 * Simple Wrapper for message hashmap.
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlMessageWrap {
	private Map messages;
	
	public HtmlMessageWrap(Map messages)
	{
		this.messages = messages;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key)
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
}
