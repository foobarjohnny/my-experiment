/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.feedback;

import com.telenav.cserver.backend.datatypes.feedback.Comment;
import com.telenav.cserver.backend.datatypes.feedback.Feedback;
import com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic;

/**
 * The request of feedback instead of the backend one in c-server internal
 * 
 * @author zhjdou
 * 
 */
public class FeedbackServiceRequest
{
    private Feedback feedback;

    private long feedbackId;

    private String userClient;

    private com.telenav.cserver.backend.datatypes.feedback.Comment[] comment;

    private com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic topic;

    private String contextString;

    private String userClientVersion;

    private String carrier;

    private String platform;

    private String device;
    
    private String screenName;

    /**
     * @return the userClientVersion
     */
    public String getUserClientVersion()
    {
        return userClientVersion;
    }

    /**
     * @param userClientVersion the userClientVersion to set
     */
    public void setUserClientVersion(String userClientVersion)
    {
        this.userClientVersion = userClientVersion;
    }

    /**
     * @return the carrier
     */
    public String getCarrier()
    {
        return carrier;
    }

    /**
     * @param carrier the carrier to set
     */
    public void setCarrier(String carrier)
    {
        this.carrier = carrier;
    }

    /**
     * @return the platform
     */
    public String getPlatform()
    {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    /**
     * @return the device
     */
    public String getDevice()
    {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device)
    {
        this.device = device;
    }

    /**
     * @return the screenName
     */
    public String getScreenName()
    {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName)
    {
        this.screenName = screenName;
    }

    /**
     * @return the userClient
     */
    public String getUserClient()
    {
        return userClient;
    }

    /**
     * @param userClient the userClient to set
     */
    public void setUserClient(String userClient)
    {
        this.userClient = userClient;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FeedbackServiceRequest=[");
        sb.append("Feedback=");
        if (this.feedback != null)
        {
            sb.append(this.feedback.toString());
        }
        sb.append(", contextString=").append(this.contextString);
        sb.append(", userClientVersion=").append(this.userClientVersion);
        sb.append(", carrier=").append(this.carrier);
        sb.append(", platform=").append(this.platform);
        sb.append(", device=").append(this.device);
        sb.append(", screenName=").append(this.screenName);
        sb.append(", FeedbackTopic=");
        if (topic != null)
        {
            sb.append(this.topic.toString());
        }
        sb.append(", comment=");
        if (this.comment != null)
        {
            for (Comment com : this.comment)
            {
                if (com != null)
                {
                    sb.append(com.toString());
                    sb.append("\n");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the contextString
     */
    public String getContextString()
    {
        return contextString;
    }

    /**
     * @param contextString the contextString to set
     */
    public void setContextString(String contextString)
    {
        this.contextString = contextString;
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

    /**
     * @return the feedbackId
     */
    public long getFeedbackId()
    {
        return feedbackId;
    }

    /**
     * @param feedbackId the feedbackId to set
     */
    public void setFeedbackId(long feedbackId)
    {
        this.feedbackId = feedbackId;
    }

    /**
     * @return the comment
     */
    public Comment[] getComment()
    {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(Comment[] comment)
    {
        this.comment = comment;
    }

    /**
     * @return the topic
     */
    public FeedbackTopic getTopic()
    {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(FeedbackTopic topic)
    {
        this.topic = topic;
    }
}
