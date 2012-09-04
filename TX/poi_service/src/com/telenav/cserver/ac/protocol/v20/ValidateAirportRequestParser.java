package com.telenav.cserver.ac.protocol.v20;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.ac.executor.v20.ValidateAirportRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author chbzhang
 * 2009-07-09
 * copy and update by xfliu at 2011/12/6
 *
 */

public class ValidateAirportRequestParser  extends BrowserProtocolRequestParser {

    @Override
    public String getExecutorType() {
        return "ValidateAirport20";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) object;
        ValidateAirportRequest request = new ValidateAirportRequest();

        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        TxNode body = handler.getAJAXBody();


        String airportName =  body.msgAt(0);
        String region = handler.getClientInfo(DataHandler.KEY_REGION);
        request.setAirportName(airportName);
        request.setRegion(region);

        return request;
    }
}
