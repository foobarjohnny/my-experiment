/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import javax.servlet.http.HttpServletRequest;

import org.easymock.IArgumentMatcher;

/**
 * HttpServletRequestMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-27
 */
public class HttpServletRequestMatcher implements IArgumentMatcher {

	private HttpServletRequest o;

	public HttpServletRequestMatcher(HttpServletRequest o) {
		this.o = o;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof HttpServletRequest)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
