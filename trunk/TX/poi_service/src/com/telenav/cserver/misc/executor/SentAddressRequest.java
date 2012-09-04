/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class SentAddressRequest extends ExecutorRequest{
    private long userId;
    private String id = "";
    private String action = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String toString() {
		return "[userId] = " + userId + "; [id] = " + id + "; [action] = "
				+ action;
	}
}
