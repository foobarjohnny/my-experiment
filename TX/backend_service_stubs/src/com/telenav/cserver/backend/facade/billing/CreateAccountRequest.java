/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;
/**
 * CreateAccountRequest
 * @author kwwang
 *
 */
public class CreateAccountRequest extends BillingFacadeCommonRequest {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private long userId = -1; // pass in the userId of device account.
	//associate the device account with the scout.me account.
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	
}
