package com.telenav.cserver.framework.html.util;

import junit.framework.TestCase;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;

public class TestHtmlSpecialDeviceHelper extends TestCase{
	
	public void testHtmlSpecialDeviceHelper()
	{
		HtmlClientInfo clientInfo;
		
        clientInfo = new HtmlClientInfo();
        clientInfo.setProgramCode("ATTNAVPROG");
		clientInfo.setCarrier("ATT");
		clientInfo.setPlatform("ANDROID");
		clientInfo.setVersion("7.1.0");
		clientInfo.setProduct("ATT_NAV");
		clientInfo.setDevice("genericTest");
		clientInfo.setWidth("460");
		clientInfo.setHeight("760");
		clientInfo.setBuildNo("1010");
		clientInfo.setLocale("en_US");
		
		HtmlSpecialDeviceHelper hpdh = HtmlSpecialDeviceHelper.getInstance();
		assertNotNull(hpdh);
		assertTrue(HtmlSpecialDeviceHelper.isSpecialDevice(clientInfo, "specialDevice"));
	}

}
