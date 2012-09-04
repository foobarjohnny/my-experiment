/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade.billing;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * BillingFacadeRequest
 * 
 * @author kwwang
 * 
 */
public class BillingFacadeCommonRequest {
	private UserProfile user;
	private TnContext tc;

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public TnContext getTc() {
		return tc;
	}

	public void setTc(TnContext tc) {
		this.tc = tc;
	}

}
