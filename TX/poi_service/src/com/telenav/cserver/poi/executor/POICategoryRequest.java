/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: POICategoryRequest.java
 * Package name: com.telenav.cserver.poi.handler
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:57:41 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @author lwei (lwei@telenav.cn) 1:57:41 PM
 */
public class POICategoryRequest extends ExecutorRequest {
	private String poiFinderVersion;
	private long id;
	private String name;
	private boolean isMostPopular;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMostPopular() {
		return isMostPopular;
	}
	public void setMostPopular(boolean isMostPopular) {
		this.isMostPopular = isMostPopular;
	}
	public String getPoiFinderVersion() {
		return poiFinderVersion;
	}
	public void setPoiFinderVersion(String poiFinderVersion) {
		this.poiFinderVersion = poiFinderVersion;
	}
}
