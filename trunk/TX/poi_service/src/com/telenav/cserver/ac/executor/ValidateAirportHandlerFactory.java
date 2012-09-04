/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.poi.datatypes.PoiConstants;

/**
 * ValidateAirportHandlerFactory
 * 
 * @author kwwang
 * 
 */
public class ValidateAirportHandlerFactory implements PoiConstants {
	private static ValidateAirportHandler commonValidateAirportHandler = new CommonValidateAirportHandler();
	private static ValidateAirportHandler brValidateAirportHandler = new BRValidateAirportHandler();

	public static ValidateAirportHandler getValidateAirportHandler(
			String programCode) {
		if (VIVO_PROG.equals(programCode))
			return brValidateAirportHandler;
		else
			return commonValidateAirportHandler;
	}
}
