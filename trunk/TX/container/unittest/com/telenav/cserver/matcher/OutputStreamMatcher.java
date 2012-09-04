/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import java.io.OutputStream;

import org.easymock.IArgumentMatcher;

/**
 * HttpServletRequestMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-27
 */
public class OutputStreamMatcher implements IArgumentMatcher {

	private OutputStream o;

	public OutputStreamMatcher(OutputStream o) {
		this.o = o;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof OutputStream)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
