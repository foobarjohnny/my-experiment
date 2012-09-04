package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.executor.HtmlAboutRequest;

public class TestHtmlAboutRequestParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		HtmlClientInfo clientInfo = TestUtil.getHtmlClientInfo(); 
		EasyMock.expect((HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO)).andReturn(clientInfo).anyTimes();
		EasyMock.expect(httpRequest.getParameter("ssoToken")).andReturn("");
		HtmlAboutRequestParser requestParser = new HtmlAboutRequestParser();
		PowerMock.replayAll();
		
		try {
			HtmlAboutRequest aboutRequest = (HtmlAboutRequest) requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			assertEquals("The two Object should be same.",getAboutRequest().toString(), aboutRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HtmlAboutRequest getAboutRequest(){
		HtmlAboutRequest request = new HtmlAboutRequest();
		request.setClientInfo(TestUtil.getHtmlClientInfo());
		request.setSsoToken("");
        return request;
	}
}
