package com.telenav.cserver.poi.protocol.protobuf;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.LocalAppInfo;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.Review;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.resource.data.AudioElement;
import com.telenav.resource.data.AudioMessage;
import com.telenav.resource.data.AudioRule;
import com.telenav.resource.data.PromptItem;
import com.telenav.resource.data.ResourceInfo;

public class SearchPoiProtoResponseFormatterTest extends SearchPoiProtoResponseFormatter{

	ProtocolBuffer formatTarget = new ProtocolBuffer();
	POISearchResponse_WS resp = new POISearchResponse_WS();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		resp.setTotalCount(10);
		
		PromptItem promptItem = new PromptItem();
		PromptItem[] promptItems = {promptItem};
		
		BasePoi basePoi = new BasePoi();
		List<BasePoi> basePois = new ArrayList<BasePoi>();
		basePois.add(basePoi);
		
		POI sponsorPoi = new POI();
		List<POI> sponsorPois = new ArrayList<POI>();
		sponsorPois.add(sponsorPoi);
		
		resp.setPromptItems(promptItems);
		resp.setBasePoiList(basePois);
		resp.setSponsorPoiList(sponsorPois);
		resp.setPoiDetailUrl("poiDetailUrl");	
		
		AudioMessage aduioElement1 = new AudioMessage();
		AudioRule aduioElement2 = new AudioRule();
		
		
		ResourceInfo resourceInfo = new ResourceInfo();
		aduioElement1.setResourceInfo(resourceInfo);
		AudioMessage child = new AudioMessage();
		child.setChildren(null);
		child.setResourceInfo(resourceInfo);
		AudioMessage[] childs = {child};
		aduioElement1.setChildren(childs);
		
		
		aduioElement2.setRuleId(0);
		int[] intArgs = {0x1, 0x2};
		aduioElement2.setIntArgs(intArgs);
		aduioElement2.setNodeArgs(childs);

		AudioElement[] audioElements = {aduioElement1, aduioElement2};
		promptItem.setAudioElements(audioElements);
		
		byte[] buf = {0x1, 0x2};
		List<Review> reviews = new ArrayList<Review>();
		Review e = new Review();
		reviews.add(e);
		LocalAppInfo localAppInfo = new LocalAppInfo("localAppInfo");
		List<LocalAppInfo> localAppInfos = new ArrayList<LocalAppInfo>();
		localAppInfos.add(localAppInfo);
		
		sponsorPoi.setAudioPoiName(buf);
		sponsorPoi.setReviews(reviews);
		sponsorPoi.setLocalAppInfos(localAppInfos);
		
		
		basePoi.setLocalAppInfos(localAppInfos);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}

}
