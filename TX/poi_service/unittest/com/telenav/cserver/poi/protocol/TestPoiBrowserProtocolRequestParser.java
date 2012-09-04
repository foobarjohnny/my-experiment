package com.telenav.cserver.poi.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestPoiBrowserProtocolRequestParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler).anyTimes();
		EasyMock.expect(handler.getClientInfo(handler.KEY_PRODUCTTYPE)).andReturn("ATT_NAV").anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(getBodyTxNode()).anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_AUDIOFORMAT)).andReturn("").anyTimes();
		
		PoiBrowserProtocolRequestParser requestParser = new PoiBrowserProtocolRequestParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TxNode getBodyTxNode(){
		TxNode node = new TxNode();
		JSONObject jo = new JSONObject();
		try {
			jo.put("categoryId", "-1");
			jo.put("inputString", "KFC");
			jo.put("from", Constant.StorageKey.POI_MODULE_FROM_TYPE);
			jo.put("searchTypeStr", "7");
			jo.put("routeID", "1");
			jo.put("segmentId", "1");
			jo.put("edgeId", "1");
			jo.put("shapePointId", "1");
			jo.put("range", "1");
			jo.put("currentLat", "1");
			jo.put("currentLon", "1");
			jo.put("searchAlongType", 1);
			jo.put("searchFromType", "1");
			jo.put("sortType", "1");
			jo.put("currentPage", "1");
			jo.put("maxResults", "9");
			jo.put("isMostPopular", "1");
			jo.put("distanceUnit", 1);
			jo.put("sponsorListingNumber", 1);
			jo.put("addressString", TestUtil.getLocationJSON().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		node.addMsg(jo.toString());
		return node;
	}
}
