/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import javax.servlet.http.HttpServletResponse;

import org.easymock.IArgumentMatcher;

/**
 * HttpServletRequestMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-27
 */
public class HttpServletResponseMatcher implements IArgumentMatcher {

	private HttpServletResponse o;

	public HttpServletResponseMatcher(HttpServletResponse o) {
		this.o = o;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof HttpServletResponse)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
