/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: POICategoryResponse.java
 * Package name: com.telenav.cserver.poi.handler
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:58:21 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.executor;

import org.json.me.JSONObject;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @author pzhang (panzhang@telenav.cn) 2010/09/07PM
 */
public class POICategoryResponse extends ExecutorResponse {
	private JSONObject categoryList;

	public JSONObject getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(JSONObject categoryList) {
		this.categoryList = categoryList;
	}
}
