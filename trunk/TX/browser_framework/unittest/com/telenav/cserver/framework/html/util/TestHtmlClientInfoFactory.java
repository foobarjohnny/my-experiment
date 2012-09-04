package com.telenav.cserver.framework.html.util;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;

import junit.framework.TestCase;

public class TestHtmlClientInfoFactory extends TestCase {
	
    private HtmlClientInfo clientInfo;
    private String ssoToken;
	
    protected void setUp() throws Exception
    {
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
		//set supported device
		//
		ssoToken = "AAAAAACW/uwAAAEx+YKk8n/ujtHKnFRLws+vEBTqnYY=";
    }
    
    public void testGetInstance()
    {
    	HtmlClientInfoFactory hcif = HtmlClientInfoFactory.getInstance();
    	assertNotNull(hcif);
    }
    
    public void testBuildClientInfoString()
    {
    	HtmlClientInfoFactory hcif = HtmlClientInfoFactory.getInstance();
    	String expected = "{\"locale\":\"en_US\",\"platform\":\"ANDROID\",\"version\":\"7.1.0\",\"programCode\":\"ATTNAVPROG\",\"productType\":\"ATT_NAV\"}&width=460&height=760";
    	assertEquals(expected, hcif.buildClientInfoString(clientInfo));
    }
    
    public void testBuild()
    {
    	HtmlClientInfoFactory hcif = HtmlClientInfoFactory.getInstance();
    	HtmlClientInfo clientInfo = new HtmlClientInfo();
    	clientInfo = hcif.build("{\"locale\":\"en_US\",\"platform\":\"ANDROID\",\"version\":\"7.1.0\",\"programCode\":\"ATTNAVPROG\",\"productType\":\"ATT_NAV\"}&width=460&height=760", "480", "320","");
    	String expected = "\ncarrier:ATTNAVPROG\n";
    	expected += "programCode:ATTNAVPROG\n";
    	expected += "platform:ANDROID\n";
    	expected += "version:7.1.0\n";
    	expected += "product:ATT_NAV\n";
    	expected += "device:\n";
    	expected += "locale:en_US\n";
    	expected += "width:480\n";
    	expected += "height:320\n";
    	expected += "region:US";
    	
    	assertEquals(expected, clientInfo.toString());
    	
    	clientInfo = hcif.build("", "320", "480","");
    	expected = "";
    	expected = "\ncarrier:SprintPCS\n";
    	expected += "programCode:SNNAVPROG\n";
    	expected += "platform:ANDROID\n";
    	expected += "version:7.1.0\n";
    	expected += "product:SN_prem\n";
    	expected += "device:genericTest\n";
    	expected += "locale:en_US\n";
    	expected += "width:480\n";
    	expected += "height:800\n";
    	expected += "region:US";
    	
    	assertEquals(expected, clientInfo.toString());
    	
    }

}
