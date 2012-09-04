/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.cose;

import java.util.Date;

/**
 * GasPriceInfo.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-20
 */
public class GasPriceInfo
{
	private double price;
	private int gasType;
	private Date updateTime;
	private String priceProvider;
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}
	/**
	 * @param gasType the gasType to set
	 */
	public void setGasType(int gasType)
	{
		this.gasType = gasType;
	}
	/**
	 * @return the gasType
	 */
	public int getGasType()
	{
		return gasType;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime()
	{
		return updateTime;
	}
	/**
	 * @param priceProvider the priceProvider to set
	 */
	public void setPriceProvider(String priceProvider)
	{
		this.priceProvider = priceProvider;
	}
	/**
	 * @return the priceProvider
	 */
	public String getPriceProvider()
	{
		return priceProvider;
	}
	
	public String toString()
	{
	    StringBuffer sb = new StringBuffer();
	    sb.append("<GasPriceInfo");
	    sb.append("priceProvider=" + this.priceProvider);
	    sb.append("&price=" + this.price);
	    sb.append("&gasType=" + this.gasType);
	    sb.append("&updateTime=" + this.updateTime);
	    sb.append(">");
	    return sb.toString();
	}
}
