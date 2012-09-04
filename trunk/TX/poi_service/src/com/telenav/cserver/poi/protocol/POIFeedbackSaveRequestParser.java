package com.telenav.cserver.poi.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.POIFeedbackSaveRequest;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class POIFeedbackSaveRequestParser extends BrowserProtocolRequestParser
{
    public String getExecutorType() 
    {
        return "POIFeedbackSave";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception 
    {
    	POIFeedbackSaveRequest req = new POIFeedbackSaveRequest();
    	
    	DataHandler handler = (DataHandler) httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

    	// get request ajax body
        TxNode body = handler.getAJAXBody();
        
        // get feedbacks from the body, and construct the feedback string array
        int len = (int) body.valueAt(0);
        String[] feedbackStrs = new String[len];
        
		int i = 0;
		for(i = 0; i < len; i++)
		{
			feedbackStrs[i] = body.msgAt(i);
		}
        
		// get the page from which the feedback is invoked
		String feedbackPage = body.msgAt(i);
		i++;

		// get the question that was asked to the user
		String feedbackQuestion = body.msgAt(i);
		i++;

		// get the comments from which the feedback is invoked
		String comments = body.msgAt(i);
		i++;

		if(feedbackPage.equals("POIListFeedback"))
		{		
			// get search keyword, category id, user id from body
			String searchKeyword, searchCatName, addressJsonStr;
			searchKeyword = body.msgAt(i);
			i++;
			searchCatName = body.msgAt(i);
			i++;

			// get search address from the body
			addressJsonStr = body.msgAt(i);
			i++;
			addressJsonStr = addressJsonStr.replaceAll("_", "\"");
			JSONObject searchLocationJson = new JSONObject(addressJsonStr);
			Stop lStop = TnUtil.convertToStop(searchLocationJson);

			// set request attributes
			req.setSearchKeyword(searchKeyword);
			req.setSearchCatName(searchCatName);
			req.setSearchLocation(lStop);
		}
		else if(feedbackPage.equals("POIDetailFeedback"))
		{
			// get poi detail name, phone number and location
			String poiName, poiPhoneNumber, addressJsonStr;
			poiName = body.msgAt(i);
			i++;
			poiPhoneNumber = body.msgAt(i);
			i++;

			// get poi location address from the body
			addressJsonStr = body.msgAt(i);
			i++;
			addressJsonStr = addressJsonStr.replaceAll("_", "\"");
			JSONObject poiLocationJson = new JSONObject(addressJsonStr);
			Stop lStop = TnUtil.convertToStop(poiLocationJson);

			// set request attributes
			req.setPoiName(poiName);
			req.setPoiPhoneNumber(poiPhoneNumber);
			req.setPoiLocation(lStop);
		}

		req.setFeedbacks(feedbackStrs);
		req.setFeedbackQuestion(feedbackQuestion);
		req.setComments(comments);
		req.setFeedbackPage(feedbackPage);
    	
        return req;
    }
}
