/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

/**
 * CancelEmailLoginResponse
 * 
 * @author kwwang
 * 
 */
public class BillingFacadeCommonResponse {
	//by default, -1 errorCode is defined by cserver.
	private String errorCode="-1";

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
