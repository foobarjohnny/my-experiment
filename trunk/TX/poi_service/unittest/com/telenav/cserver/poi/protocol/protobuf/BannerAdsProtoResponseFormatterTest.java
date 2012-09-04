package com.telenav.cserver.poi.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.BannerAdsReponse;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class BannerAdsProtoResponseFormatterTest extends BannerAdsProtoResponseFormatter{

	ProtocolBuffer formatTarget = new ProtocolBuffer();
	BannerAdsReponse resp = new BannerAdsReponse();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		resp.setImageHeight(120);
		resp.setImageWidth(80);
		resp.setClickUrl("clickUrl");
		resp.setImageUrl("imageUrl");
		
		byte[] imgData = {0x1, 0x2};
		resp.setImgData(imgData);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}

}
