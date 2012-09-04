/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.cose;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.apontador.apirequest.data.ApontadorPoint;
import com.apontador.apirequest.proxy.POIProxy;
import com.apontador.apirequest.proxy.VIVOPOIRequestInterface;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.proxy.AbstractProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.xnav.telenavfinder.Error;
import com.telenav.xnav.telenavfinder.NavFinderStatus;
import com.televigation.db.poi.Poi;

/**
 * PoiSearchBRProxy
 * 
 * @author kwwang
 * 
 */
@BackendProxy
@ThrottlingConf("PoiSearchBRProxy")
public class PoiSearchBRProxy extends AbstractProxy {

	private VIVOPOIRequestInterface poiProxy;

	protected PoiSearchBRProxy() {
		poiProxy = new POIProxy();
	}

	@ProxyDebugLog
	@Throttling
	public PoiSearchResponse tlWithinCityZip(PoiSearchRequest request,
			TnContext tc) throws ThrottlingException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("tlWithinCityZip");
		cli.addData("req", ReflectionToStringBuilder.toString(request));
		PoiSearchResponse resp = new PoiSearchResponse();
		try {
			NavFinderStatus result = poiProxy.tlWithinCityZip(
					BRPoiSearchProxyHelper.makeAreaSearchParam(request),
					request.getRadiusInMeter());

			if (Error.POI_FOUND == result.getStatus()) {
				resp.setPois(getNormalPoi(result));
			}

			cli.addData("respStatus", String.valueOf(result.getStatus()));
		} finally {
			cli.complete();
		}
		return resp;
	}

	@ProxyDebugLog
	@Throttling
	public PoiSearchResponse tlWithinDistance(PoiSearchRequest request,
			TnContext tc) throws ThrottlingException {

		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("tlWithinDistance");
		cli.addData("req", ReflectionToStringBuilder.toString(request));
		PoiSearchResponse resp = new PoiSearchResponse();
		try {
			NavFinderStatus result = poiProxy.tlWithinDistance(
					request.getRadiusInMeter(),
					BRPoiSearchProxyHelper.makeAreaSearchParam(request));

			if (Error.POI_FOUND == result.getStatus()) {
				resp.setPois(getNormalPoi(result));
			}
			cli.addData("respStatus", String.valueOf(result.getStatus()));
		} finally {
			cli.complete();
		}
		return resp;
	}

	@ProxyDebugLog
	@Throttling
	public PoiSearchResponse tlWithinRoute(PoiSearchRequest request,
			List<ApontadorPoint> points, TnContext tc)
			throws ThrottlingException {

		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("tlWithinRoute");
		cli.addData("req", ReflectionToStringBuilder.toString(request));
		PoiSearchResponse resp = new PoiSearchResponse();
		try {
			NavFinderStatus result = poiProxy.tlWithinRoute(
					BRPoiSearchProxyHelper.makeAreaSearchParam(request),
					points, request.getRadiusInMeter());

			if (Error.POI_FOUND == result.getStatus()) {
				resp.setPois(getNormalPoi(result));
			}
			cli.addData("respStatus", String.valueOf(result.getStatus()));
		} finally {
			cli.complete();
		}
		return resp;
	}

	@ProxyDebugLog
	@Throttling
	public PoiSearchResponse tlPoiAirportByCityAndName(
			AirportSearchRequest request, TnContext tc)
			throws ThrottlingException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("tlPoiAirportByCityAndName");
		cli.addData("req", ReflectionToStringBuilder.toString(request));
		PoiSearchResponse resp = new PoiSearchResponse();
		try {
			NavFinderStatus result = poiProxy.tlPoiAirportByCityAndName("",
					request.getAirportQuery());

			if (Error.POI_FOUND == result.getStatus()) {
				resp.setPois(getNormalPoi(result));
			}
			cli.addData("respStatus", String.valueOf(result.getStatus()));
		} finally {
			cli.complete();
		}
		return resp;
	}

	@Override
	public String getProxyConfType() {
		return "";
	}

	protected List<TnPoi> getNormalPoi(NavFinderStatus result) {
		List<TnPoi> normalPois = new ArrayList<TnPoi>();
		for (Enumeration ePoi = result.getPois().elements(); ePoi
				.hasMoreElements();) {
			Poi poi = (Poi) ePoi.nextElement();
			normalPois.add(BRPoiSearchProxyHelper.converToTnPoi(poi));
		}
		return normalPois;
	}

}
