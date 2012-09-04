package com.telenav.cserver.poidetail.executor.v20;

import com.telenav.cserver.framework.executor.ExecutorRequest;

public class POIDetailsRequest extends ExecutorRequest
{
	private long poiId;

	public long getPoiId() {
		return poiId;
	}

	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}
}
