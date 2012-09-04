/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

/**
 * GetTicketQuantityRequest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Dec 21, 2010
 * 
 */
public class GetTicketQuantityRequest extends MovieCommonRequest {
	private String showTime;
	private String ticketTheaterId;
	private long userId;
	

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}


	public String getTicketTheaterId() {
		return ticketTheaterId;
	}

	public void setTicketTheaterId(String ticketTheaterId) {
		this.ticketTheaterId = ticketTheaterId;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
