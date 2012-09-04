/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractNewStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.resource.datatypes.RegionGroup;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.geocoding.v40.GeoCodingServiceRequestDTO;
import com.telenav.services.geocoding.v40.GeoCodingServiceResponseDTO;
import com.telenav.services.geocoding.v40.GeoCodingServiceStub;

/**
 * GeoCodeProxy
 * 
 * @author kwwang
 * 
 */
@BackendProxy
@ThrottlingConf("GeoCodeProxy")
public class GeoCodeProxy extends
		AbstractNewStubProxy<GeoCodingServiceStub, String> {

	private Logger logger = Logger.getLogger(GeoCodeProxy.class);

	public static final String DATA_SET = "dataset";

	@ProxyDebugLog
	@Throttling
	public GeoCodingServiceResponseDTO geoCode(
			GeoCodingServiceRequestDTO request, UserProfile user, TnContext tc)
			throws ThrottlingException {
		GeoCodingServiceStub stub = null;
		GeoCodingServiceResponseDTO response = null;
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("geoCode");
		cli.addData("GeoCodingServiceResponseDTO",
				ReflectionToStringBuilder.toString(request));
		try {
			stub = createStub(getWebServiceConfigInterface(),createKey(user, tc));
			response = stub.geoCode(request);
			cli.addData("GeoCodingServiceResponseDTO",
					ReflectionToStringBuilder.toString(response));
		} catch (Exception e) {
			cli.setStatus(e);
			logger.error("geoCode failed, ", e);
		} finally {
			cli.complete();
			WebServiceUtils.cleanupStub(stub);
		}

		return response;
	}

	@Override
	public String getProxyConfType() {
		return "WS_SERVICE_GEOCODE";
	}

	@Override
	protected GeoCodingServiceStub createStub(WebServiceConfigInterface ws,
			String v) throws Exception {
		GeoCodingServiceStub stub = null;
		try {
			Map<String, String> urlMap = ws.getServiceUrlMapping();
			stub = new GeoCodingServiceStub(createContext(ws),
					getUrl(urlMap, v));
			stub._getServiceClient()
					.getOptions()
					.setTimeOutInMilliSeconds(
							ws.getWebServiceItem().getWebServiceTimeout());
		} catch (Exception e) {
			logger.fatal("create GeoCodingServiceStub stub failed", e);
		}

		return stub;
	}

	

	public String createKey(UserProfile userProfile, TnContext tc) {
		StringBuilder key = new StringBuilder();
		if (userProfile != null && userProfile.getRegion() != null) {
			key.append(RegionGroup.getRegionGroup(userProfile.getRegion()));
		}

		if (tc != null && tc.getProperty(DATA_SET) != null) {
			key.append(SEPARATOR + tc.getProperty(DATA_SET));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("get geoCode service key:" + key);
		}

		return key.toString();
	}

}
