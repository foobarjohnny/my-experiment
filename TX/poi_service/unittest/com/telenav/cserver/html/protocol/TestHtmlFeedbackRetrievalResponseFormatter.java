package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalResponse;
import com.telenav.ws.datatypes.feedback.FeedbackQuestion;

public class TestHtmlFeedbackRetrievalResponseFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		GenericFeedbackRetrievalResponse rResponse = getGenericFeedbackRetrievalResponse();
		HtmlFeedbackRetrievalResponseFormatter formatter = new HtmlFeedbackRetrievalResponseFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GenericFeedbackRetrievalResponse getGenericFeedbackRetrievalResponse(){
		GenericFeedbackRetrievalResponse rResponse = new GenericFeedbackRetrievalResponse();
		FeedbackQuestion[] feedbackList = new FeedbackQuestion[1];
		FeedbackQuestion fq = new FeedbackQuestion();
		fq.setName("");
		fq.setQuestion("cc");
		fq.setId(0);
		String[] choices = {""};
		fq.setChoice(choices);
		feedbackList[0] = fq;
		rResponse.setFeedbackList(feedbackList);
		
		return rResponse;
	}
}
