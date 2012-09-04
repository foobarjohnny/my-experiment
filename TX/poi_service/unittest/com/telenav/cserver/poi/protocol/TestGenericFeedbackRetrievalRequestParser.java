package com.telenav.cserver.poi.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.tnbrowser.util.DataHandler;

public class TestGenericFeedbackRetrievalRequestParser extends
		TestRequestParser {
	public void testParseBrowserRequest() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler).anyTimes();
		EasyMock.expect(httpRequest.getParameter("feedBackTopic")).andReturn("feedBackTopic").anyTimes();
		EasyMock.expect(httpRequest.getParameter("pageName")).andReturn("1").anyTimes();
		EasyMock.expect(httpRequest.getParameter("pageSize")).andReturn("1").anyTimes();
		EasyMock.expect(httpRequest.getParameter("pageNumber")).andReturn("10").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_LOCALE)).andReturn("en_US").anyTimes();
		
		GenericFeedbackRetrievalRequestParser requestParser = new GenericFeedbackRetrievalRequestParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
