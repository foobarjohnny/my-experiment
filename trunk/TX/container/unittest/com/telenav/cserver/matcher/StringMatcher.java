/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

/**
 * StringMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
public class StringMatcher implements IArgumentMatcher {
	
	private String str;

	public StringMatcher(String str) {
		this.str = str;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof String)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
