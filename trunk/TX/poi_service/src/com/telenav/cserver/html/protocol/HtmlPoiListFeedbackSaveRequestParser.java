/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.poi.executor.POIFeedbackSaveRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.Stop;

/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class HtmlPoiListFeedbackSaveRequestParser extends HtmlProtocolRequestParser{
	@Override
	public String getExecutorType() {
		return "POIFeedbackSave";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		POIFeedbackSaveRequest request = new POIFeedbackSaveRequest();
		
		String jsonString = httpRequest.getParameter("jsonStr");
		JSONObject json = new JSONObject(jsonString);
		
		String searchCatName = json.getString("searchCatName");
		String searchKeyword = json.getString("searchKeyword");
		
		String searchLocationStr = json.getString("searchLocation");
		JSONObject jobject = new JSONObject(searchLocationStr);
		JSONObject jo = new JSONObject(jobject.getString("stop"));
		Stop searchLocation = PoiUtil.convertAddress(jo);
		
		String feedbackPage = json.getString("feedbackPage");
		String feedbackQuestion = json.getString("feedbackQuestion");
		String feedbacksStr = json.getString("feedbacks");
		String comment = json.getString("comment");
		
		///System.out.println(feedbacksStr);
		JSONArray jarray = new JSONArray(feedbacksStr);
		
		String[] feedbacks = new String[jarray.length()];
		for(int i=0;i<jarray.length();i++){
			feedbacks[i] = jarray.get(i).toString();
		}
		
		request.setSearchCatName(searchCatName);
		request.setSearchKeyword(searchKeyword);
		request.setSearchLocation(searchLocation);
		request.setFeedbackPage(feedbackPage);
		request.setFeedbackQuestion(feedbackQuestion);
		request.setFeedbacks(feedbacks);
		request.setComments(comment);
			
		return request;
			
	}

}
