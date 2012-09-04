/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.unittestutil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * XMLfileContainer.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-13
 */
public class XMLfileContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Element> map = new HashMap<String, Element>();

	public Element getObject(String key) {
		return map.get(key);
	}

	public void setObject(String key, Element value) {
		map.put(key, value);
	}
	
	public int getCount(){
		return map.size();
	}
	
}
