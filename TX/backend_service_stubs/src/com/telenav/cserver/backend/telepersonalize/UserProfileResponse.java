/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.telepersonalize;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * UserProfileResponse
 * @author kwwang
 * @date 2010-6-4
 */
public class UserProfileResponse
{
    private UserStatus userStatus;

    public UserStatus getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus)
    {
        this.userStatus = userStatus;
    }
    
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
}
