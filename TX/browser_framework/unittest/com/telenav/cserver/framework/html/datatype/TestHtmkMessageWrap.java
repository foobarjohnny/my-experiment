package com.telenav.cserver.framework.html.datatype;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Test;

import com.telenav.tnbrowser.util.DataHandler;
import static org.junit.Assert.*;

public class TestHtmkMessageWrap {
	
	private Map<String, String> messages;
    private DataHandler handler;
    HttpServletRequest request = new MockHttpServletRequest();
    static HtmlMessageWrap hwm;
    
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
        
		messages = new HashMap<String, String>();
		messages.put("common.driveto", "drive to");
    }
    @Test
    public void testInitialMessage() throws Exception
    {
    	this.setUp();
    	hwm = new HtmlMessageWrap(messages);
    	assertEquals("common.driveto1", hwm.get("common.driveto1"));
    	assertEquals("drive to", hwm.get("common.driveto"));
    	assertEquals("drive to", hwm.getValue("common.driveto"));
    	assertEquals("common.driveto1", hwm.getValue("common.driveto1"));
    	assertEquals("search poi", hwm.getValue("common.driveto2", "search poi"));
    	assertEquals("drive to", hwm.getValue("common.driveto", "search poi"));
    	
    }
    
    @Test
    public void testSetMessage()
    {
    	Map<String, String> messagesNew = new HashMap<String, String>();;
    	messagesNew.put("poi.search", "search poi");
    	hwm.setMessages(messagesNew);
    	assertEquals(messagesNew, hwm.getMessages());
    	assertEquals("search poi", hwm.get("poi.search"));
    }
}
