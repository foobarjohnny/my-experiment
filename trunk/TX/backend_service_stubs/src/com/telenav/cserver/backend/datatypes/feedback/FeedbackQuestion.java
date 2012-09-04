/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

/**
 * Feedback question
 * 
 * @author zhjdou 2009-11-5
 */
public class FeedbackQuestion
{
    private long id;

    private String name;

    private FeedbackTopic topic;

    private String question;

    private TextAnswerType textAnswerType;

    private String[] choice;

    private String application;

    private String applicationVersion;

    private String carrier;

    private String platform;

    private String device;

    private String locale;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FeedbackQuestion=[");     
        sb.append("id=").append(this.id);
        sb.append(" name=").append(this.name);
        sb.append(" topic=").append(this.topic.toString());
        sb.append(" question=").append(this.question);
        sb.append(" textAnswerType=");
        if(this.textAnswerType!=null) {
            sb.append(this.textAnswerType.toString());
        }
        sb.append(" application=").append(this.application);
        sb.append(" applicationVersion=").append(this.applicationVersion);
        sb.append(" carrier=").append(this.carrier);
        sb.append(" platform=").append(this.platform);
        sb.append(" device=").append(this.device);
        sb.append(" locale=").append(this.locale);
        sb.append(" choice=");
        if (this.choice != null)
        {
            for (String choice : this.choice)
            {
                sb.append(choice+"\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * @return the question
     */
    public String getquestion()
    {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setquestion(String question)
    {
        this.question = question;
    }

    /**
     * @return the textAnswerType
     */
    public TextAnswerType getTextAnswerType()
    {
        return textAnswerType;
    }

    /**
     * @param textAnswerType the textAnswerType to set
     */
    public void setTextAnswerType(TextAnswerType textAnswerType)
    {
        this.textAnswerType = textAnswerType;
    }

    /**
     * @return the choice
     */
    public String[] getChoice()
    {
        return choice;
    }

    /**
     * @param choice the choice to set
     */
    public void setChoice(String[] choice)
    {
        this.choice = choice;
    }

    /**
     * @return the application
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * @return the applicationVersion
     */
    public String getApplicationVersion()
    {
        return applicationVersion;
    }

    /**
     * @param applicationVersion the applicationVersion to set
     */
    public void setApplicationVersion(String applicationVersion)
    {
        this.applicationVersion = applicationVersion;
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
