package com.telenav.cserver.ac.protocol.protobuf;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoGeoCodeSubStatus;
import com.telenav.j2me.framework.protocol.ProtoStop;

public class ValidateAddressResponseFormatterTest {

	private ValidateAddressResponseFormatter instance = new ValidateAddressResponseFormatter();
	private DataSource dataCenter = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
		List<GeoCodedAddress> addresses = new ArrayList();
		addresses.add(getGeoCodedAddress());
		ValidateAddressResponseACEWS resp = new ValidateAddressResponseACEWS();
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setMessage("just for testing");
		resp.setTotalCount(1);
		resp.setGeoCodeStatusCode(0);
		resp.setAddressList(addresses);
		ExecutorResponse[] resps = {resp};
		
		dataCenter.addData(Object.class.getName(), new ProtocolBuffer());		
		dataCenter.addData(ExecutorResponse[].class.getName(), resps);
		dataCenter.addData(ProtocolBuffer.class.getName(), new ProtocolBuffer());
		dataCenter.addData(GeoCodedAddress.class.getName(), getGeoCodedAddress());
		dataCenter.addData(ProtoStop.class.getName(), null);
		dataCenter.addData(ProtoGeoCodeSubStatus.class.getName(), null);
		dataCenter.addData(GeoCodeSubStatus.class.getName(), getGeoCodeSubStatus());
		
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing
		dataCenter.clear();
		
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataCenter);
	}
	
	public GeoCodeSubStatus getGeoCodeSubStatus(){
		GeoCodeSubStatus subStatus = new GeoCodeSubStatus();
		return subStatus;
	}
	
	public GeoCodedAddress getGeoCodedAddress(){
		
		GeoCodedAddress geoAddr = new GeoCodedAddress();
		geoAddr.setCityName("city name");
		geoAddr.setCountry("country");
		geoAddr.setCounty("county");
		geoAddr.setCrossStreetName("cross streee");
		geoAddr.setDoor("door");
		geoAddr.setFirstLine("first line");
		geoAddr.setLastLine("last line");
		geoAddr.setLabel("label");
		geoAddr.setLatitude(10000);
		geoAddr.setLongitude(10000);
		geoAddr.setPostalCode("zip");
		geoAddr.setStreetName("street name");
		geoAddr.setSubStatus(getGeoCodeSubStatus());
		return geoAddr;
	}
}
