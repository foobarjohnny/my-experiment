package com.telenav.cserver.weather.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoWeatherReq;

public class I18NWeatherProtoRequestParserTest extends I18NWeatherProtoRequestParser{

	
	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
		ProtoStop stop = ProtoStop.newBuilder().setCity("sunnyvale")
								.setIsGeocoded(true)
								.setHashCode(10)
								.setIsShareAddress(true)
								.setLabel("label")
								.setFirstLine("1130, kifer rd")
								.setState("CA")
								.setZip("94086")
								.setCountry("USA")
								.setCounty("")
								.setStopType(0)
								.setStopStatus(0)
								.build();
		
		ProtoWeatherReq req = ProtoWeatherReq.newBuilder()
									.setIsCanadianCarrier(false)
									.setLocale("en_US")
									.setLocation(stop)
									.build();			
									
		protoBuf.setBufferData(req.toByteArray());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ExecutorException {
		this.parse(protoBuf);
	}

}
