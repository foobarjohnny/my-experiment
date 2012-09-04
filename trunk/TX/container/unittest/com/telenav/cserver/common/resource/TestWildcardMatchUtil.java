/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.common.resource.orders.AccountTypeLoadOrder;
import com.telenav.cserver.common.resource.orders.CarrierLoadOrder;
import com.telenav.cserver.common.resource.orders.DeviceLoadOrder;
import com.telenav.cserver.common.resource.orders.PlatformLoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestWildcardMatchUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
public class TestWildcardMatchUtil extends TestCase{
	private LoadOrders wildcardOrders = new LoadOrders();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private TnContext tnContext = UnittestUtil.createTnContext();
	private List<MatchObject> list = new ArrayList<MatchObject>();
	
	private LoadOrder accountTypeorder = new AccountTypeLoadOrder();
	private LoadOrder carrierLoadOrder = new CarrierLoadOrder();
	private LoadOrder deviceLoadOrder = new DeviceLoadOrder();
	private LoadOrder platformLoadOrder = new PlatformLoadOrder();
	
	private MatchObject mo0 = new MatchObject();
	private MatchObject mo1 = new MatchObject();
	private MatchObject mo2 = new MatchObject();
	@Override
	protected void setUp() throws Exception {
		//set orders of wildcardOrders
		wildcardOrders.addOrder(accountTypeorder);
		wildcardOrders.addOrder(carrierLoadOrder);
		wildcardOrders.addOrder(deviceLoadOrder);
		wildcardOrders.addOrder(platformLoadOrder);
		//set the properties of MatchObject
		String keyString0[] = new String[]{"NORMAL","C*","?000","ANDROID"};
		String value0 = "JIM";
		mo0.keyStrings = keyString0;
		mo0.value = value0;
		
		String keyString1[] = new String[]{"NORMAL","C*","?000","ANDROID"};
		String value1 = "CHINA";
		mo1.keyStrings = keyString1;
		mo1.value = value1;
		
		String keyString2[] = new String[]{"NORMAL","C*","?000","ANDROID"};
		String value2 = "APPLE";
		mo2.keyStrings = keyString2;
		mo2.value = value2;
		//add them to list
		list.add(mo0);
		list.add(mo1);
		list.add(mo2);
	}
	
	public void testMatchString(){
		assertTrue(WildcardMatchUtil.matchString("5.2.01-free", "5.2.*"));
		assertTrue(WildcardMatchUtil.matchString("5.2.01-free", "*-FREE"));
		assertTrue(WildcardMatchUtil.matchString("5.2.01-free", "*"));
		assertTrue(WildcardMatchUtil.matchString("5.2.01-free", "5.2.01-FREE"));
		assertTrue(WildcardMatchUtil.matchString("5.2.01-free", "5.2.?1-FREE"));
		assertTrue(WildcardMatchUtil.matchString(null, ""));
		
		
		assertFalse(WildcardMatchUtil.matchString("5.2.01-free", "*0FREE"));
		assertFalse(WildcardMatchUtil.matchString("5.2.01-free", "5.2..*"));
		assertFalse(WildcardMatchUtil.matchString("5.2.01-free", "5.2.?-FREE"));
		assertFalse(WildcardMatchUtil.matchString("5.2.01-free", "123"));
		assertFalse(WildcardMatchUtil.matchString(null, "123"));
	}
	
	public void testMatch(){
		//define variable
		String result;
		//assert null situation
		assertNull(WildcardMatchUtil.match(null, null, null, null));
		assertNull(WildcardMatchUtil.match(null, userProfile, null, list));
		assertNull(WildcardMatchUtil.match(wildcardOrders, null, null, list));
		assertNull(WildcardMatchUtil.match(wildcardOrders, userProfile, null, null));
		//go into the outer loop
		result = WildcardMatchUtil.match(wildcardOrders, userProfile, tnContext, list);
		assertEquals("JIM",result);
		
		//should return CHINA
		mo0.keyStrings[0] = "noMactch";
		result = WildcardMatchUtil.match(wildcardOrders, userProfile, tnContext, list);
		assertEquals("CHINA",result);
		
		//should return APPLE
		mo1.keyStrings[0] = "noMactch";
		result = WildcardMatchUtil.match(wildcardOrders, userProfile, tnContext, list);
		assertEquals("APPLE",result);
	}
}
