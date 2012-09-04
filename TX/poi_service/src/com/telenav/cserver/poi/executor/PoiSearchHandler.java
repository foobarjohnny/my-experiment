/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;

/**
 * PoiSearchHandler
 * @author kwwang
 *
 */
public interface PoiSearchHandler {
	PoiSearchResponse handle(PoiSearchRequest poiSearchReq,
			POISearchRequest_WS poiRequest, ExecutorContext context)
			throws Exception;
}
