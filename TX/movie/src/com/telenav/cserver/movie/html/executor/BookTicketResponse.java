/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

/**
 * BookTicketResponse.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 Dec 21, 2010
 * 
 */
public class BookTicketResponse extends MovieCommonResponse {
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
