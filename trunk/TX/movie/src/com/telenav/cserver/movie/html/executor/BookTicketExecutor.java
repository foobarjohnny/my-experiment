/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * BookTicketExecutor.java
 * 
 * khuang@telenav.cn
 * 
 * @version 1.0 March 9, 2011
 * 
 */
public class BookTicketExecutor extends AbstractExecutor {
	@Override
	public void doExecute(ExecutorRequest req, ExecutorResponse res,
			ExecutorContext context) throws ExecutorException {
		BookTicketRequest request = (BookTicketRequest) req;
		BookTicketResponse response = (BookTicketResponse) res;
		MovieServiceProxy.getOrderId(request, response);

		response.setStatus(ExecutorResponse.STATUS_OK);
	}
}
