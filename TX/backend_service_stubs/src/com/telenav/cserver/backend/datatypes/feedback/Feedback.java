/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

import java.util.List;

import org.apache.axis2.databinding.utils.ConverterUtil;

/**
 * Feedback
 * 
 * @author zhjdou
 * 
 */
public class Feedback
{
    private long id;

    private long userId;

    private String ptn; // phone number

    private FeedbackTopic topic;

    private String title="";

    private long feedbackTime;

    private Comment[] comment;

    private NavigationContext navContext;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Feedback=[");
        sb.append("id=").append(this.id);
        sb.append(", userId=").append(this.userId);
        sb.append(", ptn=").append(this.ptn);
        sb.append(", FeedbackTopic=");
        if (topic != null)
        {
            sb.append(this.topic.toString());
        }
        sb.append(", title=").append(this.title);
        sb.append(", feedbackTime=").append(this.feedbackTime);
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

        sb.append(", navContext=");
        if (this.navContext != null)
        {
            sb.append(this.navContext.toString());
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
     * @return the userId
     */
    public long getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    /**
     * @return the ptn
     */
    public String getPtn()
    {
        return ptn;
    }

    /**
     * @param ptn the ptn to set
     */
    public void setPtn(String ptn)
    {
        this.ptn = ptn;
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
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the feedbackTime
     */
    public long getFeedbackTime()
    {
        return feedbackTime;
    }

    /**
     * @param feedbackTime the feedbackTime to set
     */
    public void setFeedbackTime(long feedbackTime)
    {
        this.feedbackTime = feedbackTime;
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
     * add choice for convenient
     * @param choice
     */
    public void addComment(Comment comment){
        if (this.comment == null) {
            this.comment = new Comment[0];
          }

          List<Comment> list = ConverterUtil.toList(this.comment);

          list.add(comment);
          this.comment = list.toArray(new Comment[list.size()]);
    }
    
    /**
     * @return the navContext
     */
    public NavigationContext getNavContext()
    {
        return navContext;
    }

    /**
     * @param navContext the navContext to set
     */
    public void setNavContext(NavigationContext navContext)
    {
        this.navContext = navContext;
    }

}
