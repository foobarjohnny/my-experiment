package com.telenav.cserver.poi.executor;


import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.ws.datatypes.feedback.FeedbackQuestion;

public class GenericFeedbackRetrievalResponse extends ExecutorResponse{

	private FeedbackQuestion[] feedbackList;
	private int pageSize;
	private int pageNumber;

	public FeedbackQuestion[] getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(FeedbackQuestion[] feedbackList) {
		this.feedbackList = feedbackList;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	
	
}
