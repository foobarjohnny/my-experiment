/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.dispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatcher is for dispatching the requests of client to different backend
 * servers by different regions.
 * 
 * @author kwwang
 * 
 */
public class Dispatcher {

	private Map<String, DispatcherItem> dispatcherSettings = new HashMap<String, DispatcherItem>();

	public Map<String, DispatcherItem> getDispatcherSettings() {
		return dispatcherSettings;
	}

	public void setDispatcherSettings(Map<String, DispatcherItem> settings) {
		this.dispatcherSettings = settings;
	}

	public String getUrl(String region) {
		return getDispatcherSettings().get(region).url;
	}

	public int getTimeout(String region) {
		return getDispatcherSettings().get(region).timeout;
	}

	/**
	 * Dispatcher Item
	 * 
	 * @author kwwang
	 * 
	 */
	static class DispatcherItem {
		public String url;
		public int timeout;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

	}
}
