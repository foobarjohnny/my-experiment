package com.telenav.cserver.poi.protocol;

import org.easymock.EasyMock;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestEditPOIRequestParser extends TestRequestParser{
	@SuppressWarnings("unchecked")
	public void testParseBrowserRequest() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock.expect(httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler).anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(getBodyTxNode()).anyTimes();
		EditPOIRequestParser requestParser = new EditPOIRequestParser();
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
			jo.put("poiId", "1");
			jo.put("price", "1");
			jo.put("brand", "KFC");
			jo.put("phone", "0123456789");
			jo.put("validated", 1);
			jo.put("rateIndex", 1);
			jo.put("rateReview", "1");
			jo.put("categoryId", "-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		node.addMsg(jo.toString());
		return node;
	}
}
