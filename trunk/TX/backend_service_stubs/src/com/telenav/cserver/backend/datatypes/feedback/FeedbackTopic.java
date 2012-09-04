/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

import org.apache.axis2.databinding.utils.ConverterUtil;

/**
 * The topic list of all feedback
 * 
 * @author zhjdou
 * 
 */
public class FeedbackTopic
{
    private String feedbackTopic;

    public static final String _ADDRESS_CAPTURE = ConverterUtil.convertToString("ADDRESS_CAPTURE");

    public static final String _BUSINESS_SEARCH = ConverterUtil.convertToString("BUSINESS_SEARCH");

    public static final String _CANCELLATION = ConverterUtil.convertToString("CANCELLATION");

    public static final String _COMMUTE_ALERT = ConverterUtil.convertToString("COMMUTE_ALERT");

    public static final String _GENERAL = ConverterUtil.convertToString("GENERAL");

    public static final String _MAP = ConverterUtil.convertToString("MAP");

    public static final String _NAVIGATION = ConverterUtil.convertToString("NAVIGATION");

    public static final String _NAVIGATION_MAP = ConverterUtil.convertToString("NAVIGATION_MAP");

    public static final String _ROUTE_GENERATION = ConverterUtil.convertToString("ROUTE_GENERATION");

    public static final String _TRAFFIC = ConverterUtil.convertToString("TRAFFIC");

    public static final String _WEATHER = ConverterUtil.convertToString("WEATHER");

    public static final String _OTHER = ConverterUtil.convertToString("OTHER");

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FeedbackTopic=[");
        sb.append("feedbackTopic=").append(this.feedbackTopic);
        sb.append("]");
        return sb.toString();
    }

    public FeedbackTopic(String value)
    {
        this.feedbackTopic = value;
    }
    
    public static FeedbackTopic createFeedbackTopic(String topic) {
        return new FeedbackTopic(topic);
    }

    /**
     * @return the feedbackTopic
     */
    public String getFeedbackTopic()
    {
        return feedbackTopic;
    }

    /**
     * @param feedbackTopic the feedbackTopic to set
     */
    public void setFeedbackTopic(String feedbackTopic)
    {
        this.feedbackTopic = feedbackTopic;
    }
}
