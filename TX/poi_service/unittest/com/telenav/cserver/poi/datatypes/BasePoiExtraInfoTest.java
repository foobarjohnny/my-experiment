package com.telenav.cserver.poi.datatypes;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasePoiExtraInfoTest extends BasePoiExtraInfo {

	BasePoiExtraInfo instance = new BasePoiExtraInfo();
	@Before
	public void setUp() throws Exception {
		instance.userPreviousRating = 3;
		instance.ratingNumber = 1;
		instance.shortMessage = "SMS";
		instance.message = "MSG";
		instance.sourceAdId = "1234";
		instance.isReservable = false;
		instance.partnerPoiId = "1000";
		instance.partner = "YelP";
		instance.poiNameAudio = new byte[]{0x1, 0x2};
		
		HashMap<String, String> extraProperties = new HashMap<String, String>();
		extraProperties.put("purpose", "UnitTest");
		instance.extraProperties = extraProperties;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToProtobuf() {
		instance.toProtobuf();
	}

}
