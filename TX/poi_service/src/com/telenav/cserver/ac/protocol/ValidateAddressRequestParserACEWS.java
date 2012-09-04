package com.telenav.cserver.ac.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.ValidateAddressRequestACEWS;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.util.JsonUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author chbzhang
 * 2009-07-09
 *
 */

public class ValidateAddressRequestParserACEWS  extends BrowserProtocolRequestParser {

    @Override
    public String getExecutorType() {
        return "ValidateAddress";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) object;
        ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();

        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        TxNode body = handler.getAJAXBody();

        String jsonStr = body.msgAt(0);
        JSONObject jo = new JSONObject(jsonStr);
        
        String street1 = JsonUtil.getString(jo, "street1");
        String street2 = JsonUtil.getString(jo, "street2");
        String firstLine = JsonUtil.getString(jo, "firstLine");
        String lastLine = JsonUtil.getString(jo, "lastLine");
        String country = JsonUtil.getString(jo, "country");
        String label = JsonUtil.getString(jo, "label");
        boolean maitai = JsonUtil.getBoolean(jo, "maitai");

        // TODO MAPPING XML for country AC TEMPLATE
        if ("USA".equalsIgnoreCase(country)) {
            country = "US";
        }
        if ("CAN".equalsIgnoreCase(country)) {
            country = "CA";
        }
        if ("MEX".equalsIgnoreCase(country)) {
            country = "MX";
        }
        
        if ("CHN".equalsIgnoreCase(country)) {
            country = "CN";
        }

        request.setStreet1(street1);
        request.setStreet2(street2);
        request.setFirstLine(firstLine);
        request.setLastLine(lastLine);
        request.setCountry(country);
        request.setLabel(label);
        request.setMaitai(maitai);

        return request;
    }
}
