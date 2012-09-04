package com.telenav.cserver.ac.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author chbzhang
 * 2009-07-09
 *
 */

public class ValidateAirportRequestParser  extends BrowserProtocolRequestParser {

    @Override
    public String getExecutorType() {
        return "ValidateAirport";
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
