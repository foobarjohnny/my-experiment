/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.poi.executor.GenericFeedbackRetrievalResponse;
import com.telenav.cserver.poi.protocol.GenericFeedbackRetrievalResponseFormatter;
import com.telenav.ws.datatypes.feedback.FeedbackQuestion;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  
 * @version 1.0 
 */
public class HtmlFeedbackRetrievalResponseFormatter extends HtmlProtocolResponseFormatter {
	
	private static Logger logger = Logger.getLogger(GenericFeedbackRetrievalResponseFormatter.class);

	@Override
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		GenericFeedbackRetrievalResponse response = (GenericFeedbackRetrievalResponse)executorResponse;
		FeedbackQuestion[] feedbackList = response.getFeedbackList();
		JSONArray jchoices = new JSONArray();
		JSONObject jfeedback = new JSONObject();
		String localName = "";
		String questionID = "1";
		String question = "";
		String[] choices = null;
		if(feedbackList != null && feedbackList.length > 0){
			localName = feedbackList[0].getName();
			question = feedbackList[0].getQuestion();
			questionID = feedbackList[0].getId()+"";
			choices = feedbackList[0].getChoice();
		}
		
		if(choices!=null&&choices.length>0){
			for(int i=0; i<choices.length; i++){
				jchoices.put(choices[i]);
			}
		}
		
		jfeedback.put("localName", localName);
		jfeedback.put("question", formateQuestion(question));
		jfeedback.put("questionID", questionID);
		jfeedback.put("choices", jchoices);
		
		logger.info("feedback>>>>>>>"+jfeedback);
		
		httpRequest.setAttribute("ajaxResponse",jfeedback.toString());
	}
	
	private String formateQuestion(String question){
		Scanner scanner = new Scanner(question);
		scanner.useDelimiter("\\s*-\\s*");
		return scanner.next();
	}
	
}