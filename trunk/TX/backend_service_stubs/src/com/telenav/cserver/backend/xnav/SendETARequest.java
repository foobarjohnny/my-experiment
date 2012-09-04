 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.xnav;

/**
 * The request aid to get trigger action for resend pin.
 * @author pzhang
 * 2010-08-03
 */
public class SendETARequest
{   
	private long userId;
	private String[] ptns;
	private String message;
    private String userClient;
    private String userClientVersion;
    private String contextString;
    
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("SendToETARequest=[");
       sb.append(", userId=").append(this.userId);
       sb.append(", ptns=").append(this.ptns.toString());
       sb.append(", message=").append(this.message);
       sb.append("]");
       return sb.toString();
   }

    /**
     * @return the userClient
     */
    public String getUserClient()
    {
        return userClient;
    }
    /**
     * @param userClient the userClient to set
     */
    public void setUserClient(String userClient)
    {
        this.userClient = userClient;
    }
    /**
     * @return the userClientVersion
     */
    public String getUserClientVersion()
    {
        return userClientVersion;
    }
    /**
     * @param userClientVersion the userClientVersion to set
     */
    public void setUserClientVersion(String userClientVersion)
    {
        this.userClientVersion = userClientVersion;
    }

	public String getContextString() {
		return contextString;
	}

	public void setContextString(String contextString) {
		this.contextString = contextString;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String[] getPtns() {
		return ptns;
	}

	public void setPtns(String[] ptns) {
		this.ptns = ptns;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
   
}
