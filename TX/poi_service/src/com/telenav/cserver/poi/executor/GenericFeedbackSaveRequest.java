package com.telenav.cserver.poi.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;

public class GenericFeedbackSaveRequest extends ExecutorRequest
{

	// page on which feedback is give
	private String feedbackPage;
	private String feedbackTopic;

	// feedback responses
	private List<FeedbackResponse> feedbackResponses = new ArrayList<FeedbackResponse>();

	// used for search results flow
	private String    searchKeyword;
	private Stop      searchLocation;
	private String    searchCatName;
	private String    pageIndex;

	// used for poi detail flow
	private String    poiName;
	private String    poiPhoneNumber;
	private Stop      poiLocation;
	private String 	  poiID;
	
	private String    transactionID;
	
	// feedback40
	private String email;
	private String version;
	private String OSVersion;
	private long userId;
	private Stop currentLocation;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getOSVersion() {
		return OSVersion;
	}

	public void setOSVersion(String oSVersion) {
		OSVersion = oSVersion;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Stop getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Stop currentLocation) {
		this.currentLocation = currentLocation;
	}

	public void addFeedbackResponse(long questionID, String question, String[] answers, String comments){
		feedbackResponses.add(new FeedbackResponse(questionID, question, answers, comments));
	}
	
	public List<FeedbackResponse> getFeedbackResponses() {
		return feedbackResponses;
	}
	public void setFeedbackResponses(List<FeedbackResponse> feedbackResponses) {
		this.feedbackResponses = feedbackResponses;
	}
	
	public String getFeedbackPage() {
		return feedbackPage;
	}
	public void setFeedbackPage(String feedbackPage) {
		this.feedbackPage = feedbackPage;
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
	
	public String getFeedbackTopic() {
		return feedbackTopic;
	}

	public void setFeedbackTopic(String feedbackTopic) {
		this.feedbackTopic = feedbackTopic;
	}

	
	public String getPoiID() {
		return poiID;
	}

	public void setPoiID(String poiID) {
		this.poiID = poiID;
	}

	
	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String toString() {

		StringBuffer feedbacksBuffer = new StringBuffer("");
		if (feedbackResponses != null && feedbackResponses.size() != 0) {
			for (FeedbackResponse feedbackResponse : feedbackResponses) {
				if ("".equals(feedbacksBuffer.toString())) {
					feedbacksBuffer.append(feedbackResponse);
				} else {
					feedbacksBuffer.append(", " + feedbackResponse);
				}
			}
		}

		return "[searchKeyword] = " + searchKeyword + 
			"; [searchLocation] = " + searchLocation + 
			"; [searchCatName] = "  + searchCatName +
			"; [pageIndex] = "  + pageIndex +
			"; [poiName] = "        + poiName +
			"; [poiPhoneNumber] = " + poiPhoneNumber +
			"; [poiLocation] = "    + poiLocation +
			"; [poiID] = "    + poiID +
			"; [transactionID] = "    + transactionID +
			"; [feedbackPage] = "    + feedbackPage +
			"; [feedbackTopic] = "    + feedbackTopic +
			"; [email] = "  + email +
			"; [version] = " + version +
			"; [OSVersion] = " + OSVersion +
			"; [userid] = " + userId +
			"; [feedbackResponses] = "      + feedbacksBuffer.toString();
	}
	
	public final static class FeedbackResponse{
		private long    questionID;
		private String    feedbackQuestion;
		private String[]  feedbacks;
		private String    comments;
		
		public FeedbackResponse() {
		}
		
		public FeedbackResponse(long questionID, String feedbackQuestion, String[] feedbacks,
				String comments) {
			super();
			this.questionID = questionID;
			this.feedbackQuestion = feedbackQuestion;
			this.feedbacks = feedbacks;
			this.comments = comments;
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

		public long getQuestionID() {
			return questionID;
		}

		public void setQuestionID(long questionID) {
			this.questionID = questionID;
		}
		

		@Override
		public String toString() {
			return "FeedbackResponse [feedbackQuestionID=[" + questionID + "], feedbackQuestion=[" + feedbackQuestion
					+ "], feedbacks=[" + Arrays.toString(feedbacks)
					+ "], comments=[" + comments + "]]";
		}
		
		
	}
}
