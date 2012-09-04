/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiSearchProxy;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.poi.util.AirportUtil;

/**
 * CommonValidateAirportHandler
 * 
 * @author kwwang
 * 
 */
public class CommonValidateAirportHandler implements ValidateAirportHandler {

	@Override
	public PoiSearchResponse handle(ValidateAirportRequest request,
			ExecutorContext context) throws Exception {
		AirportSearchRequest airportSearchRequest = AirportUtil
				.makeAirportSearchRequest(request);

		PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(context
				.getTnContext());
		PoiSearchResponse poiSearchResponse = proxy
				.searchAirport(airportSearchRequest);
		return poiSearchResponse;
	}

}
