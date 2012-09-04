/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.cserver.common.resource.LoadOrders;

/**
 * LoadOrdersMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-4
 */
public class LoadOrdersMatcher implements IArgumentMatcher{
	private LoadOrders loadOrders;

	public LoadOrdersMatcher(LoadOrders loadOrders) {
		this.loadOrders = loadOrders;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof LoadOrders)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
