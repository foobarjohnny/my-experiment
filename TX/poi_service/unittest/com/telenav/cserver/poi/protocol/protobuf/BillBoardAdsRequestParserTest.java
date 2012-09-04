package com.telenav.cserver.poi.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAds;
import com.telenav.j2me.framework.protocol.ProtoGeoFence;

public class BillBoardAdsRequestParserTest extends BillBoardAdsRequestParser{

	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
		ProtoGeoFence geoFence = ProtoGeoFence.newBuilder().setDistance(1.0)
									.setLat(3737453)
									.setLon(-12199983).build();
		
		ProtoBillBoardAds req = ProtoBillBoardAds.newBuilder().setAdsId(0)
									.setAdsUrl("")
									.setDetailViewTime(System.currentTimeMillis())
									.setExpirationTime(System.currentTimeMillis() + 1000)
									.setGeoFence(geoFence)
									.setInitialViewTime(System.currentTimeMillis() + 2000)
									.setPoiViewTime(System.currentTimeMillis() + 3000)
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
