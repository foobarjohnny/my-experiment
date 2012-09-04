/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.cose.PoiSearchBRProxy;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.poi.util.AirportUtil;

/**
 * BRValidateAirportHandler
 * 
 * @author kwwang
 * 
 */
public class BRValidateAirportHandler implements ValidateAirportHandler {

	@Override
	public PoiSearchResponse handle(ValidateAirportRequest request,
			ExecutorContext context) throws Exception {

		AirportSearchRequest airportSearchRequest = AirportUtil
				.makeAirportSearchRequest(request);
		PoiSearchBRProxy proxy = getProxy();

		PoiSearchResponse resp = proxy.tlPoiAirportByCityAndName(
				airportSearchRequest, context.getTnContext());
		return resp;
	}

	protected PoiSearchBRProxy getProxy() {
		return BackendProxyManager.getBackendProxyFactory().getBackendProxy(
				PoiSearchBRProxy.class);
	}

}
