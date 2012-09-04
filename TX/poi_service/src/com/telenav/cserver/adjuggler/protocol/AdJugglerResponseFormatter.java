package com.telenav.cserver.adjuggler.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.adjuggler.executor.AdJugglerResponse;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;

public class AdJugglerResponseFormatter extends
		BrowserProtocolResponseFormatter {
	
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		AdJugglerResponse adJugglerResponse = (AdJugglerResponse) executorResponse;
		String result = adJugglerResponse.getResult();
		TxNode node = new TxNode();
		node.addMsg(result);
		httpRequest.setAttribute("node", node);
	}
}
