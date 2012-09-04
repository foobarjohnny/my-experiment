package com.telenav.cserver.browser.framework.protocol;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import static org.junit.Assert.*;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.tnbrowser.util.DataHandler;

public class TestBrowserProtocolResponseFormatter extends BrowserProtocolResponseFormatter{

	@Test
	public void testFormat() throws ExecutorException
	{
		Map<String, String> clientInfo = new HashMap<String, String>();
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
		IMocksControl control = EasyMock.createControl();
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(clientInfo);
		EasyMock.replay(request);
		TestBrowserProtocolResponseFormatter tprf = new TestBrowserProtocolResponseFormatter();
		ExecutorResponse response[] = new ExecutorResponse[2];
		response[0] = new ExecutorResponse(); 
		tprf.format(request, response);
		assertNotNull(response);
		assertNotNull(response[0]);
		Map<String, String> clientInfo1 = (Map<String, String>)response[0].getAttribute("CLIENT_INFO1");
		assertNotNull(clientInfo1);
		assertEquals("ATT", clientInfo.get("carrier"));
		
	}
	
	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse responses) throws Exception {
		Map<String, String> clientInfo = (Map<String, String>)httpRequest.getAttribute("CLIENT_INFO");
		responses.setAttribute("CLIENT_INFO1", clientInfo);
	}
	
	

}
