/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * EmailConfirmationResponse
 * @author kwwang
 * @date 2010-6-4
 */
public class EmailConfirmationResponse
{
    private boolean isSuccess;
    private String errorMessage;
    public boolean isSuccess()
    {
        return isSuccess;
    }
    public void setSuccess(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
   
}
