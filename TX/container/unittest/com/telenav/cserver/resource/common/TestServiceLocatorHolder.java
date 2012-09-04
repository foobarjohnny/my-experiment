/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.resource.common;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.resource.datatypes.BrowserServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestServiceLocatorHolder
 * 
 * @author kwwang
 * 
 */
public class TestServiceLocatorHolder {
	private ServiceLocatorHolder serviceLocatorHolder = (ServiceLocatorHolder) ResourceHolderManager
			.getResourceHolder(HolderType.SERVICE_LOCATOR_TYPE);
	
	private UserProfile profile;
	private TnContext tc;
	
	@Before
	public void init()
	{
		profile=new UserProfile();
		profile.setProgramCode("ATTNAVPROG");
		profile.setPlatform("IPHONE");
		profile.setVersion("7.0.01");
		profile.setProduct("ATT_NAV");
		profile.setDevice("9800");
		
		tc=new TnContext();
	}
	
	
	@Test
	public void createObject()
	{
		ResourceContent rc=serviceLocatorHolder.createObject("", profile, tc);
		ServiceMapping sm=(ServiceMapping)rc.getObject();
		
		Assert.assertNotNull(sm);
		Assert.assertEquals("1.0.01", sm.getActionVersion());
		Assert.assertEquals("1.0.03", sm.getVersion());
		
		
		//cserver servict item part
		ServiceItem resourceItem = getServiceItemByType("resource", sm);
		Assert.assertNotNull(resourceItem);
		Assert.assertEquals("/resource-cserver/telenav-server-pb", resourceItem.getServiceDomainName());
		Assert.assertEquals("http://s-tn60-rim-resource.telenav.com:8080", resourceItem.getUrlMap().get("resource.http"));
		Assert.assertEquals(12, resourceItem.getActions().size());
		
		
		
		ServiceItem flowTypeItem = getServiceItemByType("flowtype_purchase_url", sm);
		Assert.assertNotNull(flowTypeItem);
		Assert.assertEquals("", flowTypeItem.getServiceDomainName());
		Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080/login_startup_service/html/upsell.do", flowTypeItem.getUrlMap().get("urlforpurchase.http"));
		Assert.assertEquals(0, flowTypeItem.getActions().size());
		
		
		//browser service item part
		ServiceItem browserLoginItem = getServiceItemByType("browser_login", sm);
		Assert.assertNotNull(browserLoginItem);
		Assert.assertEquals("", browserLoginItem.getServiceDomainName());
		Assert.assertEquals("/html/upsell.do", browserLoginItem.getUrlMap().get("urlforpurchase.http"));
		Assert.assertEquals("/html/startup.do", browserLoginItem.getUrlMap().get("urlforflowtype.http"));
		Assert.assertEquals(0, browserLoginItem.getActions().size());
		if(browserLoginItem instanceof BrowserServiceItem)
			Assert.assertEquals("http://s-tn60-rim-login.telenav.com:8080/login_startup_service/", ((BrowserServiceItem)browserLoginItem).getBaseUrl());
		
	}
	
	
	protected ServiceItem getServiceItemByType(String type, ServiceMapping sm)
	{
		for(ServiceItem item:sm.getServiceMapping())
		{
			if(item.getType().equals(type))
				return item;
		}
		return null;
	}
	
}
