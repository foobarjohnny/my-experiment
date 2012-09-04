/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.browser.movie.Constant.RRKey;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.action.BookTicketAction;
import com.telenav.cserver.movie.html.datatypes.TicketItem;
import com.telenav.cserver.movie.html.executor.GetTicketQuantityResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.navstar.util.JSONArray;

/**
 * GetTicketQuantityResponseFormatter.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Dec 21, 2010
 * 
 */
public class GetTicketQuantityResponseFormatter extends
		HtmlProtocolResponseFormatter {
	private static Logger logger = Logger.getLogger(GetTicketQuantityResponseFormatter.class);

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		GetTicketQuantityResponse response = (GetTicketQuantityResponse) executorResponse;

		JSONObject result = new JSONObject();
		
		JSONObject tickets = new JSONObject();
		List<TicketItem> ticketList = response.getTicketList();
		int count = 0;
		for (TicketItem ticket : ticketList) {
			JSONObject jo = new JSONObject();
			jo.put(HtmlConstants.RRKey.GTQ_TICKET_ID, ticket.getTicketId());
			jo.put(HtmlConstants.RRKey.GTQ_TICKET_TYPE, ticket.getType());
			jo.put(HtmlConstants.RRKey.GTQ_TICKET_PRICE, ticket.getPrice());
			jo.put(HtmlConstants.RRKey.GTQ_TICKET_CURRENCY, ticket
					.getCurrency());
			jo.put(HtmlConstants.RRKey.GTQ_TICKET_QUANTITY, ticket
					.getQuantity());

			tickets.put(""+count,jo);
			count++;

		}
		
		result.put("ticketList", tickets);
		result.put("surcharge", response.getSurcharge());
		result.put("convenienceCharge", response.getConvenienceCharge());
		logger.debug("convenienceFee:"+response.getSurcharge());
		
		// JSONObject surcharge = new JSONObject();

		httpRequest.setAttribute("ajaxResponse", result.toString());
	}

}
