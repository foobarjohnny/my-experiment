/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.poi.datatypes.PoiConstants;

/**
 * ValidateAddressHandlerFactory
 * 
 * @author kwwang
 * @date 2011-6-30
 */
public class ValidateAddressHandlerFactory implements PoiConstants {
	private static ValidateAddressHandler commonValidateAddressHandler = new CommonValidateAddressHandler();

	private static ValidateAddressHandler brValidateAddressHandler = new BRValidateAddressHandler();

	public static ValidateAddressHandler getValidateAddressHandlerBy(String type) {
		if (VIVO_PROG.equals(type))
			return brValidateAddressHandler;
		else
			return commonValidateAddressHandler;
	}
}
