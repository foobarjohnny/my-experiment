/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @TODO	Define the request Object
 * @author jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 */
public class HtmlHotelRequest extends ExecutorRequest {

	private long PoiId;
	private boolean isDummy;

	public long getPoiId() {
		return PoiId;
	}

	public void setPoiId(long poiId) {
		PoiId = poiId;
	}

	public boolean isDummy() {
		return isDummy;
	}

	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}

}
