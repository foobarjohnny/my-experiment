/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
/**
 * @TODO	Call the executor to implement business logic
 * @author  
 * @version 1.0 
 */
public class HtmlHotelExecutor extends AbstractExecutor {

	@Override
	public void doExecute(ExecutorRequest executorRequest,
			ExecutorResponse executorResponse, ExecutorContext context)
			throws ExecutorException {
		HtmlHotelResponse response = (HtmlHotelResponse) executorResponse;
		HtmlHotelRequest request = (HtmlHotelRequest) executorRequest;
		
		if(request.isDummy())
		{
			response.setHotel(HtmlServiceProxyDummy.getInstance()
					.getDummyHotel());
		}
		else
		{
			response.setHotel(HtmlPoiDetailServiceProxy.getInstance()
					.getHotelDetail(request.getPoiId()));
		}

	}
}
