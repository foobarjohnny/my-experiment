package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;

public class POIFeedbackSaveResponseFormatter extends BrowserProtocolResponseFormatter
{
    
	public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception 
	{
		
        TxNode node = new TxNode();
		if (executorResponse.getStatus() == ExecutorResponse.STATUS_OK){
	        node.addValue(ExecutorResponse.STATUS_OK);
		}else{
	        node.addValue(ExecutorResponse.STATUS_FAIL);
		}
        httpRequest.setAttribute("node", node);
    }
}
