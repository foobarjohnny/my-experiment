/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.kernel.ws.axis2.Axis2Helper;

/**
 * GetReviewResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-7-7
 *
 */
public class GetReviewResponse
{

    private ReviewServicePOIReview[] review;
    
    private String statusCode;
    
    private String statusMessage;

    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append("GetReviewResponse=");
        sb.append("statusCode=");
        sb.append(statusCode);
        sb.append(",statusMessage=");
        sb.append(statusMessage);
        if( Axis2Helper.isNonEmptyArray(review) ) {
            sb.append(", review");
            for(int index=0;index<this.review.length;index++){
                  sb.append(this.review[index].toString()+"\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * @return the review
     */
    public ReviewServicePOIReview[] getReview()
    {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(ReviewServicePOIReview[] review)
    {
        this.review = review;
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
}
