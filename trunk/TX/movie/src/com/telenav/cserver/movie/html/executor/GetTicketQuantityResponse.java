/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;
import com.telenav.cserver.movie.html.datatypes.TicketItem;

/**
 * GetTicketQuantityResponse.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 Dec 21, 2010
 * 
 */
public class GetTicketQuantityResponse extends MovieCommonResponse {

	private List<TicketItem> ticketList = new ArrayList<TicketItem>();

	private double surcharge;
	private double convenienceCharge;

	public List<TicketItem> getTicketList() {
		return this.ticketList;
	}

	public void setTicketList(List<TicketItem> ticketList) {
		this.ticketList = ticketList;
	}

	public double getSurcharge() {
		return this.surcharge;
	}

	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;

	}
	
	public double getConvenienceCharge() {
		return convenienceCharge;
	}

	public void setConvenienceCharge(double convenienceCharge) {
		this.convenienceCharge = convenienceCharge;
	}
}