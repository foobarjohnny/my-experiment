package com.telenav.cserver.about.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.about.executor.AboutFeedbackResponse;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;

public class AboutFeedbackResponseFormatter extends
		BrowserProtocolResponseFormatter {


	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
        AboutFeedbackResponse response = (AboutFeedbackResponse) executorResponse;

        TxNode node = new TxNode();

        node.addValue(response.getStatus());

        httpRequest.setAttribute("node", node);

	}

}
