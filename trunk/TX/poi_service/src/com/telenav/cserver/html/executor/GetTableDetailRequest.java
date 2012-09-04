/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
/**
 * @TODO	Define the request Object
 * @author  
 * @version 1.0 
 */
public class GetTableDetailRequest extends ExecutorRequest {
	private long partnerPoiId;
	private String SearchDate;
	private String SearchTime;
	private int partySize;

	public long getPartnerPoiId() {
		return partnerPoiId;
	}

	public void setPartnerPoiId(long partnerPoiId) {
		this.partnerPoiId = partnerPoiId;
	}

	public String getSearchDate() {
		return SearchDate;
	}

	public void setSearchDate(String searchDate) {
		SearchDate = searchDate;
	}

	public String getSearchTime() {
		return SearchTime;
	}

	public void setSearchTime(String searchTime) {
		SearchTime = searchTime;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int partySize) {
		this.partySize = partySize;
	}

}
