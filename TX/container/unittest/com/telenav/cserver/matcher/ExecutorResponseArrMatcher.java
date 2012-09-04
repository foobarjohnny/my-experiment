/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * TxNodeResponseFormatter_format.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-27
 */
public class ExecutorResponseArrMatcher implements IArgumentMatcher{
	/**
	 * 
	 */
	public ExecutorResponseArrMatcher(ExecutorResponse[] r) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean matches(Object obj) {
		if (obj instanceof ExecutorResponse[])
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
