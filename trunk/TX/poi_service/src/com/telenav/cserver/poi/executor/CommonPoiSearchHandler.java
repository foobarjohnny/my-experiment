/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiSearchProxy;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.struts.util.NSPoiUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * CommonPoiSearchHandler
 * @author kwwang
 *
 */
public class CommonPoiSearchHandler implements PoiSearchHandler, PoiConstants {

	public static Logger logger = Logger
			.getLogger(CommonPoiSearchHandler.class);

	@Override
	public PoiSearchResponse handle(PoiSearchRequest poiSearchReq,
			POISearchRequest_WS poiRequest, ExecutorContext context)
			throws Exception {
		TnContext tc = context.getTnContext();
		PoiSearchProxy poiSearchProxy = CoseFactory
				.createPoiSearchProxy(context.getTnContext());
		PoiSearchResponse result = null;

		switch (poiRequest.getSearchType()) {
		case SEARCH_ALONGROUTE:
			int searchAlongType = poiRequest.getSearchAlongType();
			if (searchAlongType == 0) {
				List<GeoCode> routePoints = NSPoiUtil.getRoutePoints(tc,
						poiRequest.getRouteID(), poiRequest.getSegmentId(),
						poiRequest.getEdgeId(), poiRequest.getShapePointId(),
						poiRequest.getRange(), poiRequest.getCurrentLat(),
						poiRequest.getCurrentLon());
				poiSearchReq.setRoutePoints(routePoints);
			} else {
				NSPoiUtil.setReverseRoutePointsAndAnchor(
						context.getTnContext(), poiSearchReq,
						poiRequest.getRouteID(), poiRequest.getRange());
			}
			result = poiSearchProxy.searchAlongRoute(poiSearchReq);
			break;
		case SEARCH_ADDRESS:
			result = poiSearchProxy.searchWithinDistance(poiSearchReq);
			break;
		default:
			logger.fatal("unsupported search type = "
					+ poiRequest.getSearchType());
		}
		return result;
	}

}
