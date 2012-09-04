/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

/**
 * LoadOrdersMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-4
 */
public class ByteArrMatcher implements IArgumentMatcher{
	private byte[] bytes;

	public ByteArrMatcher(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof byte[])
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
