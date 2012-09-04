/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.poi.datatypes.PoiConstants;

/**
 * PoiSearchHandlerFactory
 * @author kwwang
 *
 */
public class PoiSearchHandlerFactory implements PoiConstants {
	private static PoiSearchHandler commonPoiHandler = new CommonPoiSearchHandler();
	private static PoiSearchHandler brPoiHandler = new BRPoiSearchHandler();

	public static PoiSearchHandler getPoiSearchHandlerBy(String programCode) {
		if (VIVO_PROG.equals(programCode))
			return brPoiHandler;
		else
			return commonPoiHandler;
	}
}
