/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.executor.GetTableDetailResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.datatypes.restaurant.v10.RestaurantAvailOffer;
import com.telenav.datatypes.restaurant.v10.TableOffer;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class GetTableDetailResponseFormatter extends
		BrowserProtocolResponseFormatter {

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {

		GetTableDetailResponse rResponse = (GetTableDetailResponse) executorResponse;
		RestaurantAvailOffer rAvailOffer = rResponse.getAvailOffer();
		JSONObject jAvailOffer = new JSONObject();
		if (rAvailOffer != null) {
			jAvailOffer = new JSONObject();
			jAvailOffer.put(HtmlConstants.RRKey.SEARCH_OFFER_TIME1, "");
			jAvailOffer.put(HtmlConstants.RRKey.SEARCH_OFFER_TIME2, "");
			jAvailOffer.put(HtmlConstants.RRKey.SEARCH_OFFER_TIME3, "");

			TableOffer[] tbOffers = rAvailOffer.getTableOffers();

			int i = 1;
			for (TableOffer tbOffer : tbOffers) {
				Date returnTime = tbOffer.getDateTime().getTime();
				HtmlPoiUtil.safeJSONPutString(jAvailOffer,
						HtmlConstants.RRKey.SEARCH_OFFER_TIME + i,
						HtmlConstants.SHORT_TIME_FORMAT.format(returnTime));
				i++;
			}

		} else {

		}

		httpRequest.setAttribute("ajaxResponse", jAvailOffer.toString());

	}

}
