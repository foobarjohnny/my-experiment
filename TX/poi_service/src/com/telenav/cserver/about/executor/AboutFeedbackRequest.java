package com.telenav.cserver.about.executor;

import java.util.Vector;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.xnav.feedback.FeedbackData;

public class AboutFeedbackRequest extends ExecutorRequest {
    private long userId;
    private Vector answers;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Vector getAnswers() {
        return answers;
    }

    public void setAnswers(Vector answers) {
        this.answers = answers;
    }
    
    public String toString() {
		StringBuffer answersBuffer = new StringBuffer("");
		if(answers != null && answers.size()!=0){
			for(int i=0;i<answers.size();i++){
				FeedbackData FD = (FeedbackData)answers.get(i);
				if("".equals(answersBuffer.toString())){
					answersBuffer.append(FD.getAnswer());
				}else{
					answersBuffer.append(", " + FD.getAnswer());
				}
			}
		}
		return "[userId] = " + userId + "; [answers] = " + answersBuffer.toString();
	}
}
