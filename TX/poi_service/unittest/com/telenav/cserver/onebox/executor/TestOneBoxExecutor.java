package com.telenav.cserver.onebox.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.onebox.executor.OneBoxExecutor;
import com.telenav.cserver.onebox.executor.OneBoxRequest;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.datatypes.Stop;

import junit.framework.TestCase;

public class TestOneBoxExecutor extends TestCase {
	private OneBoxResponse resp = new OneBoxResponse();
	
	public void testDoExecute() throws ExecutorException {
		OneBoxExecutor excutor = new OneBoxExecutor();
		excutor.doExecute(getOneBoxRequest(5), resp, TestUtil
				.getExecutorContext());
	}

	public void testDoExecuteSearchAlong() throws ExecutorException {
		OneBoxExecutor excutor = new OneBoxExecutor();
		excutor.doExecute(getOneBoxRequest(7), resp, TestUtil
				.getExecutorContext());
	}

	private OneBoxRequest getOneBoxRequest(int searchType) {
		OneBoxRequest request = new OneBoxRequest();
		request.setSearchString("coffee");
		request.setPageNumber(0);
		request.setMaxResults(9);
		request.setDistanceUnit(0);
		request.setSponsorListingNumber(1);
		request.setSearchType(searchType);
		request.setTransactionId("");
		if (searchType == 7) {
			request.setRouteID(752041060);
			request.setSegmentId(0);
			request.setEdgeId(0);
			request.setShapePointId(0);
			request.setRange(0);
			request.setCurrentLat(3737391);
			request.setCurrentLon(-12199982);
			request.setSearchAlongType(0);
			
			Stop stopDest = new Stop();
			stopDest.lat = 3733950;
			stopDest.lon = -12203241;
			stopDest.city = "";
			stopDest.state = "";
			stopDest.zip = "";
			request.setStopDest(stopDest);
		}
		
		Stop stop = new Stop();
		stop.lat = 3737453;
		stop.lon = -12199983;
		stop.city = "Santa clara";
		stop.state = "Ca";
		stop.zip = "94801";
		request.setStop(stop);
		
		request.setUserProfile(TestUtil.getUserProfile());
		System.out.println(request.toString());

		return request;
	}

}
