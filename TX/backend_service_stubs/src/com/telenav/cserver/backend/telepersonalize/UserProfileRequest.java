/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * UserProfileRequest
 * @author kwwang
 * @date 2010-6-4
 */
public class UserProfileRequest
{
    private String credentialId;

    private CSCredentialType credentialType;

    public String getCredentialId()
    {
        return credentialId;
    }

    public void setCredentialId(String credentialId)
    {
        this.credentialId = credentialId;
    }

    public CSCredentialType getCredentialType()
    {
        return credentialType;
    }

    public void setCredentialType(CSCredentialType credentialType)
    {
        this.credentialType = credentialType;
    }

    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
}
