package com.telenav.browser.movie.datatypes;

import static org.junit.Assert.*;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.telenav.j2me.datatypes.Stop;

public class TestAddressTest {
	
	private JSONObject addressJSON;
	private String addressString = "{\"zip\":\"\",\"lon\":-12199983,\"state\":\"\",\"firstLine\":\"\",\"label\":\"\",\"type\":6,\"lat\":3737453,\"country\":\"\",\"city\":\"\"}";

	@Before
	public void setUp() throws Exception {
		addressJSON = new JSONObject(addressString);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMakeFrom() throws JSONException{
		Address address = new Address();
		address.makeFrom(addressJSON);
		
		assertEquals(address.label, "");
		assertEquals(address.firstLine,"");
		assertEquals(address.city,"");
		assertEquals(address.state,"");
		assertEquals(address.country,"");
		assertEquals(address.zip, "");
		assertEquals(address.lat,3737453);
		assertEquals(address.lon,-12199983);
		assertEquals(address.stopType,Stop.STOP_CURRENT_LOCATION);
	}

}
