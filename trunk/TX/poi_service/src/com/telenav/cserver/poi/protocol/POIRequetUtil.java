package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class POIRequetUtil {
	public static JSONObject getJo(HttpServletRequest httpRequest)
			throws Exception {
		// Get the JSON request.
		DataHandler handler = (DataHandler) httpRequest
				.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		
		TxNode body = handler.getAJAXBody();
		
		String joString = body.msgAt(0);
		JSONObject jo = new JSONObject(joString);
		if (jo == null)
			throw new IllegalArgumentException(
					"POI Request does not contain a json object");
		return jo;

	}
}
