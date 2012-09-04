package com.telenav.cserver.poi.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;

import junit.framework.TestCase;

public class TestGenericFeedbackRetrievalExecutor extends TestCase {
	private GenericFeedbackRetrievalResponse resp = new GenericFeedbackRetrievalResponse();

	public void testDoExecute() throws ExecutorException {
		GenericFeedbackRetrievalExecutor excutor = new GenericFeedbackRetrievalExecutor();
		excutor.doExecute(getGenericFeedbackRetrievalRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private GenericFeedbackRetrievalRequest getGenericFeedbackRetrievalRequest() {
		GenericFeedbackRetrievalRequest request = new GenericFeedbackRetrievalRequest();
		request.setFeedbackTopic("feedbackTopic");
		request.setPageName("mainpage");
		request.setPageNumber(1);
		request.setPageSize(1);

		return request;
	}

}
