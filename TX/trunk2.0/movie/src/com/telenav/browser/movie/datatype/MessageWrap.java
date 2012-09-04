/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.browser.movie.datatype;

import java.util.Map;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-4-22
 */
public class MessageWrap {
	private Map messages;
	
	public MessageWrap(Map messages)
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
		String value = (String)this.getMessages().get(key);
		
		if(value == null)
		{
			value = key;
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
