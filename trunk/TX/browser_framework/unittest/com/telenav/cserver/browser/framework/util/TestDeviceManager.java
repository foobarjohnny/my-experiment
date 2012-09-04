/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.browser.framework.util;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.struts.mock.MockHttpServletRequest;

import junit.framework.TestCase;

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.util.DeviceManager;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.webapp.taglib.TnLayoutTag;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestDeviceManager extends TestCase
{
    private TnContext tnContext;
    private DataHandler handler;

    @Override
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
        
        HttpServletRequest request = new MockHttpServletRequest();
        handler = new DataHandler(request);
        
        Hashtable clientInfo = new Hashtable();
        clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
        clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_HEIGHT, "800");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-800");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800-480");   
        handler.setClientInfo(clientInfo);
    }

    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testResourceConfig()
    {
    	//test simple movie list
    	DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(handler);
    	System.out.println(config.toString());
    }
    
    public void testLayoutTag()
    {
    	DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(handler);
    	String layout = config.getLayoutKeyWithDevice();
    	//test simple movie list
    	TnLayoutTag tag = new TnLayoutTag();
    	String filePath = "device." + layout + ".480x800";
    	String layoutFile = "StartUp";
    	tag.generateWholeLayout("480","800",filePath,layoutFile,"common","");
    	
    	System.out.println(tag.outputText.toString());
    }
    
    public void testLayoutTagWithCommonFile()
    {
    	DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(handler);
    	String layout = config.getLayoutKeyWithDevice();
    	//test simple movie list
    	TnLayoutTag tag = new TnLayoutTag();
    	String filePath = "device." + layout + ".480x800";
    	String filePathWithCommonLayout = "device." + "Common." + layout + ".480x800";
    	String layoutFile = "StartUp";
    	tag.generateWholeLayout("480","800",filePath,layoutFile,"common",filePathWithCommonLayout);
    	
    	System.out.println(tag.outputText.toString());
    }
}
