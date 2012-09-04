/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;
/**
 * CancelEmailLoginResponse
 * @author kwwang
 *
 */
public class EmailLoginResponse extends BillingFacadeCommonResponse {
	private long userId=-1L;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
