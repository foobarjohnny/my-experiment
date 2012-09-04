package com.telenav.cserver.ac.protocol.v20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.v20.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;

public class ValidateAddressResponseFormatterACEWSTest extends
		ValidateAddressResponseFormatterACEWS {
	MockHttpServletRequest request = new MockHttpServletRequest();
	ValidateAddressResponseACEWS response = new ValidateAddressResponseACEWS();

	@Before
	public void setUp() throws Exception {
		
		GeoCodedAddress geoAddr = new GeoCodedAddress();
		List<GeoCodedAddress> addresses = new ArrayList<GeoCodedAddress>();
		addresses.add(geoAddr);
		
		geoAddr.setFirstLine("Semiconductor Dr at Kifer Rd");
		geoAddr.setLastLine("");
		geoAddr.setLabel("Semiconductor Dr");
		
		geoAddr.setState("CA");
		geoAddr.setPostalCode("95051");
		geoAddr.setCity("Santa Clara");
		geoAddr.setCrossStreetName("");
		geoAddr.setStreetName("");
		geoAddr.setCountry("USA");
		geoAddr.setSuite("");
		geoAddr.setSublocality("");
		geoAddr.setLocality("");
		geoAddr.setLocale("");
		geoAddr.setSubStreet("");
		geoAddr.setBuildingName("");
		geoAddr.setAddressId("");
		GeoCodeSubStatus geoCodeSubStatus = new GeoCodeSubStatus();
		geoAddr.setSubStatus(geoCodeSubStatus);
		HashMap<String, String> lines = new HashMap<String, String>();
		lines.put(com.telenav.cserver.backend.datatypes.AddressFormatConstants.FIRST_LINE, "Semiconductor Dr at Kifer Rd");
		lines.put(com.telenav.cserver.backend.datatypes.AddressFormatConstants.LAST_LINE,"");
		geoAddr.setLines(lines);
		
		
		response.setAddressList(addresses);
		response.setLabel("geoAddr");
		response.setMaitai(false);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBrowserResponseHttpServletRequestExecutorResponse() {
		
		try
		{
			this.parseBrowserResponse(request, response);
		}catch(Throwable e)
		{
			Assert.fail();
		}
		
	}

}
