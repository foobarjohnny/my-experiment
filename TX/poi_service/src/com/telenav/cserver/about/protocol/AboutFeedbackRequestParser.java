package com.telenav.cserver.about.protocol;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.about.datatypes.FeedbackContents;
import com.telenav.cserver.about.executor.AboutFeedbackRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.xnav.feedback.FeedbackData;
import com.telenav.xnav.feedback.FeedbackTopic;

public class AboutFeedbackRequestParser extends BrowserProtocolRequestParser {

	public String getExecutorType() {
		return "AboutFeedback";
	}

	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest)
			throws Exception {
	    DataHandler handler = (DataHandler) httpRequest
        .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
	    String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);
	    Vector vDefault = FeedbackContents.getInstance().getFeedbackTopics(locale);
	    
	    TxNode body = handler.getAJAXBody();
	    Vector answers = new Vector();
	    if(vDefault != null)
	    {
		    for(int i=0; i<vDefault.size(); i++)
		    {
		        FeedbackTopic topic = (FeedbackTopic)vDefault.get(i);
	            long id = topic.getId();
	            String subject = topic.getSubject();
	            if(subject != null && subject != "")
	            {
	                int selectedIndex = (int)body.valueAt(i);
	                if(selectedIndex <= topic.getAnswers().length)
	                {
	                    String answer = topic.getAnswers()[selectedIndex];
	                    answers.add(new FeedbackData(id,answer));
	                }
	            }
		    }
	    }
	    AboutFeedbackRequest request = new AboutFeedbackRequest();
	    request.setUserId(PoiUtil.getUserId(handler));
	    request.setAnswers(answers);
	    
		return request;
	}

}
