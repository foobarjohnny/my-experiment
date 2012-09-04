package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlNickNameParser extends TestRequestParser{
	public void testParseBrowserRequest(){
		EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(HtmlConstants.OPERATE_NICKNAME_CHECKANDUPDATE).anyTimes();
		EasyMock.expect(httpRequest.getParameter("ssoToken")).andReturn("").anyTimes();
		EasyMock.expect(httpRequest.getParameter("nickName")).andReturn("").anyTimes();
		PowerMock.replayAll();
		HtmlNickNameParser requestParser = new HtmlNickNameParser();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
