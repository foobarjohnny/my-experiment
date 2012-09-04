package com.telenav.cserver.poidetail.protocol.v20;

import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poidetail.executor.v20.POIDetailsResponse;

public class POIDetailsResponseFormatterTest extends POIDetailsResponseFormatter{
	
	JSONObject formatTarget = new JSONObject();
	
	POIDetailsResponse resp = new POIDetailsResponse();
	POIDetailsResponse[] responses = {resp};
	
	
	@Before
	public void setUp() throws Exception {
		resp.setErrorMessage("errorMsg");
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setPoiId(1234);
		resp.setBusinessHours("businessHours");
		resp.setBusinessHoursNote("businessHoursNote");
		resp.setDescription("description");
		resp.setPriceRange("priceRange");
		resp.setOlsonTimezone("olsonTimezone");
		resp.setLogoId("logoId");
		resp.setMediaServerKey("mediaServerKey");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}

}
