 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.addresssharing;

/**
 * The request aid to get trigger action for FetchSharedAddress pin.
 * @author pzhang
 * 2010-08-03
 */
public class FetchSharedAddressRequest
{   
	private long userId;
	private String contextString;

   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("FetchSharedAddressRequest=[");
       sb.append(", userId=").append(this.userId);
       sb.append("]");
       return sb.toString();
   }


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}


	public String getContextString() {
		return contextString;
	}


	public void setContextString(String contextString) {
		this.contextString = contextString;
	}
}
