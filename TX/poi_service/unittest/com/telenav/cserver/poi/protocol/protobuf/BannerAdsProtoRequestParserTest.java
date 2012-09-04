package com.telenav.cserver.poi.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBannerAdsReq;

public class BannerAdsProtoRequestParserTest extends BannerAdsProtoRequestParser{
	
	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
		ProtoBannerAdsReq req = ProtoBannerAdsReq.newBuilder()
									.setAddressType(6)
									.setCategoryId(595)
									.setHeight(800)
									.setWidth(480)				
									.setKeyWord("Food")
									.setLat(3737453)
									.setLon(-12199983)
									.setPageId(1)
									.setPageIndex(0)
									.setSearchId("")
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
