/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * EmailConfirmationRequest
 * @author kwwang
 * @date 2010-6-4
 */
public class EmailConfirmationRequest
{
    private String userId;
    private String email;
    private String emailType;
    private String contextString;
    public String getUserId()
    {
        return userId;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEmailType()
    {
        return emailType;
    }
    public void setEmailType(String emailType)
    {
        this.emailType = emailType;
    }
    public String getContextString()
    {
        return contextString;
    }
    public void setContextString(String contextString)
    {
        this.contextString = contextString;
    }
    
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
}
