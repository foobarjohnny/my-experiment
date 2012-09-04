package com.telenav.cserver.poi.protocol.protobuf;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.backend.datatypes.adservice.GeoFence;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.BillBoardAdsResponse;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class BillBoardAdsResponseFormatterTest extends BillBoardAdsResponseFormatter{


	ProtocolBuffer formatTarget = new ProtocolBuffer();
	BillBoardAdsResponse resp = new BillBoardAdsResponse();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		
		BillBoardAds bbs = new BillBoardAds();
		List<BillBoardAds> billBoardAdsList = new ArrayList<BillBoardAds>();
		billBoardAdsList.add(bbs);
		resp.setBillBoardAdsList(billBoardAdsList);
		
		GeoFence geoFence = new GeoFence();
		geoFence.setDistance(1.0);
		geoFence.setLat(3737453);
		geoFence.setLon(-12199983);
		
		bbs.setAdsUrl("");
		bbs.setDetailViewTime(System.currentTimeMillis());
		bbs.setExpirationTime(System.currentTimeMillis() + 1000);
		bbs.setGeoFence(geoFence);
		bbs.setInitialViewTime(System.currentTimeMillis() + 2000);
		bbs.setPoiViewTime(System.currentTimeMillis() + 3000);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}

}
