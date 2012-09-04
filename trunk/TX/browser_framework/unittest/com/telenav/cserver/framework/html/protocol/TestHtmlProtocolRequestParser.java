package com.telenav.cserver.framework.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.mock.MockHttpServletRequest;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import static org.junit.Assert.*;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;


public class TestHtmlProtocolRequestParser extends HtmlProtocolRequestParser{
	
	HtmlClientInfo clientInfo;
	IMocksControl control;
	String ssoToken;
	public  ExecutorRequest parseBrowserRequest(
            HttpServletRequest request) throws Exception
            {
				return new ExecutorRequest();
		
            }
	public String getExecutorType()
	{
		return "Login";
	}
	
    
    public void setUp() throws Exception
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
		ssoToken = "AAAAAACW/uwAAAEx+YKk8n/ujtHKnFRLws+vEBTqnYY=";
	}
    
    @Test
    public void testParse() throws Exception
    {
    	this.setUp();
    	control = EasyMock.createControl();
    	HttpServletRequest request = control.createMock(HttpServletRequest.class);
    	EasyMock.expect(request.getAttribute("HTML_CLIENT_INFO")).andReturn(clientInfo).anyTimes();
    	EasyMock.expect(request.getParameter("ssoToken")).andReturn(ssoToken).anyTimes();
    	EasyMock.replay(request);
    	TestHtmlProtocolRequestParser thprp = new TestHtmlProtocolRequestParser();
    	ExecutorRequest[] browserRequest = thprp.parse(request);
    	assertNotNull(browserRequest);
		assertNotNull(browserRequest[0].getUserProfile());
		assertEquals("ATT", browserRequest[0].getUserProfile().getCarrier());
		assertEquals("Login",browserRequest[0].getExecutorType());
    }
    
	@Test
	public void testGetStringParm()
	{
		control = EasyMock.createControl();
    	HttpServletRequest request = new MockHttpServletRequest();
    	DataHandler handler = new DataHandler(request);
    	TxNode node = new TxNode();
        node.addMsg("value");
        handler.setParameter("TestKey", node);
        TxNode node1 = new TxNode();
        handler.setParameter("TestKey2", node1);
    	TestHtmlProtocolRequestParser htpbr = new TestHtmlProtocolRequestParser();
		String value = htpbr.getStringParm(handler, "TestKey");
		assertEquals("value", value);
		value = htpbr.getStringParm(handler, "TestKey1");
		assertEquals("", value);
		value = htpbr.getStringParm(handler, "TestKey2");
		assertEquals("", value);
	}
    
}
