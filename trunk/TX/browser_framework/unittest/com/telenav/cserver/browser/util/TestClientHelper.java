package com.telenav.cserver.browser.util;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.TestCase;

public class TestClientHelper extends TestCase{
    private DataHandler handler;
    HttpServletRequest request = new MockHttpServletRequest();
    
    protected void setUp() throws Exception
    {
    	handler = new DataHandler(request);
    	
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
        clientInfo.put(DataHandler.KEY_CARRIER, "MMI");
        clientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
        clientInfo.put(DataHandler.KEY_VERSION, "6_4_01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "320");
        clientInfo.put(DataHandler.KEY_HEIGHT, "240");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "320");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "240");   
        handler.setClientInfo(clientInfo);
    	
	}
    
    public void testGetModuleNameForPoi() throws Exception{
    	String moduleName = ClientHelper.getModuleNameForPoi(handler);
    	assertEquals("touch64",moduleName);
    }
    
	public void testGetLayoutKeyWithProduct() {
		String key = ClientHelper.getLayoutKeyWithProduct(handler, null);
		assertEquals("MMI.RIM.6_4_01.TN", key);
	}
}
