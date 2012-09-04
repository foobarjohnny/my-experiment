/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.navstar;

import com.telenav.navstar.proxy.NavResult;

/**
 * a encapsulate class for {@link NavResult}
 * <p>
 * 
 * get a {@link NavResult} through the method
 * {@link NavResultResponse#getNavResult()} <br>
 * you should check the {@link NavResult} is <code>null</code> by yourself
 * 
 * @author mmwang
 * @version 1.0 2010-7-13
 * 
 */
public class NavResultResponse
{

	private NavResult navResult;

	public NavResultResponse(NavResult result)
	{
		this.navResult = result;
	}

	public NavResult getNavResult()
	{
		return navResult;
	}
}
