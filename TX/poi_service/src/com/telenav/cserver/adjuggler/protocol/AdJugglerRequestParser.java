package com.telenav.cserver.adjuggler.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.adjuggler.executor.AdJugglerRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class AdJugglerRequestParser extends BrowserProtocolRequestParser {
	@Override
	public String getExecutorType() {
		return "AdJuggler";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
			throws Exception {
		AdJugglerRequest request = new AdJugglerRequest();
		HttpServletRequest httpRequest = (HttpServletRequest) object;
		DataHandler handler = (DataHandler) httpRequest
				.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		TxNode body = handler.getAJAXBody();
		String joString = body.msgAt(0);
		JSONObject jo = new JSONObject(joString);
		request.setActionJO(jo);
		request.setHandler(handler);
		
		String imageKey = (String)httpRequest.getAttribute(BrowserFrameworkConstants.IMAGE_KEY);
		request.setImageKey(imageKey);
		
		return request;
	}
}
