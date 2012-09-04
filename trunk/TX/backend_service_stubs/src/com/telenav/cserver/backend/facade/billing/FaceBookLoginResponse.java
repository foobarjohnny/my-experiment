/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

/**
 * FaceBookLoginResponse
 * 
 * @author kwwang
 * 
 */
public class FaceBookLoginResponse extends BillingFacadeCommonResponse {
	boolean hasFaceBookAccountBound;
	
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public boolean isHasFaceBookAccountBound() {
		return hasFaceBookAccountBound;
	}

	public void setHasFaceBookAccountBound(boolean hasFaceBookAccountBound) {
		this.hasFaceBookAccountBound = hasFaceBookAccountBound;
	}
}
