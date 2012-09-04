/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import junit.framework.TestCase;

/**
 * TestDeviceCarrierMapping.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
public class TestDeviceCarrierMapping extends TestCase{
	
	DeviceCarrierMapping deviceCarrierMapping = new DeviceCarrierMapping();
	
	public void testGetCarrier(){
		assertEquals("ATT",DeviceCarrierMapping.getCarrier("a", null));
		assertEquals("ATT",DeviceCarrierMapping.getCarrier("a", ""));
		assertEquals("TTAA",DeviceCarrierMapping.getCarrier("b", "TTAA"));
	}
	
	public void testGetCarrier1(){
		assertEquals("ATT",DeviceCarrierMapping.getCarrier("a"));
	}

}
