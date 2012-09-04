/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;

/**
 * ByteArrayWrapperMatcher.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-23
 */
public class ByteArrayWrapperMatcher  implements IArgumentMatcher{
	private ByteArrayWrapper byteArrayWrapper;

	public ByteArrayWrapperMatcher(ByteArrayWrapper byteArrayWrapper) {
		this.byteArrayWrapper = byteArrayWrapper;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof ByteArrayWrapper)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}


}
