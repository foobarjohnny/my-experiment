/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;

/**
 * ValidateAirportHandler
 * @author kwwang
 *
 */
public interface ValidateAirportHandler {
	PoiSearchResponse handle(ValidateAirportRequest request,
			ExecutorContext context) throws Exception;
}
