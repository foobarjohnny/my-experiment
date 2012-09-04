/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.geocoding.v40.GeoCodingServiceResponseDTO;

/**
 * TestGeoCodeProxy
 * @author kwwang
 *
 */
public class TestGeoCodeProxy {
	
	protected TnContext tc;
	
	protected UserProfile user;
	
	@Before
	public void init()
	{
		tc=new TnContext();
		tc.addProperty("dataset", "Navteq");
		user=new UserProfile();
		user.setRegion("US");
	}
	
	@Test
	public void geoCode() throws ThrottlingException
	{
		GeoCodeProxyRequest request=new GeoCodeProxyRequest();
		request.setAddressLine("<HOUSE_NUMBER,STREET>=5950 Mohawk Drive San Jose, CA 95123<CITY>=;<POSTAL_CODE>=");
		request.setCountry(user.getRegion());
		request.setTransactionId("11000211");//mock one
		request.setLat(37.373627);
		request.setLon(-121.999022);
		GeoCodingServiceResponseDTO resp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(GeoCodeProxy.class).geoCode(GeoCodeProxyHelper.createGeoCodingServiceRequestDTO(request, tc), user, tc);
		System.out.println( "TestGeoCodeProxy status: " + resp.getAddresses()[0].getState() );
		System.out.println( "TestGeoCodeProxy city: " + resp.getAddresses()[0].getCity() );
		Assert.assertNotNull(resp);
	}
	
}
