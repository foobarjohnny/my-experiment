/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.datatypes.AdsDetail;

/**
 * @TODO	Define the response Object
 * @author jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 *
 */
public class HtmlAdsResponse extends ExecutorResponse
{
	private String operateType;
	private long adId;
	private AdsDetail adsDetail;

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nadsDetail:");
		if(adsDetail != null)
		{
			sb.append(this.getAdsDetail().toString());
		}
		
		return sb.toString();
	}
	
	
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}


	public AdsDetail getAdsDetail() {
		return adsDetail;
	}


	public void setAdsDetail(AdsDetail adsDetail) {
		this.adsDetail = adsDetail;
	}


	public long getAdId() {
		return adId;
	}


	public void setAdId(long adId) {
		this.adId = adId;
	}
}
