/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;


/**
 * EmailLoginRequest
 * @author kwwang
 *
 */
public class EmailLoginRequest extends BillingFacadeCommonRequest {
	
	private String email;
	private String password;

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
