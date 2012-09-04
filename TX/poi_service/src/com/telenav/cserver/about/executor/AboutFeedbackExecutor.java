package com.telenav.cserver.about.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class AboutFeedbackExecutor extends AbstractExecutor {


	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException {
		
		AboutFeedbackResponse aboutFeedbackResponse = (AboutFeedbackResponse)resp;
		
		//TODO do the saving logic here
		boolean submitStatus = true;//new FeedbackManager().saveFeedbackAnswers(aboutFeedbackRequest.getUserId(), aboutFeedbackRequest.getAnswers());
		
		int status = ExecutorResponse.STATUS_FAIL;
		if(submitStatus)
		{
		    status = ExecutorResponse.STATUS_OK;
		}
		
		aboutFeedbackResponse.setStatus(status);
	}

}
