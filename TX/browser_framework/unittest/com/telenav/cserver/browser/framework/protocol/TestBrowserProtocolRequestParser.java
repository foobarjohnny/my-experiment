package com.telenav.cserver.browser.framework.protocol;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

public class TestBrowserProtocolRequestParser extends BrowserProtocolRequestParser{

    private TnContext tnContext;
    private static DataHandler handler;
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    
	private Map<String, String> messages;
	IMocksControl control;
    
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
        
		messages = new HashMap<String, String>();
		messages.put("common.driveto", "drive to");
		
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
        TxNode node = new TxNode();
        node.addMsg("value");
        handler.setParameter("TestKey", node);
        TxNode node1 = new TxNode();
        handler.setParameter("TestKey2", node1);
	}
	
	public  ExecutorRequest parseBrowserRequest(
            HttpServletRequest request) throws Exception
            {
				return new ExecutorRequest();
		
            }
	public String getExecutorType()
	{
		return "Login";
	}
	
	@Test
	public void testParse() throws Exception
	{
		TestBrowserProtocolRequestParser tpbr = new TestBrowserProtocolRequestParser();
		tpbr.setUp();
		
		control = EasyMock.createControl();
    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
		
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
    	
        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
    	EasyMock.replay(request);
    	
    	control = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = control.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		control = EasyMock.createControl();
		ExecutorRequest[] browserRequest = tpbr.parse(request);
		assertNotNull(browserRequest[0].getUserProfile());
		assertEquals("ATT", browserRequest[0].getUserProfile().getCarrier());
		assertEquals("Login",browserRequest[0].getExecutorType());
	}
	
	@Test
	public void testGetStringParm()
	{
		TestBrowserProtocolRequestParser tpbr = new TestBrowserProtocolRequestParser();
		String value = tpbr.getStringParm(handler, "TestKey");
		assertEquals("value", value);
		value = tpbr.getStringParm(handler, "TestKey1");
		assertEquals("", value);
		value = tpbr.getStringParm(handler, "TestKey2");
		assertEquals("", value);
	}
}
