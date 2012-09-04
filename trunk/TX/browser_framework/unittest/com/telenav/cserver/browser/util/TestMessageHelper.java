package com.telenav.cserver.browser.util;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Test;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;
import junit.framework.TestCase;

public class TestMessageHelper extends TestCase {
	
	MessageHelper mh = null;
	DataHandler handler = null;
	TnContext tnContext;
	
	public void setUp()
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
        
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
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
	
	public void testGetInstance()
	{
		mh = MessageHelper.getInstance();
		assertTrue(mh.isReadFromServer());
	}
	
	public void testIsReadFromServer()
	{
		mh = MessageHelper.getInstance(true);
		assertTrue(mh.isReadFromServer());
	}
	
	public void testSetReadFromServer()
	{
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(false);
		assertTrue(!mh.isReadFromServer());
	}
	
	public void testInitial()
	{
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(true);
		mh.initMessage(handler);
		assertEquals("Submit", mh.getMessageValue(handler, "common.button.Submit"));
	}

	@Test
	public void testAllMessageFound(){
		HttpServletRequest request = new MockHttpServletRequest();
        DataHandler specialHandler = new DataHandler(request);
       
        Hashtable<String, String> SpecialClientInfo = new Hashtable<String, String>();
        SpecialClientInfo.put(DataHandler.KEY_CARRIER, "Rogers");
        SpecialClientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
        SpecialClientInfo.put(DataHandler.KEY_VERSION, "6.0.01");
        SpecialClientInfo.put(DataHandler.KEY_PRODUCTTYPE, "RN");
        SpecialClientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        SpecialClientInfo.put(DataHandler.KEY_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_HEIGHT, "320");
        SpecialClientInfo.put(DataHandler.KEY_LOCALE, "es_MX");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320");   
        specialHandler.setClientInfo(SpecialClientInfo);
		
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(true);
		mh.initMessage(specialHandler);
		assertEquals("device_level", mh.getMessageValue(specialHandler, "message"));
	}
	
	@Test
	public void testAllMessageNotFound(){			
        HttpServletRequest request = new MockHttpServletRequest();
        DataHandler specialHandler = new DataHandler(request);
       
        Hashtable<String, String> SpecialClientInfo = new Hashtable<String, String>();
        SpecialClientInfo.put(DataHandler.KEY_CARRIER, "MMI");
        SpecialClientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
        SpecialClientInfo.put(DataHandler.KEY_VERSION, "6.4.01");
        SpecialClientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
        SpecialClientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        SpecialClientInfo.put(DataHandler.KEY_WIDTH, "320");
        SpecialClientInfo.put(DataHandler.KEY_HEIGHT, "240");
        SpecialClientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "320");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "240");   
        specialHandler.setClientInfo(SpecialClientInfo);
		
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(true);
		mh.initMessage(specialHandler);
		assertNull(mh.getMessageValue(specialHandler, "common.button.Submit"));
	}
	
	@Test
	public void testCarrierMessageNotFound(){		
		HttpServletRequest request = new MockHttpServletRequest();
        DataHandler specialHandler = new DataHandler(request);
       
        Hashtable<String, String> SpecialClientInfo = new Hashtable<String, String>();
        SpecialClientInfo.put(DataHandler.KEY_CARRIER, "SprintPCS");
        SpecialClientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
        SpecialClientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        SpecialClientInfo.put(DataHandler.KEY_PRODUCTTYPE, "SN");
        SpecialClientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        SpecialClientInfo.put(DataHandler.KEY_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_HEIGHT, "320");
        SpecialClientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320");   
        specialHandler.setClientInfo(SpecialClientInfo);
		
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(true);
		mh.initMessage(specialHandler);
		assertEquals("Submit", mh.getMessageValue(handler, "common.button.Submit"));
	}
	
	@Test
	public void testDeviceMessageNotFound(){
		HttpServletRequest request = new MockHttpServletRequest();
        DataHandler specialHandler = new DataHandler(request);
        
        Hashtable<String, String> SpecialClientInfo = new Hashtable<String, String>();
        SpecialClientInfo.put(DataHandler.KEY_CARRIER, "SprintPCS");
        SpecialClientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        SpecialClientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        SpecialClientInfo.put(DataHandler.KEY_PRODUCTTYPE, "SN");
        SpecialClientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        SpecialClientInfo.put(DataHandler.KEY_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_HEIGHT, "320");
        SpecialClientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480");
        SpecialClientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320");     
        specialHandler.setClientInfo(SpecialClientInfo);
		
		mh = MessageHelper.getInstance(true);
		mh.setReadFromServer(true);
		mh.initMessage(specialHandler);
		assertEquals("Submit", mh.getMessageValue(handler, "common.button.Submit"));
	}
	
	
	public void testGetMessageValue()
	{
		mh = MessageHelper.getInstance(true);
		assertEquals("What was wrong with your search for you?", mh.getMessageValue(handler, "poi.feedback.listQuestion"));
	}
	
}
