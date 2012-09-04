/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.feedback;

import com.telenav.cserver.backend.datatypes.feedback.Feedback;

/**
 * The request of feedback instead of the backend one in c-server internal
 * @author zhjdou
 *
 */
public class FeedbackServiceResponse
{
    private long feedbackId;

    private Feedback feedback;

    private String statusCode;

    private String statusMessage;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FeedbackServiceResponse=[");
        sb.append("Feedback=");
        if(this.feedback!=null) {
            sb.append(this.feedback.toString());
        }
        sb.append(", feedbackId=").append(this.feedbackId);
        sb.append(", statusCode=").append(this.statusCode);
        sb.append(", comment=").append(this.statusMessage);
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * @return the feedbackId
     */
    public long getFeedbackId()
    {
        return feedbackId;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode()
    {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage()
    {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    /**
     * @param feedbackId the feedbackId to set
     */
    public void setFeedbackId(long feedbackId)
    {
        this.feedbackId = feedbackId;
    }

    /**
     * @return the feedback
     */
    public Feedback getFeedback()
    {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(Feedback feedback)
    {
        this.feedback = feedback;
    }

}
