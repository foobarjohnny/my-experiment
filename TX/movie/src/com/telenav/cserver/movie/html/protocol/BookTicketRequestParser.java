/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.datatypes.BookingInfoItem;
import com.telenav.cserver.movie.html.datatypes.TicketItem;
import com.telenav.cserver.movie.html.executor.BookTicketRequest;
import com.telenav.cserver.movie.html.executor.GetTicketQuantityRequest;
import com.telenav.cserver.movie.html.executor.MovieDataConvert;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;

/**
 * BookTicketRequestParser.java
 *
 * khuang@telenav.cn
 *
 * @version 1.0 Mar 9, 2011
 *
 */
public class BookTicketRequestParser extends HtmlProtocolRequestParser {
	public String getExecutorType() {
		return "bookTicket";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		String jsonStr = httpRequest.getParameter("jsonStr");
		JSONObject jo = new JSONObject(jsonStr);

		BookingInfoItem info = new BookingInfoItem();

		JSONArray tickets = jo.getJSONArray("ticketArray");
		List<TicketItem> ticketItems = new ArrayList<TicketItem>();
		//get all the ticket
		for (int i = 0; i < tickets.length(); i++) {
			JSONObject ticket = tickets.getJSONObject(i);
			TicketItem ticketItem = new TicketItem();
			ticketItem.setTicketId(ticket.getString("id"));
			ticketItem.setPrice(ticket.getDouble("price"));
			ticketItem.setQuantity(ticket.getInt("quantity"));
			ticketItem.setCurrency(ticket.getString("currency"));
			ticketItem.setType(ticket.getString("type"));

			ticketItems.add(ticketItem);
		}
		info.setTickets(ticketItems);

		info.setMovieId(jo.getString("movieId"));
		info.setTheaterId(jo.getString("theaterId"));
		info.setShowDate(jo.getString("showDate"));
		info.setShowTime(jo.getString("showTime"));
		info.setConfirmEmail(jo.getString("confirmEmail"));
		info.setTotalAmout(jo.getDouble("totalAmount"));

		BookTicketRequest request = new BookTicketRequest();
		request.setBookingInfo(info);

		// get the userId based on ssoToken
		String ssoTokenStr = httpRequest.getParameter("ssoToken");
		long userId = -1;
		try{
		    userId = Long.parseLong(HtmlCommonUtil.getUserId(ssoTokenStr));
		}catch(NumberFormatException e)
        {
        }
		request.setUserId(userId);


		return request;
	}
}
