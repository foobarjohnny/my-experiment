/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @TODO	Define the request Object
 * @author panzhang@telenav.cn
 * @version 1.0 3-3, 2011
 *
 */
public class HtmlAdsRequest extends ExecutorRequest
{
    private String operateType;
    private long adId;
    private String deviceHeight;
    private String deviceWidth;
    private String logoWidth;
    private String logoHeight;
	private boolean isDummy;
	private boolean fromPoiDetail = false;
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nadId:");
		sb.append(this.getAdId());
		return sb.toString();
	}

	public String getOperateType() {
		return operateType;
	}


	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}
	
    public String getDeviceHeight()
    {
        return deviceHeight;
    }

    public void setDeviceHeight(String deviceHeight)
    {
        this.deviceHeight = deviceHeight;
    }

    public String getDeviceWidth()
    {
        return deviceWidth;
    }

    public void setDeviceWidth(String deviceWidth)
    {
        this.deviceWidth = deviceWidth;
    }
    
    public String getLogoWidth()
    {
        return logoWidth;
    }

    public void setLogoWidth(String logoWidth)
    {
        this.logoWidth = logoWidth;
    }

    public String getLogoHeight()
    {
        return logoHeight;
    }

    public void setLogoHeight(String logoHeight)
    {
        this.logoHeight = logoHeight;
    }
    
	public boolean isDummy() {
		return isDummy;
	}

	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}

	public boolean isFromPoiDetail() {
		return fromPoiDetail;
	}

	public void setFromPoiDetail(boolean fromPoiDetail) {
		this.fromPoiDetail = fromPoiDetail;
	}

	
}
