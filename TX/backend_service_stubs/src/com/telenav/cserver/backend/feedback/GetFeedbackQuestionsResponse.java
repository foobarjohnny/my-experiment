/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.feedback;

import com.telenav.cserver.backend.datatypes.feedback.FeedbackQuestion;

/**
 * Feedback question response
 * 
 * @author zhjdou 2009-11-5
 */
public class GetFeedbackQuestionsResponse
{
    private FeedbackQuestion[] question;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GetFeedbackQuestionsResponse=[");
        sb.append("feedbackQuestions=");
        if (this.question != null)
        {
            for (FeedbackQuestion quest : this.question)
            {
                if (quest != null)
                {
                    sb.append(quest.toString() + "\n");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the question
     */
    public FeedbackQuestion[] getQuestion()
    {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(FeedbackQuestion[] question)
    {
        this.question = question;
    }

}
