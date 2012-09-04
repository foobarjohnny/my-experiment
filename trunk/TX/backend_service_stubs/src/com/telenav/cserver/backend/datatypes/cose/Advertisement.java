/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.cose;

import java.util.Date;

/**
 * Advertisement.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-16
 */
public class Advertisement
{
	private String sourceAdId;
	private String adSource;
	private String message;
	private String shortMessage;
	private boolean payPerCall;
	private boolean payPerClick;
	private boolean poundEnabled;
	private boolean starEnabled;
	private String eventPrice;
	private String adPageUrl;
	private String buyerName;
	private Date startTime;
	private Date endTime;
	private int ranking;
	private int adType;
	/**
	 * @param sourceAdId the sourceAdId to set
	 */
	public void setSourceAdId(String sourceAdId)
	{
		this.sourceAdId = sourceAdId;
	}
	/**
	 * @return the sourceAdId
	 */
	public String getSourceAdId()
	{
		return sourceAdId;
	}
	/**
	 * @param adSource the adSource to set
	 */
	public void setAdSource(String adSource)
	{
		this.adSource = adSource;
	}
	/**
	 * @return the adSource
	 */
	public String getAdSource()
	{
		return adSource;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	/**
	 * @param shortMessage the shortMessage to set
	 */
	public void setShortMessage(String shortMessage)
	{
		this.shortMessage = shortMessage;
	}
	/**
	 * @return the shortMessage
	 */
	public String getShortMessage()
	{
		return shortMessage;
	}
	/**
	 * @param payPerCall the payPerCall to set
	 */
	public void setPayPerCall(boolean payPerCall)
	{
		this.payPerCall = payPerCall;
	}
	/**
	 * @return the payPerCall
	 */
	public boolean getPayPerCall()
	{
		return payPerCall;
	}
	/**
	 * @param payPerClick the payPerClick to set
	 */
	public void setPayPerClick(boolean payPerClick)
	{
		this.payPerClick = payPerClick;
	}
	/**
	 * @return the payPerClick
	 */
	public boolean getPayPerClick()
	{
		return payPerClick;
	}
	/**
	 * @param poundEnabled the poundEnabled to set
	 */
	public void setPoundEnabled(boolean poundEnabled)
	{
		this.poundEnabled = poundEnabled;
	}
	/**
	 * @return the poundEnabled
	 */
	public boolean getPoundEnabled()
	{
		return poundEnabled;
	}

	/**
	 * @param eventPrice the eventPrice to set
	 */
	public void setEventPrice(String eventPrice)
	{
		this.eventPrice = eventPrice;
	}
	/**
	 * @return the eventPrice
	 */
	public String getEventPrice()
	{
		return eventPrice;
	}
	/**
	 * @param adPageUrl the adPageUrl to set
	 */
	public void setAdPageUrl(String adPageUrl)
	{
		this.adPageUrl = adPageUrl;
	}
	/**
	 * @return the adPageUrl
	 */
	public String getAdPageUrl()
	{
		return adPageUrl;
	}
	/**
	 * @param buyerName the buyerName to set
	 */
	public void setBuyerName(String buyerName)
	{
		this.buyerName = buyerName;
	}
	/**
	 * @return the buyerName
	 */
	public String getBuyerName()
	{
		return buyerName;
	}
	
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime()
	{
		return endTime;
	}
	/**
	 * @param starEnabled the starEnabled to set
	 */
	public void setStarEnabled(boolean starEnabled)
	{
		this.starEnabled = starEnabled;
	}
	/**
	 * @return the starEnabled
	 */
	public boolean isStarEnabled()
	{
		return starEnabled;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}
	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(int ranking)
	{
		this.ranking = ranking;
	}
	/**
	 * @return the ranking
	 */
	public int getRanking()
	{
		return ranking;
	}
	/**
	 * @param adType the adType to set
	 */
	public void setAdType(int adType)
	{
		this.adType = adType;
	}
	/**
	 * @return the adType
	 */
	public int getAdType()
	{
		return adType;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Ad[");
		sb.append("sourceAdId=");
		sb.append(this.sourceAdId);
		sb.append(", adPageUrl=");
		sb.append(this.adPageUrl);
		sb.append(", adType=");
		sb.append(this.adType);
		sb.append("]>");
		return sb.toString();
	}
}
