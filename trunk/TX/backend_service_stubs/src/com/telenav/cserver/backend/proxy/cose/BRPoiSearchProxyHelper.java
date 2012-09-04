/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.cose;

import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.xnav.telenavfinder.AreaSearchParam;
import com.televigation.db.address.Address;
import com.televigation.db.poi.Poi;

public class BRPoiSearchProxyHelper {

	public static final double DM5_TO_KM = 0.001113195;

	public static final double KM_TO_DM5 = 1 / DM5_TO_KM;

	public static AreaSearchParam makeAreaSearchParam(PoiSearchRequest request) {
		AreaSearchParam asp = new AreaSearchParam();
		asp.setAnchor(makeAddressFrom(request.getAnchor()));
		asp.setCategoryId(request.getCategoryId());
		asp.setBrandName(request.getPoiQuery());
		asp.setBrandNameMatching(AreaSearchParam.PREFIXHEAD);
		asp.setPageNumber(request.getPageNumber());
		asp.setMaxResultCount(request.getPageSize());
		asp.setRawCountNeeded(true);
		return asp;
	}

	public static TnPoi converToTnPoi(Poi poi) {
		TnPoi tnPoi = new TnPoi();
		tnPoi.setPoiId(poi.getPoiId());
		tnPoi.setNavigable(poi.isNavigable());
		tnPoi.setVendor(poi.getVendorCode());
		tnPoi.setDistanceInMeter(poi.getDistance() * KM_TO_DM5);

		// make address
		com.telenav.cserver.backend.datatypes.Address addr = new com.telenav.cserver.backend.datatypes.Address();
		addr.setCityName(poi.getAddress().getCity());
		addr.setState(poi.getAddress().getProvince());
		addr.setCountry(poi.getAddress().getCountry());
		addr.setPostalCode(poi.getAddress().getPostalCode());
		addr.setLatitude(poi.getAddress().getLat());
		addr.setLongitude(poi.getAddress().getLon());
		tnPoi.setAddress(addr);

		tnPoi.setPhoneNumber(poi.getPhone());
		return tnPoi;
	}

	public static Address makeAddressFrom(
			com.telenav.cserver.backend.datatypes.Address addr) {
		Address address = new Address();
		address.setLat(addr.getLatitude());
		address.setLon(addr.getLongitude());
		address.setPostalCode(addr.getPostalCode());
		address.setCity(addr.getCityName());
		address.setProvince(addr.getState());
		address.setCountry(addr.getCountry());
		return address;
	}
}
