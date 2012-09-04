 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.feedback;

import com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic;

/**
 * The request aid to get the details of feedback question.
 * @author zhjdou
 * 2009-11-5
 */
public class GetFeedbackQuestionsRequest
{   
    private FeedbackTopic topic;
    private String userClient;
    private String userClientVersion;
    private String carrier;
    private String platform;
    private String device;
    private String locale;
    
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("GetFeedbackQuestionsRequest=[");
       sb.append("FeedbackTopic=");
       if (this.topic != null)
       {
           sb.append(this.topic.toString());
       }
       sb.append(", userClient=").append(this.userClient);
       sb.append(", userClientVersion=").append(this.userClientVersion);
       sb.append(", carrier=").append(this.carrier);
       sb.append(", platform=").append(this.platform);
       sb.append(", device=").append(this.device);
       sb.append(", local=").append(this.locale);
       sb.append("]");
       return sb.toString();
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
     * @return the locale
     */
    public String getLocale()
    {
        return locale;
    }
    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale)
    {
        this.locale = locale;
    }
   
}
