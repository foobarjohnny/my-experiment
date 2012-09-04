package com.telenav.cserver.browser.datatype;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;

import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class TestMessageWrap extends TestCase {
	
    private TnContext tnContext;
    private DataHandler handler;
    
	private Map<String, String> messages;
    
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
        
		messages = new HashMap<String, String>();
		messages.put("common.driveto", "drive to");
    }
    
	public void testMessageWrap()
	{	

		MessageWrap mw = new MessageWrap(messages, true);
		assertTrue(mw.isReadFromServer());
		assertEquals(mw.get("common.driveto"),"drive to");
	}
	
	public void testGet()
	{
		MessageWrap mw = new MessageWrap(messages, true);
		assertEquals(mw.get("common.driveto"),"drive to");
	}
	
	public void testGetWhenFalse()
	{
		MessageWrap mw = new MessageWrap(messages, false);
		assertEquals("$(common.driveto)" ,mw.get("common.driveto"));
	}
	
	public void testGetValueIsNull()
	{
		MessageWrap mw = new MessageWrap(messages, true);
		assertEquals("common.driveto1", mw.get("common.driveto1"));
	}
	
	
	public void testGetValue()
	{
		MessageWrap mw = new MessageWrap(messages, true);
		assertEquals("drive to", mw.get("common.driveto"));
		assertEquals("drive to", mw.getValue("common.driveto"));
		assertEquals("drive to1", mw.getValue("drive to1"));
		assertEquals("drive to", mw.getValue("drive to2", "drive to"));
	}
	
	public void testGetValueNull()
	{
		MessageWrap mw = new MessageWrap(messages, true);
		assertEquals("common.driveto1", mw.get("common.driveto1"));
	}
	
	public void testReadFromServer()
	{
		MessageWrap mw1 = new MessageWrap(messages, true);
		mw1.setReadFromServer(true);
		assertTrue(mw1.isReadFromServer());
	}

}
