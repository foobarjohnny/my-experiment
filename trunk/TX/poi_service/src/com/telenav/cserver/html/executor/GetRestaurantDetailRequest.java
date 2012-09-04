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
public class GetRestaurantDetailRequest extends ExecutorRequest {

	private long partnerPoiId;

	public long getPartnerPoiId() {
		return partnerPoiId;
	}

	public void setPartnerPoiId(long partnerPoiId) {
		this.partnerPoiId = partnerPoiId;
	}
}
