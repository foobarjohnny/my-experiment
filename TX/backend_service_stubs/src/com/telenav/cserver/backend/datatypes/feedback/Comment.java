/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

import java.util.List;

import javax.activation.DataHandler;

import org.apache.axis2.databinding.utils.ConverterUtil;

/**
 * <b>Comment</b>
 * @author zhjdou
 *
 */
public class Comment
{
    private long id;
    private long commentTypeId;
    private String commentator;
    private String comments;
    private long commentTime;
    private TextAnswerType answerType;
    private String[] choice;
    private MediumType mediumType;
    private DataHandler binaryData;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Comment=[");
        sb.append("id=").append(this.id);
        sb.append(", commentTypeId=").append(this.commentTypeId);
        sb.append(", commentator=").append(this.commentator);
        sb.append(", comments=").append(this.comments);
        sb.append(", commentTime=").append(this.commentTime);
        sb.append(", answerType=");
        if(this.answerType!=null){
            sb.append(this.answerType.toString());
        }
        sb.append(", mediumType=");
        if(this.mediumType!=null){
            sb.append(this.mediumType.toString());
        }
        sb.append(", choice=");
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
     * @return the answerType
     */
    public TextAnswerType getAnswerType()
    {
        return answerType;
    }


    /**
     * @param answerType the answerType to set
     */
    public void setAnswerType(TextAnswerType answerType)
    {
        this.answerType = answerType;
    }

    /**
     * add choice for convenient
     * @param choice
     */
    public void addChoice(String choice){
        if (this.choice == null) {
            this.choice = new String[0];
          }

          List<String> list = ConverterUtil.toList(this.choice);

          list.add(choice);
          this.choice = list.toArray(new String[list.size()]);
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
     * @return the lmediumType
     */
    public MediumType getMediumType()
    {
        return mediumType;
    }


    /**
     * @param lmediumType the lmediumType to set
     */
    public void setMediumType(MediumType mediumType)
    {
        this.mediumType = mediumType;
    }


    /**
     * @return the binaryData
     */
    public DataHandler getBinaryData()
    {
        return binaryData;
    }


    /**
     * @param binaryData the binaryData to set
     */
    public void setBinaryData(DataHandler binaryData)
    {
        this.binaryData = binaryData;
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
     * @return the commentTypeId
     */
    public long getCommentTypeId()
    {
        return commentTypeId;
    }
    /**
     * @param commentTypeId the commentTypeId to set
     */
    public void setCommentTypeId(long commentTypeId)
    {
        this.commentTypeId = commentTypeId;
    }
    /**
     * @return the commentator
     */
    public String getCommentator()
    {
        return commentator;
    }
    /**
     * @param commentator the commentator to set
     */
    public void setCommentator(String commentator)
    {
        this.commentator = commentator;
    }
    /**
     * @return the comments
     */
    public String getComments()
    {
        return comments;
    }
    /**
     * @param comments the comments to set
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    /**
     * @return the commentTime
     */
    public long getCommentTime()
    {
        return commentTime;
    }
    /**
     * @param commentTime the commentTime to set
     */
    public void setCommentTime(long commentTime)
    {
        this.commentTime = commentTime;
    }
}
