/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.cose;

import java.util.Date;

/**
 * TnPriceInfo.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class TnPriceInfo
{
	private double price;
	private String product;
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
	 * @param product the product to set
	 */
	public void setProduct(String product)
	{
		this.product = product;
	}
	/**
	 * @return the product
	 */
	public String getProduct()
	{
		return product;
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
}
