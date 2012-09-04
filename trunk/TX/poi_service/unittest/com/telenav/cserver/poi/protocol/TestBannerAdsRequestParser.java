package com.telenav.cserver.poi.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestBannerAdsRequestParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock
				.expect(
						httpRequest
								.getAttribute(BrowserFrameworkConstants.CLIENT_INFO))
				.andReturn(handler).anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(getBodyTxNode())
				.anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_WIDTH))
				.andReturn("480").anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_HEIGHT))
				.andReturn("800").anyTimes();
		EasyMock
		.expect(httpRequest.getHeader("x-forwarded-for"))
		.andReturn("x-forwarded-for").anyTimes();

		BannerAdsRequestParser requestParser = new BannerAdsRequestParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TxNode getBodyTxNode() {
		TxNode node = new TxNode();
		JSONObject jo = new JSONObject();
		try {
			jo.put("pageId", "1");
			jo.put("pageIndex", "1");
			jo.put("keyWord", "KFC");
			jo.put("catId", "-1");
			jo.put("searchUID", "1");
			jo.put("searchLocation", TestUtil.getLocationJSON());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		node.addMsg(jo.toString());
		return node;
	}
}
