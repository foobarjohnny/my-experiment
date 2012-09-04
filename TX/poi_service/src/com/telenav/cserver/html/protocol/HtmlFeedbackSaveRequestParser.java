/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.poi.executor.GenericFeedbackSaveRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.Stop;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class HtmlFeedbackSaveRequestParser extends HtmlProtocolRequestParser {
	
	private Logger logger = Logger.getLogger(HtmlFeedbackSaveRequestParser.class);
	@Override
	public String getExecutorType() {
		return "GenericFeedbackSave";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
		GenericFeedbackSaveRequest request = new GenericFeedbackSaveRequest();
		
		String jsonString = httpRequest.getParameter("jsonStr");
		JSONObject json = new JSONObject(jsonString);
		
		HtmlClientInfo clientInfo = (HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
		String clientVersion = json.getString("clientVersion");
		if(clientVersion.equals("")){
			clientVersion = HtmlPoiUtil.getVersionNo(clientInfo);
		}

		String email = json.getString("email");
		String userAgent = json.getString("userAgent");
		String OSVersion = getOSVersion(userAgent);
		String pageName = json.optString("pageName");
		String feedbackTopic = getTopic(pageName);
		//String ssoToken = json.getString("ssoToken");
		//long userId = Long.parseLong(HtmlCommonUtil.getUserId(ssoToken));
		long userId = -1;
		try
        {
            userId = Long.parseLong(clientInfo.getUserId());
        }
        catch(NumberFormatException e)
        {
        }
        
		Stop location = new Stop();
		if(pageName.equals("")){
			String currentLocationStr = json.getString("currentLocation");
			if (!currentLocationStr.equals("")) {
				Stop currentLocation = convertToStop(currentLocationStr);
				request.setCurrentLocation(currentLocation);
			}
		}else{
			String locationStr = json.getString("location");
			if (!locationStr.equals("")) {
				location = convertToStop(locationStr);
			}
		}
		
		
		if(!"".equals(email)){
			request.setEmail(email);
		}
		request.setUserId(userId);
		request.setOSVersion(OSVersion);
		request.setFeedbackPage(pageName);
		request.setFeedbackTopic(feedbackTopic);
		
		if (pageName.equalsIgnoreCase("poilist")) {
			request.setSearchKeyword(json.optString("searchKeyword"));
			request.setSearchCatName(json.optString("searchCatName"));
			request.setSearchLocation(location);
		} else if (pageName.equalsIgnoreCase("poidetail")) {
			request.setPoiName(json.optString("poiName"));
			request.setPoiPhoneNumber(json.optString("poiPhoneNumber"));
			request.setPoiID(json.optString("poiID"));
			request.setPoiLocation(location);
		}

		long questionID = -1;
		String strQuestionId = json.optString("questionID");
		if (!"".equals(strQuestionId)) {
			questionID = Long.parseLong(strQuestionId);
		}
		String question = json.optString("question");
		String comments = json.optString("comments");
		String feedbacksStr = json.optString("feedbacks");
		JSONArray jarray = new JSONArray(feedbacksStr);
		String[] answers = new String[jarray.length()];
		for (int i = 0; i < jarray.length(); i++) {
			answers[i] = jarray.get(i).toString();
		}

		request.addFeedbackResponse(questionID, question, answers, comments);
		request.setVersion(clientVersion);
		return request;

	}
	
	/**
	 * @param userAgent
	 * @return
	 */
	public String getOSVersion(String userAgent) {
		String[] array = userAgent.split("[;)]");
		
		String os = "";
		if(array != null && array.length > 4){
			os = array[2];
		}else if(array != null && array.length > 1){
			os = array[1];//ios5
		}
		return os;
	}

	/**
	 * @param pageName
	 * @return
	 */
	private String getTopic(String pageName){
		String topic = "";
		if("poilist".equals(pageName)){
			//poi list feedback
			topic = "SEARCHFEEDBACKV2";
		}else if("poidetail".equals(pageName)){
			//poi detail feedback
			topic = "POIFEEDBACKV2";
		}else{
			//generic feedback
			topic = "GENERALFEEDBACKV2";
		}
		return topic;
	}
	
	/**
	 * @param location
	 * @return
	 * @throws JSONException
	 */
	private Stop convertToStop(String location) throws JSONException{
		JSONObject json = new JSONObject(location);
		Stop stop = PoiUtil.convertAddress(json);
		return stop;
	}

}
