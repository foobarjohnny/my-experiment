/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import com.telenav.datatypes.address.v40.GeoCoordinate;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.geocoding.v40.GeoCodingServiceRequestDTO;
import com.telenav.services.geocoding.v40.SearchArea;

/**
 * GeoCodeProxyHelper
 * @author kwwang
 *
 */
public class GeoCodeProxyHelper {
	
    private static final String clientName = "ace-ws-client";

    private static final String clientVersion = "1.0.0.3";
    
    private static final String componentName = "POI";
	
	public static GeoCodingServiceRequestDTO createGeoCodingServiceRequestDTO(GeoCodeProxyRequest request,TnContext tnContext)
	{
		GeoCodingServiceRequestDTO dto = new GeoCodingServiceRequestDTO();
        dto.setComponentName(componentName);
        dto.setClientName(clientName);
        dto.setClientVersion(clientVersion);
        dto.setContextString(tnContext.toContextString());
        dto.setTransactionId(request.getTransactionId());
        
        SearchArea searchArea = new SearchArea();
		searchArea.addCountryList(Country.Factory.fromValue(request.getCountry()));
		GeoCoordinate geo = new GeoCoordinate();
		geo.setLatitude(request.getLat());
		geo.setLongitude(request.getLon());
		searchArea.setAnchorPoint(geo);
		dto.setSearchArea(searchArea);
		
		dto.setAddress(request.getAddressLine());
        
        return dto;
	}
}
