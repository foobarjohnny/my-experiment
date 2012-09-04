package com.telenav.cserver.poi.datatypes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.j2me.framework.protocol.ProtoStop;

public class DataConvertTest {

	DataConvert instance = new DataConvert();
	com.telenav.j2me.datatypes.Stop stop = new com.telenav.j2me.datatypes.Stop();
	ProtoStop protoStop = null;
	@Before
	public void setUp() throws Exception {
		stop.city = "Palo Alto";
		stop.country = "USA";
		stop.county = "";
		stop.firstLine = "811 E. Charleston Road";
		stop.isGeocoded = false;
		stop.label = "";
		stop.isShareAddress = false;
		stop.lat = 3742204;
		stop.lon = -12210461;
		stop.state = "CA";
		stop.stopId = "";
		stop.zip = "94303";
		
		protoStop = ProtoStop.newBuilder().setCity("CA")
					.setCountry("USA")
					.setCounty("")
					.setFirstLine("811 E. Charleston Road")
					.setIsGeocoded(false)
					.setIsShareAddress(false)
					.setLabel("")
					.setLat(3742204)
					.setLon(-12210461)
					.setState("CA")
					.setStopId("")
					.setZip("94303")
					.build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertToStopStop() {
		DataConvert.convertToStop(stop);
	}

	@Test
	public void testConvertToStopProtoStop() {
		DataConvert.convertToStop(protoStop);
	}

}
