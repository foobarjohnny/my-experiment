package com.telenav.cserver.poi.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.struts.Constant;

import junit.framework.TestCase;

public class TestPOISearchExecutor_WS extends TestCase {
	private POISearchResponse_WS resp = new POISearchResponse_WS();

	public void testDoExecute() throws ExecutorException {
		POISearchExecutor_WS excutor = new POISearchExecutor_WS();
		excutor.doExecute(getPOISearchRequest_WS(5), resp, TestUtil
				.getExecutorContext());
	}

	public void testDoExecuteSearchAlong() throws ExecutorException {
		POISearchExecutor_WS excutor = new POISearchExecutor_WS();
		excutor.doExecute(getPOISearchRequest_WS(7), resp, TestUtil
				.getExecutorContext());
	}
	
	public void testDoExecuteTN7x()  throws ExecutorException
	{
		POISearchExecutor_WS excutor = new POISearchExecutor_WS();
		excutor.doExecute(getPOISearchRequest_WS(5), resp, TestUtil
				.getExecutorContextFor7x());
	}

	private POISearchRequest_WS getPOISearchRequest_WS(int searchType) {
		POISearchRequest_WS request = new POISearchRequest_WS();
		request.setProductType("ATT_NAV");
		request.setAudioFormat("amr");
		request.setCategoryId(Constant.DEFAULT_CATEGORY);
		request.setSearchString("");
		request.setRadiusInMeter(Constant.DEFAULT_RADIUS);
		request.setNeedAudio(true);
		request.setSearchType(searchType);
		if (searchType == 7) {
			request.setRouteID(750251600);
			request.setSegmentId(1);
			request.setEdgeId(0);
			request.setShapePointId(1);
			request.setRange(18);
			request.setCurrentLat(3737469);
			request.setCurrentLon(-12199586);
			request.setSearchAlongType(0);
		}

		request.setSearchFromType(1);
		request.setSortType(Constant.SORT_BY_RELEVANCE);
		request.setPageNumber(0);
		request.setMaxResults(9);
		request.setMostPopular(false);
		request.setDistanceUnit(0);
		request.setSponsorListingNumber(1);
		Stop stop = new Stop();
		stop.lat = 3737469;
		stop.lon = -12199586;
		request.setStop(stop);

		request.setUserProfile(TestUtil.getUserProfile());

		return request;
	}
}
