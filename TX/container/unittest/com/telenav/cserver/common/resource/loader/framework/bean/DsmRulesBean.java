package com.telenav.cserver.common.resource.loader.framework.bean;

public class DsmRulesBean
{
	private String alertDataSrc; // alert_data_src
	private String flowDataSrc; // flow_data_src

	private String needSponsor; // needsponsor
	private String adEngine; // adengine
	private String adType; // adtype
	private String needReview; // needreview
	private String supportTouch; // needreview
	private String isAndroid; // isAndroid
	private String poiFinderVersion; // poiFinderVersion
	private String defaultCountry; // defaultCountry

	public String getAlertDataSrc()
	{
		return alertDataSrc;
	}

	public void setAlertDataSrc(String alertDataSrc)
	{
		this.alertDataSrc = alertDataSrc;
	}

	public String getFlowDataSrc()
	{
		return flowDataSrc;
	}

	public void setFlowDataSrc(String flowDataSrc)
	{
		this.flowDataSrc = flowDataSrc;
	}

	public String getNeedSponsor()
	{
		return needSponsor;
	}

	public void setNeedSponsor(String needSponsor)
	{
		this.needSponsor = needSponsor;
	}

	public String getAdEngine()
	{
		return adEngine;
	}

	public void setAdEngine(String adEngine)
	{
		this.adEngine = adEngine;
	}

	public String getAdType()
	{
		return adType;
	}

	public void setAdType(String adType)
	{
		this.adType = adType;
	}

	public String getNeedReview()
	{
		return needReview;
	}

	public void setNeedReview(String needReview)
	{
		this.needReview = needReview;
	}

	public String getSupportTouch()
	{
		return supportTouch;
	}

	public void setSupportTouch(String supportTouch)
	{
		this.supportTouch = supportTouch;
	}

	public String getIsAndroid()
	{
		return isAndroid;
	}

	public void setIsAndroid(String isAndroid)
	{
		this.isAndroid = isAndroid;
	}

	public String getPoiFinderVersion()
	{
		return poiFinderVersion;
	}

	public void setPoiFinderVersion(String poiFinderVersion)
	{
		this.poiFinderVersion = poiFinderVersion;
	}

	public String getDefaultCountry()
	{
		return defaultCountry;
	}

	public void setDefaultCountry(String defaultCountry)
	{
		this.defaultCountry = defaultCountry;
	}

	@Override
	public String toString()
	{
		return "DsmRulesBean [alertDataSrc=" + alertDataSrc + ", flowDataSrc=" + flowDataSrc + ", neeSponsor=" + needSponsor
				+ ", adEngine=" + adEngine + ", adType=" + adType + ", needReview=" + needReview + ", supportTouch=" + supportTouch
				+ ", isAndroid=" + isAndroid + ", poiFinderVersion=" + poiFinderVersion + ", defaultCountry=" + defaultCountry + "]";
	}

}
