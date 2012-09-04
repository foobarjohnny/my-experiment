package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

public class GenericFeedbackRetrievalRequest extends ExecutorRequest{
	private String feedbackTopic;
	private String pageName;
	private int pageSize;
	private int pageNumber;
	private String locale; 

	public String getFeedbackTopic() {
		return feedbackTopic;
	}

	public void setFeedbackTopic(String feedbackTopic) {
		this.feedbackTopic = feedbackTopic;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	
	
	
}
