/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * TestServiceItem
 * @author kwwang
 *
 */
public class TestServiceItem {
	
	protected ServiceItem serviceItem;
	
	@Before
	public void init()
	{
		serviceItem=new ServiceItem();
		List<String> actions=new ArrayList<String>();
		actions.add("combinationSyncResource");
		actions.add("syncServiceLocator");
		actions.add("syncResource");
		serviceItem.setActions(actions);
		serviceItem.setServiceDomainName("/resource-cserver/telenav-server-pb");
		serviceItem.setType("resource-cserver");
		
		Map<String,String> urlMap =new HashMap<String, String>();
		urlMap.put("resource.http", "http://localhost:8080/resource-cserver");
		urlMap.put("resource.http.9000", "http://localhost:9000/resource-cserver");
		serviceItem.setUrlMap(urlMap);
	}
	
	@Test
	public void deepCopy()
	{
		ServiceItem cloneItem=serviceItem.deepCopy();
		Assert.assertEquals("/resource-cserver/telenav-server-pb", cloneItem.getServiceDomainName());
		Assert.assertEquals("resource-cserver", cloneItem.getType());
		Assert.assertEquals(3, cloneItem.getActions().size());
		Assert.assertEquals(2, cloneItem.getUrlMap().keySet().size());
	
		Assert.assertEquals("http://localhost:8080/resource-cserver", cloneItem.getUrlMap().get("resource.http"));
		cloneItem.getUrlMap().put("resource.http", "wrong dns");
		Assert.assertEquals("http://localhost:8080/resource-cserver", serviceItem.getUrlMap().get("resource.http"));
	}
}
