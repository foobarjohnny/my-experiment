/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Each LocalAppInfo object represent a local application, with all properties
 * that it need.
 * 
 * @author kwwang
 * 
 */
public class LocalAppInfo {
	private String name;
	private Map<String, String> props = new HashMap<String, String>();

	public LocalAppInfo(String name)
	{
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addProp(String key,String value)
	{
		this.props.put(key, value);
	}

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

}
