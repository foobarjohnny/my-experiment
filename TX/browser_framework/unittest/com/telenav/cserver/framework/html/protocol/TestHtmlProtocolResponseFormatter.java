package com.telenav.cserver.framework.html.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;

import com.telenav.cserver.browser.framework.protocol.TestBrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.tnbrowser.util.DataHandler;

public class TestHtmlProtocolResponseFormatter extends HtmlProtocolResponseFormatter{

	@Test
	public void testFormat() throws ExecutorException
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
		
		IMocksControl control = EasyMock.createControl();
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getAttribute("HTML_CLIENT_INFO")).andReturn(clientInfo);
		EasyMock.replay(request);
		TestHtmlProtocolResponseFormatter tprf = new TestHtmlProtocolResponseFormatter();
		ExecutorResponse response[] = new ExecutorResponse[2];
		response[0] = new ExecutorResponse(); 
		tprf.format(request, response);
		assertNotNull(response);
		assertNotNull(response[0]);
		HtmlClientInfo clientInfo1 = (HtmlClientInfo)response[0].getAttribute("HTML_CLIENT_INFO1");
		assertNotNull(clientInfo1);
		assertEquals("ATT", clientInfo1.getCarrier());
		
	}
	
	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse responses) throws Exception {
		HtmlClientInfo clientInfo = (HtmlClientInfo)httpRequest.getAttribute("HTML_CLIENT_INFO");
		responses.setAttribute("HTML_CLIENT_INFO1", clientInfo);
	}

}
