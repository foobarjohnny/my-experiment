/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


/**
 * LoadOrders.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class LoadOrders 
{
	private static Logger logger = Logger.getLogger(LoadOrders.class);

	private List<LoadOrder> orders = new ArrayList<LoadOrder>(4);
	
	public List<LoadOrder> getOrders()
	{
		return orders;
	}
	
	
	public void addOrder(LoadOrder order)
	{
		orders.add(order);
	}
	

	private boolean lowerCase = false;
	
	/**
	 * whether resource file name is in lower case
	 * 
	 * @return
	 */
	public boolean isLowerCase()
	{
		return lowerCase;
	}
	
	public void setLowerCase(boolean lowerCase)
	{
		this.lowerCase = lowerCase;
	}

	/**
	 * add string as order, ignore case, seperated by ','
	 * 
	 * @param orders like "locale", "locale,platform"
	 */
	public void addOrderString(String orderString)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Parsing orderString:" + orderString);
		}
		if(orderString == null || orderString.length() == 0)
		{
			return;
		}
		StringTokenizer token = new StringTokenizer(orderString, ",");
		while(token.hasMoreTokens())
		{
			String str = token.nextToken().trim().toLowerCase();
			LoadOrder loadOrder = LoadOrderManager.getLoadOrder(str);
			if(loadOrder == null)
			{
				// warning log
				logger.fatal("Unexpected load order:" + str);
			}
			else
			{
				addOrder(loadOrder);
			}
				

		}
	}
	
	public String toString()
	{
		return "isLowerCase: " + isLowerCase() + ",orders:" + orders;
	}
}
