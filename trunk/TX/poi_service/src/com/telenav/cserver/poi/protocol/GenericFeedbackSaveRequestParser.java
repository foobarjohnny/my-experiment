package com.telenav.cserver.poi.protocol;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.GenericFeedbackSaveRequest;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class GenericFeedbackSaveRequestParser extends BrowserProtocolRequestParser
{
    public String getExecutorType() 
    {
        return "GenericFeedbackSave";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception 
    {
    	GenericFeedbackSaveRequest req = new GenericFeedbackSaveRequest();
    	
    	DataHandler handler = (DataHandler) httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

    	// get request ajax body
        TxNode answerNode = handler.getAJAXBody();
        
        int msgLength = answerNode.msgsSize();
        String feedbackPage = null;
        String feedbackTopic = null;
        
        if(msgLength > 0){
        	feedbackPage = StringEscapeUtils.escapeXml(answerNode.msgAt(0));
        }
        
        if(msgLength > 1){
        	feedbackTopic = StringEscapeUtils.escapeXml(answerNode.msgAt(1));
        }
        
        /**
         * question node structure:
         *    node --- msg [0] --- question string
         *         ----value [0] -- question ID
         *         --- child [0] -- answer choices
         *                       -- msg[0]
         *                       -- msg[1]
         *                       ......
         *         ---  child[1] -- comments
         */
        //retrieve answers
        int questionListSize = answerNode.childrenSize();
        TxNode qNode = null, cNode = null, fNode = null;
        String feedbackQuestion="",  comments="";
        long questionID = -1;
        String[] feedbacks = null;
        int cSize, fLen;
        for(int i=0; i< questionListSize; i++){
        	questionID = -1; //unknown question id.
        	comments=""; //default comments response
        	qNode = answerNode.childAt(i);
        	if(qNode.getValuesCount() > 0){
        		questionID = qNode.valueAt(0);
        	}
        	if(qNode.msgsSize() > 0){
        		feedbackQuestion = StringEscapeUtils.escapeXml(qNode.msgAt(0));
        	}
        	cSize = qNode.childrenSize();
        	
        	if(cSize > 1){
        		cNode = qNode.childAt(1);
        		comments = StringEscapeUtils.escapeXml(cNode.msgAt(0));
        	}
        	if(cSize >0){
        		fNode = qNode.childAt(0);
        		fLen = fNode.msgsSize();
        		feedbacks = new String[fLen];
        		for(int j=0; j< fLen; j++){
        			feedbacks[j] = StringEscapeUtils.escapeXml(fNode.msgAt(j));
        		}
        	}
        	req.addFeedbackResponse(questionID, feedbackQuestion, feedbacks, comments);
        }
        
        //handle customized message fields under the root of answer node.
        //field name and value should come in pairs
        Class reqClass = GenericFeedbackSaveRequest.class;
        Method fMethod;
        String fieldName, addressJsonStr;
        for(int i=2, j=i+1; j< msgLength; i=i+2, j=j+2){
			try {
				fieldName = answerNode.msgAt(i);
				if(fieldName != null && fieldName.toLowerCase().contains("location")){
					addressJsonStr = answerNode.msgAt(j);
					Stop lStop = null;
					if(addressJsonStr != null){
						addressJsonStr = addressJsonStr.replaceAll("_", "\"");
						JSONObject poiLocationJson = new JSONObject(addressJsonStr);
						lStop = TnUtil.convertToStop(poiLocationJson);
					}
					fMethod = reqClass.getMethod("set" + fieldName,
							new Class[] { Stop.class });
					fMethod.invoke(req, new Object[] { lStop });
					
				} else {
					fMethod = reqClass.getMethod("set" + fieldName,
							new Class[] { String.class });
					fMethod.invoke(req, new Object[] { answerNode.msgAt(j) });
				}
			} catch (Exception e) {
				System.out.println("Exception when setting customized fields: "
						+ e.getMessage());
			}
        }

		 
		req.setFeedbackPage(feedbackPage);
    	req.setFeedbackTopic(feedbackTopic);
        return req;
    }
}
