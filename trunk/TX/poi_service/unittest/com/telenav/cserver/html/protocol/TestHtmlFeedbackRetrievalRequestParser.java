package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;

public class TestHtmlFeedbackRetrievalRequestParser extends TestRequestParser{
	public void testParseBrowserRequest(){
		HtmlClientInfo clientInfo = TestUtil.getHtmlClientInfo(); 
		EasyMock.expect((HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO)).andReturn(clientInfo).anyTimes();
		EasyMock.expect(httpRequest.getParameter("jsp")).andReturn("");
		PowerMock.replayAll();
		HtmlFeedbackRetrievalRequestParser requestParser = new HtmlFeedbackRetrievalRequestParser();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
