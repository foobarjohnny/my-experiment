package com.telenav.cserver.framework.html.datatype;

import junit.framework.TestCase;

public class TestHtmlDeviceScreenSize extends TestCase {
	
	HtmlDeviceScreenSize hdss = null;
	
	public void setUp()
	{
		hdss =  new HtmlDeviceScreenSize();
		hdss.setWidth(480);
		hdss.setHeight(320);
	}
	
	
	public void testSetDeviceScreenSize()
	{
		hdss.setDeviceName("320x240_240x320");
		hdss.setDeviceScreenSize();
		assertEquals(320, hdss.getHeight());
		assertEquals(240, hdss.getWidth());
	}
	
	public void testCompareTo()
	{
		HtmlDeviceScreenSize o2 = new HtmlDeviceScreenSize();
		o2.setDeviceName("1024x600_600x1024");
		o2.setWidth(1024);
		o2.setHeight(600);
		assertEquals(-1,hdss.compareTo(o2));
		
		o2.setDeviceName("176x220_220x176");
		o2.setWidth(220);
		o2.setHeight(176);
		assertEquals(1, hdss.compareTo(o2));
		
		o2.setDeviceName("330x340_340x330");
		o2.setWidth(330);
		o2.setHeight(340);
		assertEquals(0, hdss.compareTo(o2));
	}
}
