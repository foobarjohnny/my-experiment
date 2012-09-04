package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestPOIFeedbackSaveRequestParser extends TestRequestParser {
	public void testParseBrowserRequest() {
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock
				.expect(
						httpRequest
								.getAttribute(BrowserFrameworkConstants.CLIENT_INFO))
				.andReturn(handler).anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(getBodyTxNode())
				.anyTimes();
		POIFeedbackSaveRequestParser requestParser = new POIFeedbackSaveRequestParser();
		PowerMock.replayAll();
		try {
			requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testParseBrowserRequestForPOIDetailFeedback() {
		HttpServletRequest httpRequest = PowerMock
				.createMock(HttpServletRequest.class);
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		EasyMock
				.expect(
						httpRequest
								.getAttribute(BrowserFrameworkConstants.CLIENT_INFO))
				.andReturn(handler).anyTimes();
		EasyMock.expect(handler.getAJAXBody()).andReturn(
				getBodyTxNodeForPOIDetailFeedback()).anyTimes();
		POIFeedbackSaveRequestParser requestParser = new POIFeedbackSaveRequestParser();
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
		node.addValue(1);
		node.addMsg("POIListFeedback");
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		return node;
	}

	private TxNode getBodyTxNodeForPOIDetailFeedback() {
		TxNode node = new TxNode();
		node.addValue(1);
		node.addMsg("POIDetailFeedback");
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		return node;
	}

}
