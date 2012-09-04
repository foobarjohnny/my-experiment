/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
/**
 * @TODO	Call the executor to implement business logic
 * @author  
 * @version 1.0 
 */
public class GetRestaurantDetailExecutor extends AbstractExecutor {

	@Override
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) {

		GetRestaurantDetailRequest rRequest = (GetRestaurantDetailRequest) req;
		GetRestaurantDetailResponse rResponse = (GetRestaurantDetailResponse) resp;
		HtmlRestaurantProxy.getRestaurantDetail(rRequest, rResponse);

	}

}
