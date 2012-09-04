package com.telenav.cserver.poi.executor;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.Stop;

import junit.framework.TestCase;

public class TestPOIFeedbackSaveExecutor extends TestCase {
	private POIFeedbackSaveResponse resp = new POIFeedbackSaveResponse();

	public void testDoExecuteForList() throws ExecutorException, JSONException {
		POIFeedbackSaveExecutor excutor = new POIFeedbackSaveExecutor();
		excutor.doExecute(getPOIFeedbackSaveRequestForList(), resp, TestUtil
				.getExecutorContext());
	}

	public void testDoExecuteForDetail() throws ExecutorException,
			JSONException {
		POIFeedbackSaveExecutor excutor = new POIFeedbackSaveExecutor();
		excutor.doExecute(getPOIFeedbackSaveRequestForDetail(), resp, TestUtil
				.getExecutorContext());
	}

	private POIFeedbackSaveRequest getPOIFeedbackSaveRequestForList()
			throws JSONException {
		POIFeedbackSaveRequest request = new POIFeedbackSaveRequest();

		request.setSearchKeyword("");
		request.setSearchCatName("ATM");
		JSONObject searchLocationJson = new JSONObject();
		searchLocationJson.put("lat", 3735869);
		searchLocationJson.put("lon", -12199734);
		Stop lStop = TnUtil.convertToStop(searchLocationJson);
		request.setSearchLocation(lStop);

		String[] feedbackStrs = new String[1];
		feedbackStrs[0] = "Results contained duplicates";
		request.setFeedbacks(feedbackStrs);
		request
				.setFeedbackQuestion("What was wrong with your search for - ATM?");
		request.setComments("Unit test poi list...........");
		request.setFeedbackPage("POIListFeedback");

		request.setUserProfile(TestUtil.getUserProfile());
		System.out.println(request.toString());
		return request;
	}

	private POIFeedbackSaveRequest getPOIFeedbackSaveRequestForDetail()
			throws JSONException {
		POIFeedbackSaveRequest request = new POIFeedbackSaveRequest();

		JSONObject searchLocationJson = TestUtil.getLocationJSON();
		Stop lStop = TnUtil.convertToStop(searchLocationJson);

		request.setPoiName("Citibank");
		request.setPoiPhoneNumber("8006273999");
		request.setPoiLocation(lStop);

		String[] feedbackStrs = new String[1];
		feedbackStrs[0] = "Address number incorrect";
		request.setFeedbacks(feedbackStrs);
		request.setFeedbackQuestion("What is incorrect about - Citibank?");
		request.setComments("Unit test poi detail.......");
		request.setFeedbackPage("POIDetailFeedback");

		request.setUserProfile(TestUtil.getUserProfile());
		System.out.println(request.toString());
		return request;
	}
}
