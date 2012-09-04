package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.html.executor.HtmlAdsRequest;
import com.telenav.cserver.html.util.HtmlConstants;

public class TestHtmlAdsParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		try {
			HtmlAdsParser requestParser = new HtmlAdsParser();
			EasyMock.expect(httpRequest.getParameter("isDummy")).andReturn(
					"true");
			EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(
					"");
			EasyMock.expect(httpRequest.getParameter("adsId")).andReturn("");
			EasyMock.expect(httpRequest.getParameter("height")).andReturn("");
			EasyMock.expect(httpRequest.getParameter("width")).andReturn("");
			PowerMock.replayAll();

			HtmlAdsRequest request = (HtmlAdsRequest) requestParser
					.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			// assertEquals("The two Object should be same.",
			// getHtmlAdsRequestForDummy().toString(), request.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testParseBrowserRequestForFetchDetailData() {
		try {
			HttpServletRequest httpRequest = PowerMock
					.createMock(HttpServletRequest.class);
			HtmlAdsParser requestParser = new HtmlAdsParser();
			EasyMock.expect(httpRequest.getParameter("isDummy")).andReturn(
					"true");
			EasyMock.expect(httpRequest.getParameter("operateType")).andReturn(
					HtmlConstants.OPERATE_ADSVIEW_FETCH_DETAIL_DATA);
			EasyMock.expect(httpRequest.getParameter("adsId")).andReturn("");
			EasyMock.expect(httpRequest.getParameter("logoHeight")).andReturn(
					"");
			EasyMock.expect(httpRequest.getParameter("logoWidth"))
					.andReturn("");
			PowerMock.replayAll();

			HtmlAdsRequest request = (HtmlAdsRequest) requestParser
					.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			// assertEquals("The two Object should be same.",
			// getHtmlAdsRequestForDummy().toString(), request.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private HtmlAdsRequest getHtmlAdsRequestForDummy() {
	// HtmlAdsRequest request = new HtmlAdsRequest();
	// request.setDummy(true);
	// request.setOperateType(HtmlConstants.OPERATE_ADSVIEW_BASIC);
	// request.setAdId(0L);
	// return request;
	// }
}
