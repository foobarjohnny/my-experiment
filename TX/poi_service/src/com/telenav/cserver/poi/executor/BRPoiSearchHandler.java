/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.List;

import org.apache.log4j.Logger;

import com.apontador.apirequest.data.ApontadorPoint;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.cose.PoiSearchBRProxy;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.struts.util.NSPoiUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * BRPoiSearchHandler
 * @author kwwang
 *
 */
public class BRPoiSearchHandler implements PoiSearchHandler, PoiConstants {
	public static Logger logger = Logger.getLogger(BRPoiSearchHandler.class);

	@Override
	public PoiSearchResponse handle(PoiSearchRequest poiSearchReq,
			POISearchRequest_WS poiRequest, ExecutorContext context)
			throws Exception {
		TnContext tc = context.getTnContext();
		PoiSearchResponse result = null;
		PoiSearchBRProxy proxy = getProxy();
		switch (poiRequest.getSearchType()) {
		case SEARCH_ALONGROUTE:
			int searchAlongType = poiRequest.getSearchAlongType();
			List<ApontadorPoint> points = null;
			if (searchAlongType == 0) {
				points = NSPoiUtil.getRoutePointsDistnaceApart(tc,
						poiRequest.getRouteID(), poiRequest.getSegmentId(),
						poiRequest.getEdgeId(), poiRequest.getShapePointId(),
						poiRequest.getRange(), poiRequest.getCurrentLat(),
						poiRequest.getCurrentLon(),
						poiRequest.getRadiusInMeter(),
						NSPoiUtil.MAX_SEARCH_DIST);

			} else {
				points = NSPoiUtil.getReverseRoutePointsDistnaceApart(tc,
						poiSearchReq, poiRequest.getRouteID(),
						poiRequest.getRange(), poiRequest.getRadiusInMeter(),
						NSPoiUtil.MAX_SEARCH_DIST);
			}
			result = proxy.tlWithinRoute(poiSearchReq, points, tc);
			break;
		case SEARCH_ADDRESS:
			result = proxy.tlWithinDistance(poiSearchReq, tc);
			break;
		default:
			logger.fatal("unsupported search type = "
					+ poiRequest.getSearchType());
		}
		return result;
	}

	protected PoiSearchBRProxy getProxy() {
		return BackendProxyManager.getBackendProxyFactory().getBackendProxy(
				PoiSearchBRProxy.class);
	}
}
