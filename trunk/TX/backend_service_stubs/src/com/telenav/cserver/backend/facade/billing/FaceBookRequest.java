/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

/**
 * FaceBookRequest
 * 
 * @author kwwang
 * 
 */
public class FaceBookRequest extends BillingFacadeCommonRequest {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
