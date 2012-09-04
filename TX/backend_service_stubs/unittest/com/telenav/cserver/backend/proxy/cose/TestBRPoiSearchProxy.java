package com.telenav.cserver.backend.proxy.cose;

import org.junit.Test;

import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.proxy.BackendProxyManager;

public class TestBRPoiSearchProxy {
	private PoiSearchBRProxy proxy;

	public void init() {
		proxy = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
				PoiSearchBRProxy.class);
	}

	@Test
	public void tlWithinCityZip() {
		PoiSearchRequest request=new PoiSearchRequest();
		
		
	}
}
