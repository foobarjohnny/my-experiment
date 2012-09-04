/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;
import org.json.me.JSONObject;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.executor.BookTicketResponse;

/**
 * BookTicketResponseFormatter.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 Mar 9, 2010
 * 
 */
public class BookTicketResponseFormatter extends HtmlProtocolResponseFormatter {

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		BookTicketResponse response = (BookTicketResponse) executorResponse;

		//JSONObject jo = new JSONObject();
		String orderId = response.getOrderId();
		//jo.put("orderId", orderId);
		
		httpRequest.setAttribute("ajaxResponse", orderId);
	}

}
