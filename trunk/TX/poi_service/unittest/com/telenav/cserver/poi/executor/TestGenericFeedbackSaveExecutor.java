package com.telenav.cserver.poi.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;

import junit.framework.TestCase;

public class TestGenericFeedbackSaveExecutor extends TestCase {
	private GenericFeedbackSaveResponse resp = new GenericFeedbackSaveResponse();

	public void testDoExecutePOIListFeedback() throws ExecutorException {
		GenericFeedbackSaveExecutor excutor = new GenericFeedbackSaveExecutor();
		excutor.doExecute(getGenericFeedbackSaveRequest("POIListFeedback"),
				resp, TestUtil.getExecutorContext());
	}

	public void testDoExecutePOIDetailFeedback() throws ExecutorException {
		GenericFeedbackSaveExecutor excutor = new GenericFeedbackSaveExecutor();
		excutor.doExecute(getGenericFeedbackSaveRequest("POIDetailFeedback"),
				resp, TestUtil.getExecutorContext());
	}

	private GenericFeedbackSaveRequest getGenericFeedbackSaveRequest(
			String feedbackPage) {
		GenericFeedbackSaveRequest request = new GenericFeedbackSaveRequest();
		String[] feedbackStrs = new String[1];
		feedbackStrs[0] = "unit test for GenericFeedbackSaveExecutor";
		request.addFeedbackResponse(1001L, "How", feedbackStrs,
				"Just a unit test");

		request.setFeedbackPage(feedbackPage);
		request.setFeedbackTopic("feedbackTopic");
		
		request.setUserProfile(TestUtil.getUserProfile());
		System.out.println(request.toString());

		return request;
	}

}
