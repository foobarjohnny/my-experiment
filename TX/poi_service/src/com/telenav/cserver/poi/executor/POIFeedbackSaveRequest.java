package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;

public class POIFeedbackSaveRequest extends ExecutorRequest
{

	// page on which feedback is give
	private String feedbackPage;

	// feedbacks
	private String    feedbackQuestion;
	private String[]  feedbacks;
	private String    comments;

	// used for search results flow
	private String    searchKeyword;
	private Stop      searchLocation;
	private String    searchCatName;

	// used for poi detail flow
	private String    poiName;
	private String    poiPhoneNumber;
	private Stop      poiLocation;

	
	public String getFeedbackPage() {
		return feedbackPage;
	}
	public void setFeedbackPage(String feedbackPage) {
		this.feedbackPage = feedbackPage;
	}

	public String getFeedbackQuestion() {
		return feedbackQuestion;
	}
	public void setFeedbackQuestion(String feedbackQuestion) {
		this.feedbackQuestion = feedbackQuestion;
	}

	public String[] getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(String[] feedbacks) {
		this.feedbacks = feedbacks;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Stop getSearchLocation() {
		return searchLocation;
	}
	public void setSearchLocation(Stop searchLocation) {
		this.searchLocation = searchLocation;
	}

	public String getSearchCatName() {
		return searchCatName;
	}
	public void setSearchCatName(String searchCatName) {
		this.searchCatName = searchCatName;
	}

	public String getPoiName() {
		return poiName;
	}
	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public Stop getPoiLocation() {
		return poiLocation;
	}
	public void setPoiLocation(Stop poiLocation) {
		this.poiLocation = poiLocation;
	}

	public String getPoiPhoneNumber() {
		return poiPhoneNumber;
	}
	public void setPoiPhoneNumber(String poiPhoneNumber) {
		this.poiPhoneNumber = poiPhoneNumber;
	}

	public String toString() {

		StringBuffer feedbacksBuffer = new StringBuffer("");
		if (feedbacks != null && feedbacks.length != 0) {
			for (String feedback : feedbacks) {
				if ("".equals(feedbacksBuffer.toString())) {
					feedbacksBuffer.append(feedback);
				} else {
					feedbacksBuffer.append(", " + feedback);
				}
			}
		}

		return "[searchKeyword] = " + searchKeyword + 
			"; [searchLocation] = " + searchLocation + 
			"; [searchCatName] = "  + searchCatName +
			"; [poiName] = "        + poiName +
			"; [poiPhoneNumber] = " + poiPhoneNumber +
			"; [poiLocation] = "    + poiLocation +
			"; [comments] = "       + comments +
			"; [feedbacks] = "      + feedbacks;
	}
}
