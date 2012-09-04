package com.telenav.cserver.browser.util;

import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.tnbrowser.util.IFeatureManager;

import junit.framework.TestCase;

public class TestFeatureManager extends TestCase {
	
    private TnContext tnContext;
    private DataHandler handler;
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FeatureManager manager = new FeatureManager();
	
    protected void setUp() throws Exception
    {
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "genericTest");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "ANDROID");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.2.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
    	handler = new DataHandler(request);
    	
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
        clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
        clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        clientInfo.put(DataHandler.KEY_VERSION, "6_2_01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_HEIGHT, "800");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-800");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800-480");   
        handler.setClientInfo(clientInfo);
    	
    	request.setAttribute("CLIENT_INFO", handler);
	}
	
	public void testGetManager()
	{
		String params[] = {DataHandler.KEY_CARRIER,DataHandler.KEY_PLATFORM,DataHandler.KEY_VERSION};
		FeatureManager manager = new FeatureManager();
		manager = (FeatureManager)FeatureManager.getManager(handler,params);
		assertTrue(!manager.isEnabled("Login.Convenience"));
		
	}
	
	public void testGetManagerAllFeatures()
	{
		String params[] = {DataHandler.KEY_CARRIER,DataHandler.KEY_VERSION};
		FeatureManager manager = new FeatureManager();
		manager = (FeatureManager)FeatureManager.getManager(handler,params);
		assertTrue(manager.isEnabled("Login.Convenience"));
		
	}

}
