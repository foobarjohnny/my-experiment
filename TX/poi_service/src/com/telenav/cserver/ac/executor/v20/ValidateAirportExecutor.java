/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor.v20;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiSearchProxyV20;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.reporting.ReportType;
import com.telenav.cserver.framework.reporting.ReportingRequest;
import com.telenav.cserver.framework.reporting.ReportingUtil;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;
import com.telenav.cserver.poi.util.AirportUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ValidateAddressExecutorACEWS.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAirportExecutor extends AbstractExecutor {
	
	private static Logger logger= Logger.getLogger(ValidateAirportExecutor.class);
	
	public static final double DEGREE_MULTIPLIER = 1.e5;

	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException {
		ValidateAirportRequest request = (ValidateAirportRequest) req;
		ValidateAirportResponse response = (ValidateAirportResponse) resp;
		String airportName = request.getAirportName();

		TnContext tc = context.getTnContext();
		UserProfile userProfile = req.getUserProfile();
		ReportingRequest misLog = new ReportingRequest(
				ReportType.SERVER_MIS_LOG_REPORT, userProfile, tc);

		misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME,
				ServerMISReportor.POI_SERVLET_NAME);
		misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, this
				.getClass().getSimpleName());
		misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID,
				ServerMISReportor.VALIDATE_AIRPORT_LOGTYPE);
		misLog.addServerMisLogField(ServerMISReportor.CUSTOM16,
				tc.getProperty(TnContext.PROP_MAP_DATASET));
		misLog.addServerMisLogField(ServerMISReportor.CUSTOM00, airportName);

		PoiSearchResponse poiSearchResponse = new PoiSearchResponse();

		try {
			AirportSearchRequest airportSearchRequest = AirportUtil
					.makeAirportSearchRequest(request);

			PoiSearchProxyV20 proxy = CoseFactory
					.createPoiSearch20Proxy(context.getTnContext());
			poiSearchResponse = proxy.searchAirport(airportSearchRequest);

		} catch (Exception e) {
			logger.fatal("validate airport failed,",e);
		}

		List<TnPoi> pois = poiSearchResponse.getPois();

		if (pois == null) {
			pois = new ArrayList<TnPoi>();
		}

		String status = "-1";
		if (pois.size() > 0) {
			status = "0";
		}

		misLog.addServerMisLogField(ServerMISReportor.CUSTOM01, status);
		misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG,
				ServerMISReportor.COMPLETE_SUCCEED);
		ReportingUtil.report(misLog);

		response.setAirportList(pois);
	}
}
