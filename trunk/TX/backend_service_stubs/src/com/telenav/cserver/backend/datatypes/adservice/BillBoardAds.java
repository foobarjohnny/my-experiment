/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.adservice;


/**
 * BillBoardAds
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-3-16
 */
public class BillBoardAds
{
    private long adsId;
    private String adsUrl;
    private long initialViewTime;
    private long detailViewTime;
    private long poiViewTime;
    private long expirationTime;
    private GeoFence geoFence;
    private String adsSource;
    public long getAdsId()
    {
        return adsId;
    }
    public void setAdsId(long adsId)
    {
        this.adsId = adsId;
    }
    public long getInitialViewTime()
    {
        return initialViewTime;
    }
    public void setInitialViewTime(long initialViewTime)
    {
        this.initialViewTime = initialViewTime;
    }
    public long getDetailViewTime()
    {
        return detailViewTime;
    }
    public void setDetailViewTime(long detailViewTime)
    {
        this.detailViewTime = detailViewTime;
    }
    public long getPoiViewTime()
    {
        return poiViewTime;
    }
    public void setPoiViewTime(long poiViewTime)
    {
        this.poiViewTime = poiViewTime;
    }
    public long getExpirationTime()
    {
        return expirationTime;
    }
    public void setExpirationTime(long expirationTime)
    {
        this.expirationTime = expirationTime;
    }
    public GeoFence getGeoFence()
    {
        return geoFence;
    }
    public void setGeoFence(GeoFence geoFence)
    {
        this.geoFence = geoFence;
    }
    public String getAdsUrl()
    {
        return adsUrl;
    }
    public void setAdsUrl(String adsUrl)
    {
        this.adsUrl = adsUrl;
    }
	public String getAdsSource() {
		return adsSource;
	}
	public void setAdsSource(String adsSource) {
		this.adsSource = adsSource;
	}
    
    
}
