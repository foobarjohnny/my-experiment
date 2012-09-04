/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import com.telenav.cserver.movie.html.datatypes.BookingInfoItem;

/**
 * BookTicketRequest.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 March 9, 2011
 * 
 */
public class BookTicketRequest extends MovieCommonRequest {

	private BookingInfoItem bookingInfo;
	private long userId;

	public BookingInfoItem getBookingInfo() {
		return bookingInfo;
	}

	public void setBookingInfo(BookingInfoItem bookingInfo) {
		this.bookingInfo = bookingInfo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
