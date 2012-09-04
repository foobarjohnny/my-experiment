/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * CoseFactory.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-17
 */
public class CoseFactory
{
	public static PoiSearchProxy createPoiSearchProxy(TnContext tc)
	{
		return new PoiSearchProxy(tc);
	}
	
	public static PoiSearchProxyV20 createPoiSearch20Proxy(TnContext tc)
	{
		return new PoiSearchProxyV20(tc);
	}
}
